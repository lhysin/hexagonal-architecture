package com.example.hexagonal.scenario.case4.application.port.in;

public interface GetMyPageDashboardUseCase {
    DashboardView get(Long userId, boolean includeCoupons);

    record DashboardView(String profileName, long orderCount, int availableCouponCount) {}
}
