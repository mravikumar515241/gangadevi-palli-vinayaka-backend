package com.gangadevi.vinayaka.game.dto;

import com.gangadevi.vinayaka.game.entity.GameCategory;
import com.gangadevi.vinayaka.game.entity.GameStatus;

import java.time.OffsetDateTime;
import java.util.List;

public record GameResponse(
        Long id,
        Integer festivalYear,
        String name,
        String teluguName,
        GameCategory category,
        String description,
        GameStatus status,
        int winnerCount,
        List<GameWinnerResponse> winners,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
