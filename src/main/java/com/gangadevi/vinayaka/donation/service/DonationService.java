package com.gangadevi.vinayaka.donation.service;

import com.gangadevi.vinayaka.common.exception.ResourceNotFoundException;
import com.gangadevi.vinayaka.donation.dto.DonationRequest;
import com.gangadevi.vinayaka.donation.dto.DonationResponse;
import com.gangadevi.vinayaka.donation.entity.Donation;
import com.gangadevi.vinayaka.donation.entity.Donor;
import com.gangadevi.vinayaka.donation.repository.DonationRepository;
import com.gangadevi.vinayaka.donation.repository.DonorRepository;
import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import com.gangadevi.vinayaka.festival.repository.FestivalYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DonationService {
    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final FestivalYearRepository festivalYearRepository;

    @Transactional(readOnly = true)
    public List<DonationResponse> findByFestivalYear(Integer year) {
        ensureFestivalYear(year);
        return donationRepository.findByFestivalYearYearOrderByDonationDateDescIdDesc(year)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DonationResponse findById(Long id) {
        return toResponse(getDonation(id));
    }

    public DonationResponse create(Integer year, DonationRequest request) {
        validateAmounts(request);
        FestivalYear festivalYear = getFestivalYear(year);
        Donor donor = getDonor(request.donorId());

        Donation donation = Donation.builder()
                .festivalYear(festivalYear)
                .donor(donor)
                .plannedAmount(request.plannedAmount())
                .receivedAmount(request.receivedAmount())
                .donationDate(request.donationDate())
                .paymentMethod(request.paymentMethod())
                .referenceNumber(normalize(request.referenceNumber()))
                .notes(normalize(request.notes()))
                .build();

        return toResponse(donationRepository.save(donation));
    }

    public DonationResponse update(Long id, DonationRequest request) {
        validateAmounts(request);
        Donation donation = getDonation(id);
        donation.setDonor(getDonor(request.donorId()));
        donation.setPlannedAmount(request.plannedAmount());
        donation.setReceivedAmount(request.receivedAmount());
        donation.setDonationDate(request.donationDate());
        donation.setPaymentMethod(request.paymentMethod());
        donation.setReferenceNumber(normalize(request.referenceNumber()));
        donation.setNotes(normalize(request.notes()));
        return toResponse(donationRepository.save(donation));
    }

    public void delete(Long id) {
        getDonation(id);
        donationRepository.deleteById(id);
    }

    private void validateAmounts(DonationRequest request) {
        if (request.plannedAmount().compareTo(BigDecimal.ZERO) < 0 ||
                request.receivedAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Donation amounts cannot be negative");
        }
        if (request.receivedAmount().compareTo(request.plannedAmount()) > 0) {
            throw new IllegalArgumentException("Received amount cannot be greater than planned amount");
        }
    }

    private FestivalYear getFestivalYear(Integer year) {
        return festivalYearRepository.findByYear(year)
                .orElseThrow(() -> new ResourceNotFoundException("Festival year not found: " + year));
    }

    private void ensureFestivalYear(Integer year) {
        getFestivalYear(year);
    }

    private Donor getDonor(Long id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found: " + id));
    }

    private Donation getDonation(Long id) {
        return donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found: " + id));
    }

    private DonationResponse toResponse(Donation donation) {
        BigDecimal pending = donation.getPlannedAmount().subtract(donation.getReceivedAmount());
        return new DonationResponse(
                donation.getId(),
                donation.getFestivalYear().getYear(),
                donation.getDonor().getId(),
                donation.getDonor().getName(),
                donation.getPlannedAmount(),
                donation.getReceivedAmount(),
                pending,
                donation.getDonationDate(),
                donation.getPaymentMethod(),
                donation.getReferenceNumber(),
                donation.getNotes(),
                donation.getCreatedAt(),
                donation.getUpdatedAt());
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }
}
