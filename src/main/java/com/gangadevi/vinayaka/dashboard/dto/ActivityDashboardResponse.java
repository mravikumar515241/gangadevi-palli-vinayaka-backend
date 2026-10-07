package com.gangadevi.vinayaka.dashboard.dto;

import java.math.BigDecimal;

public record ActivityDashboardResponse(
        BigDecimal plannedBudget,
        BigDecimal actualSpent,
        BigDecimal remainingBudget,
        long activityCount
) {}
