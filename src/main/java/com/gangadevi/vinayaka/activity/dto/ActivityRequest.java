package com.gangadevi.vinayaka.activity.dto;

import com.gangadevi.vinayaka.activity.entity.ActivityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ActivityRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 200) String teluguName,
        @NotNull @DecimalMin(value = "0.00") @Digits(integer = 12, fraction = 2) BigDecimal plannedBudget,
        @Size(max = 2000) String description,
        @NotNull ActivityStatus status
) {}
