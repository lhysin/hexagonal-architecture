package com.example.case4.adapter.in.web;

import com.example.case4.application.port.in.GetDashboardUseCase;
import com.example.case4.application.port.in.GetDashboardUseCase.DashboardView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/case4/dashboard")
public class DashboardController {
    private final GetDashboardUseCase useCase;

    public DashboardController(GetDashboardUseCase useCase) { this.useCase = useCase; }

    @GetMapping
    DashboardView get(@RequestParam Long userId, @RequestParam(defaultValue = "true") boolean includeCoupon) {
        return useCase.get(userId, includeCoupon);
    }
}
