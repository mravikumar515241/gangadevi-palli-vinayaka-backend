package com.gangadevi.vinayaka.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LadduDashboardResponse(
        boolean exists,
        Long auctionId,
        String ownerName,
        BigDecimal winningAmount,
        LocalDate auctionDate,
        LocalDate dueDate,
        BigDecimal interestRate,
        BigDecimal interestAmount,
        BigDecimal totalPayable,
        BigDecimal totalPaid,
        BigDecimal outstandingAmount,
        String status
) {
    public static LadduDashboardResponse empty() {
        return new LadduDashboardResponse(
                false, null, null, BigDecimal.ZERO, null, null,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, null);
    }
}
