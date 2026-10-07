package com.gangadevi.vinayaka.dashboard.dto;

import java.math.BigDecimal;

public record DonationDashboardResponse(
        BigDecimal planned,
        BigDecimal received,
        BigDecimal pending,
        long donorCount
) {}
