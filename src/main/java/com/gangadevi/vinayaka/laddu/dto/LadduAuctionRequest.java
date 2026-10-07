package com.gangadevi.vinayaka.laddu.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LadduAuctionRequest(
    @NotBlank @Size(max = 120) String ownerName,
    @Size(max = 30) String ownerPhone,
    @NotNull @Positive BigDecimal winningAmount,
    @NotNull LocalDate auctionDate
) {}
