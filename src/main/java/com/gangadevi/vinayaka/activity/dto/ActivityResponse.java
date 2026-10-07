package com.gangadevi.vinayaka.activity.dto;

import com.gangadevi.vinayaka.activity.entity.ActivityStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ActivityResponse(
        Long id,
        Integer festivalYear,
        String name,
        String teluguName,
        BigDecimal plannedBudget,
        BigDecimal actualSpent,
        BigDecimal remainingBudget,
        Integer itemCount,
        String description,
        ActivityStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
