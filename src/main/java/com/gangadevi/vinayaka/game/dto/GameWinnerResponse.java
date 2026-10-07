package com.gangadevi.vinayaka.game.dto;

import com.gangadevi.vinayaka.game.entity.WinnerPosition;

import java.time.OffsetDateTime;

public record GameWinnerResponse(
        Long id,
        Long gameId,
        WinnerPosition position,
        String winnerName,
        String teamName,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
