package com.example.hexagonal.scenario.case4.application.service;

import com.example.hexagonal.scenario.case4.application.port.in.GetMyPageDashboardUseCase;
import com.example.hexagonal.scenario.case4.application.port.out.LoadCouponSummaryPort;
import com.example.hexagonal.scenario.case4.application.port.out.LoadOrderSummaryPort;
import com.example.hexagonal.scenario.case4.application.port.out.LoadProfilePort;

public class GetMyPageDashboardQueryService implements GetMyPageDashboardUseCase {
    private final LoadProfilePort loadProfilePort;
    private final LoadOrderSummaryPort loadOrderSummaryPort;
    private final LoadCouponSummaryPort loadCouponSummaryPort;

    public GetMyPageDashboardQueryService(LoadProfilePort loadProfilePort,
                                          LoadOrderSummaryPort loadOrderSummaryPort,
                                          LoadCouponSummaryPort loadCouponSummaryPort) {
        this.loadProfilePort = loadProfilePort;
        this.loadOrderSummaryPort = loadOrderSummaryPort;
        this.loadCouponSummaryPort = loadCouponSummaryPort;
    }

    @Override
    public DashboardView get(Long userId, boolean includeCoupons) {
        String profileName = loadProfilePort.loadProfileName(userId);
        long orderCount = loadOrderSummaryPort.loadOrderCount(userId);
        int couponCount = includeCoupons ? loadCouponSummaryPort.loadAvailableCouponCount(userId) : 0;
        return new DashboardView(profileName, orderCount, couponCount);
    }
}
