package com.gangadevi.vinayaka.laddu.controller;
import com.gangadevi.vinayaka.audit.annotation.Audited;
import com.gangadevi.vinayaka.audit.entity.AuditAction;

import com.gangadevi.vinayaka.laddu.dto.*;
import com.gangadevi.vinayaka.laddu.entity.InterestSlab;
import com.gangadevi.vinayaka.laddu.service.LadduAuctionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LadduAuctionController {
    private final LadduAuctionService service;
    public LadduAuctionController(LadduAuctionService service){this.service=service;}

    @GetMapping("/festivals/{year}/laddu-auction")
    public LadduAuctionResponse get(@PathVariable Integer year){return service.getByYear(year);}

    @PostMapping("/festivals/{year}/laddu-auction")
    @ResponseStatus(HttpStatus.CREATED)
    @Audited(action = AuditAction.CREATE, entityType = "LADDU_AUCTION", description = "Created laddu auction", entityIdFromResult = true)
    public LadduAuctionResponse create(@PathVariable Integer year,@Valid @RequestBody LadduAuctionRequest r){return service.create(year,r);}

    @PutMapping("/laddu-auctions/{id}")
    @Audited(action = AuditAction.UPDATE, entityType = "LADDU_AUCTION", description = "Updated laddu auction", entityIdArgument = 0)
    public LadduAuctionResponse update(@PathVariable Long id,@Valid @RequestBody LadduAuctionRequest r){return service.update(id,r);}

    @DeleteMapping("/laddu-auctions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Audited(action = AuditAction.DELETE, entityType = "LADDU_AUCTION", description = "Deleted laddu auction", entityIdArgument = 0)
    public void delete(@PathVariable Long id){service.delete(id);}

    @GetMapping("/laddu-auctions/{auctionId}/payments")
    public List<LadduPaymentResponse> payments(@PathVariable Long auctionId){return service.payments(auctionId);}

    @PostMapping("/laddu-auctions/{auctionId}/payments")
    @ResponseStatus(HttpStatus.CREATED)
    @Audited(action = AuditAction.CREATE, entityType = "LADDU_PAYMENT", description = "Created laddu payment", entityIdFromResult = true)
    public LadduPaymentResponse addPayment(@PathVariable Long auctionId,@Valid @RequestBody LadduPaymentRequest r){return service.addPayment(auctionId,r);}

    @PutMapping("/laddu-payments/{id}")
    @Audited(action = AuditAction.UPDATE, entityType = "LADDU_PAYMENT", description = "Updated laddu payment", entityIdArgument = 0)
    public LadduPaymentResponse updatePayment(@PathVariable Long id,@Valid @RequestBody LadduPaymentRequest r){return service.updatePayment(id,r);}

    @DeleteMapping("/laddu-payments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Audited(action = AuditAction.DELETE, entityType = "LADDU_PAYMENT", description = "Deleted laddu payment", entityIdArgument = 0)
    public void deletePayment(@PathVariable Long id){service.deletePayment(id);}

    @GetMapping("/laddu-auction/interest-slabs")
    public List<InterestSlab> slabs(){return service.interestSlabs();}
}
