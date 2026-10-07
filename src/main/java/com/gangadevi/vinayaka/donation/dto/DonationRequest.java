package com.gangadevi.vinayaka.donation.dto;

import com.gangadevi.vinayaka.donation.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DonationRequest(
        @NotNull Long donorId,
        @NotNull @DecimalMin(value = "0.00") @Digits(integer = 12, fraction = 2) BigDecimal plannedAmount,
        @NotNull @DecimalMin(value = "0.00") @Digits(integer = 12, fraction = 2) BigDecimal receivedAmount,
        LocalDate donationDate,
        PaymentMethod paymentMethod,
        @Size(max = 100) String referenceNumber,
        @Size(max = 1000) String notes
) {}
