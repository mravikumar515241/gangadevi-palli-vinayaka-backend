package com.gangadevi.vinayaka.donation.dto;

import com.gangadevi.vinayaka.donation.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record DonationResponse(
        Long id,
        Integer festivalYear,
        Long donorId,
        String donorName,
        BigDecimal plannedAmount,
        BigDecimal receivedAmount,
        BigDecimal pendingAmount,
        LocalDate donationDate,
        PaymentMethod paymentMethod,
        String referenceNumber,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
