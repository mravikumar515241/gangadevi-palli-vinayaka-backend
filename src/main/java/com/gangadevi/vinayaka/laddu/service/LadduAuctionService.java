package com.gangadevi.vinayaka.laddu.service;

import com.gangadevi.vinayaka.laddu.dto.*;
import com.gangadevi.vinayaka.common.exception.ResourceNotFoundException;
import com.gangadevi.vinayaka.common.exception.ConflictException;
import com.gangadevi.vinayaka.common.exception.BusinessException;
import com.gangadevi.vinayaka.laddu.entity.*;
import com.gangadevi.vinayaka.laddu.repository.*;
import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import com.gangadevi.vinayaka.festival.repository.FestivalYearRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class LadduAuctionService {
    private final LadduAuctionRepository auctions;
    private final LadduPaymentRepository payments;
    private final InterestSlabRepository slabs;
    private final FestivalYearRepository years;

    public LadduAuctionService(LadduAuctionRepository auctions, LadduPaymentRepository payments,
                                InterestSlabRepository slabs, FestivalYearRepository years) {
        this.auctions=auctions; this.payments=payments; this.slabs=slabs; this.years=years;
    }

    public LadduAuctionResponse create(Integer year, LadduAuctionRequest r) {
        FestivalYear fy=years.findByYear(year).orElseThrow(() -> new ResourceNotFoundException("Festival year not found: "+year));
        if(auctions.findByFestivalYear(fy).isPresent()) throw new ConflictException("A laddu auction already exists for year "+year);
        LadduAuction a=new LadduAuction();
        a.setFestivalYear(fy); a.setOwnerName(r.ownerName()); a.setOwnerPhone(r.ownerPhone());
        a.setWinningAmount(money(r.winningAmount())); a.setAuctionDate(r.auctionDate());
        a.setDueDate(r.auctionDate().plusMonths(1));
        recalculate(a);
        return response(auctions.save(a));
    }

    @Transactional(readOnly=true)
    public LadduAuctionResponse getByYear(Integer year) {
        FestivalYear fy=years.findByYear(year).orElseThrow(() -> new ResourceNotFoundException("Festival year not found: "+year));
        return response(auctions.findByFestivalYear(fy).orElseThrow(() -> new ResourceNotFoundException("Laddu auction not found for year "+year)));
    }

    public LadduAuctionResponse update(Long id,LadduAuctionRequest r) {
        LadduAuction a=get(id); a.setOwnerName(r.ownerName()); a.setOwnerPhone(r.ownerPhone());
        a.setWinningAmount(money(r.winningAmount())); a.setAuctionDate(r.auctionDate()); a.setDueDate(r.auctionDate().plusMonths(1));
        recalculate(a); return response(auctions.save(a));
    }

    public void delete(Long id) { auctions.delete(get(id)); }

    public LadduPaymentResponse addPayment(Long auctionId,LadduPaymentRequest r) {
        LadduAuction a=get(auctionId); BigDecimal total=payments.sumAmountByAuctionId(auctionId);
        BigDecimal proposed=total.add(r.amount());
        if(proposed.compareTo(a.getWinningAmount().add(a.getInterestAmount()))>0)
            throw new BusinessException("Payment exceeds current total payable amount");
        LadduPayment p=new LadduPayment(); p.setAuction(a); p.setAmount(money(r.amount())); p.setPaymentDate(r.paymentDate());
        p.setPaymentMethod(r.paymentMethod()); p.setReferenceNumber(r.referenceNumber()); p.setNotes(r.notes());
        LadduPayment saved=payments.save(p); recalculate(a); auctions.save(a); return paymentResponse(saved);
    }

    @Transactional(readOnly=true)
    public List<LadduPaymentResponse> payments(Long auctionId) {
        get(auctionId); return payments.findByAuctionIdOrderByPaymentDateAscIdAsc(auctionId).stream().map(this::paymentResponse).toList();
    }

    public LadduPaymentResponse updatePayment(Long id,LadduPaymentRequest r) {
        LadduPayment p=payments.findById(id).orElseThrow(() -> new ResourceNotFoundException("Laddu payment not found: "+id));
        p.setAmount(money(r.amount())); p.setPaymentDate(r.paymentDate()); p.setPaymentMethod(r.paymentMethod());
        p.setReferenceNumber(r.referenceNumber()); p.setNotes(r.notes()); LadduPayment saved=payments.save(p);
        recalculate(p.getAuction()); auctions.save(p.getAuction()); return paymentResponse(saved);
    }

    public void deletePayment(Long id) {
        LadduPayment p=payments.findById(id).orElseThrow(() -> new ResourceNotFoundException("Laddu payment not found: "+id));
        LadduAuction a=p.getAuction(); payments.delete(p); payments.flush(); recalculate(a); auctions.save(a);
    }

    @Transactional(readOnly=true)
    public List<InterestSlab> interestSlabs() { return slabs.findByActiveTrueOrderByMinAmountAsc(); }

    private void recalculate(LadduAuction a) {
        BigDecimal paid=a.getId()==null?BigDecimal.ZERO:payments.sumAmountByAuctionId(a.getId());
        List<LadduPayment> ps=a.getId()==null?List.of():payments.findByAuctionIdOrderByPaymentDateAscIdAsc(a.getId());
        LocalDate last=ps.stream().map(LadduPayment::getPaymentDate).max(LocalDate::compareTo).orElse(null);

        // Business rule: full payment on or before auction date + 1 month = no interest.
        // After the grace date, the configured slab is applied as a ONE-TIME percentage.
        boolean fullWithinGrace=paid.compareTo(a.getWinningAmount())>=0 && last!=null && !last.isAfter(a.getDueDate());
        BigDecimal rate=BigDecimal.ZERO;
        if(!fullWithinGrace && ((last!=null && last.isAfter(a.getDueDate())) || (last==null && LocalDate.now().isAfter(a.getDueDate())))) {
            rate=slabs.findByActiveTrueOrderByMinAmountAsc().stream()
                .filter(s -> a.getWinningAmount().compareTo(s.getMinAmount())>=0)
                .filter(s -> s.getMaxAmount()==null || a.getWinningAmount().compareTo(s.getMaxAmount())<0)
                .map(InterestSlab::getInterestRate).findFirst().orElse(BigDecimal.ZERO);
        }
        BigDecimal interest=a.getWinningAmount().multiply(rate).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
        a.setInterestRate(rate); a.setInterestAmount(interest); a.setTotalPayable(a.getWinningAmount().add(interest));

        BigDecimal outstanding=a.getTotalPayable().subtract(paid);
        if(paid.signum()==0) a.setStatus("PENDING");
        else if(outstanding.signum()<=0) a.setStatus("PAID");
        else if(last!=null && last.isAfter(a.getDueDate())) a.setStatus("OVERDUE");
        else a.setStatus("PARTIALLY_PAID");
    }

    private LadduAuction get(Long id) { return auctions.findById(id).orElseThrow(() -> new ResourceNotFoundException("Laddu auction not found: "+id)); }
    private BigDecimal money(BigDecimal v) { return v.setScale(2,RoundingMode.HALF_UP); }

    private LadduAuctionResponse response(LadduAuction a) {
        BigDecimal paid=a.getId()==null?BigDecimal.ZERO:payments.sumAmountByAuctionId(a.getId());
        return new LadduAuctionResponse(a.getId(),a.getFestivalYear().getYear(),a.getOwnerName(),a.getOwnerPhone(),
            a.getWinningAmount(),a.getAuctionDate(),a.getDueDate(),a.getInterestRate(),a.getInterestAmount(),
            a.getTotalPayable(),paid,a.getTotalPayable().subtract(paid),a.getStatus());
    }

    private LadduPaymentResponse paymentResponse(LadduPayment p) {
        return new LadduPaymentResponse(p.getId(),p.getAuction().getId(),p.getAmount(),p.getPaymentDate(),
            p.getPaymentMethod(),p.getReferenceNumber(),p.getNotes());
    }
}
