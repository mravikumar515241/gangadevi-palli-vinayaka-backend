package com.gangadevi.vinayaka.dashboard.dto;

public record GameDashboardResponse(
        long totalGames,
        long plannedGames,
        long ongoingGames,
        long completedGames,
        long cancelledGames
) {}
