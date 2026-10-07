package com.gangadevi.vinayaka.laddu.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LadduAuctionResponse(
    Long id, Integer festivalYear, String ownerName, String ownerPhone,
    BigDecimal winningAmount, LocalDate auctionDate, LocalDate dueDate,
    BigDecimal interestRate, BigDecimal interestAmount, BigDecimal totalPayable,
    BigDecimal totalPaid, BigDecimal outstandingAmount, String status
) {}
