package com.gangadevi.vinayaka.dashboard.controller;

import com.gangadevi.vinayaka.dashboard.dto.DashboardResponse;
import com.gangadevi.vinayaka.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/festivals")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/{year}/dashboard")
    public DashboardResponse getDashboard(@PathVariable Integer year) {
        return dashboardService.getDashboard(year);
    }
}
