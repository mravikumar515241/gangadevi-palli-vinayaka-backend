package com.gangadevi.vinayaka.laddu.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LadduPaymentRequest(
    @NotNull @Positive BigDecimal amount,
    @NotNull LocalDate paymentDate,
    @Size(max = 30) String paymentMethod,
    @Size(max = 100) String referenceNumber,
    @Size(max = 500) String notes
) {}
