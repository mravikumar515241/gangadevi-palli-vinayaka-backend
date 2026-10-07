package com.gangadevi.vinayaka.laddu.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LadduPaymentResponse(
    Long id, Long auctionId, BigDecimal amount, LocalDate paymentDate,
    String paymentMethod, String referenceNumber, String notes
) {}
