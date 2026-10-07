package com.gangadevi.vinayaka.activity.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ActivityItemRequest(
        @NotBlank @Size(max = 150) String itemName,
        @NotNull @DecimalMin(value = "0.001") @Digits(integer = 11, fraction = 3) BigDecimal quantity,
        @NotBlank @Size(max = 30) String unit,
        @NotNull @DecimalMin(value = "0.00") @Digits(integer = 12, fraction = 2) BigDecimal unitCost,
        @Size(max = 1000) String notes
) {}
