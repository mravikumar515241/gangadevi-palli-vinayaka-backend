package com.gangadevi.vinayaka.activity.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ActivityItemResponse(
        Long id,
        Long activityId,
        String itemName,
        BigDecimal quantity,
        String unit,
        BigDecimal unitCost,
        BigDecimal totalCost,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
