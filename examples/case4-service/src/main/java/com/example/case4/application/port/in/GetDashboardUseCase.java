package com.example.case4.application.port.in;

public interface GetDashboardUseCase {
    DashboardView get(Long userId, boolean includeCoupon);

    record DashboardView(String profileName, long orderCount, int couponCount) {}
}
