package com.gangadevi.vinayaka.game.dto;

import com.gangadevi.vinayaka.game.entity.GameCategory;
import com.gangadevi.vinayaka.game.entity.GameStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GameRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 200) String teluguName,
        @NotNull GameCategory category,
        @Size(max = 2000) String description,
        @NotNull GameStatus status
) {}
