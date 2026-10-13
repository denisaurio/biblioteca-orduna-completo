package com.biblioteca.controller;

import com.biblioteca.dto.DashboardDTO;
import com.biblioteca.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** INICIO del bibliotecario/gerente. El lector usa GET /api/mi/panel. */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /** GET /api/dashboard - KPIs y libros destacados */
    @GetMapping
    public DashboardDTO dashboard() {
        return dashboardService.dashboard();
    }
}
