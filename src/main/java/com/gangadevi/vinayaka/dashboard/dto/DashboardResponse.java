package com.gangadevi.vinayaka.dashboard.dto;

public record DashboardResponse(
        Integer festivalYear,
        DonationDashboardResponse donations,
        ActivityDashboardResponse activities,
        GameDashboardResponse games,
        GalleryDashboardResponse gallery,
        LadduDashboardResponse ladduAuction
) {}
