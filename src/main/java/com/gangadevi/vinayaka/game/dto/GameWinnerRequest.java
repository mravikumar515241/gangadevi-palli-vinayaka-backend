package com.gangadevi.vinayaka.game.dto;

import com.gangadevi.vinayaka.game.entity.WinnerPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GameWinnerRequest(
        @NotNull WinnerPosition position,
        @NotBlank @Size(max = 150) String winnerName,
        @Size(max = 150) String teamName,
        @Size(max = 1000) String notes
) {}
