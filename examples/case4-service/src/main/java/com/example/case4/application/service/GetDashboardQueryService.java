package com.example.case4.application.service;

import com.example.case4.application.port.in.GetDashboardUseCase;
import com.example.case4.application.port.out.LoadCouponPort;
import com.example.case4.application.port.out.LoadOrderPort;
import com.example.case4.application.port.out.LoadProfilePort;
import org.springframework.stereotype.Service;

@Service
public class GetDashboardQueryService implements GetDashboardUseCase {
    private final LoadProfilePort profile;
    private final LoadOrderPort orders;
    private final LoadCouponPort coupons;

    public GetDashboardQueryService(LoadProfilePort profile, LoadOrderPort orders, LoadCouponPort coupons) {
        this.profile = profile;
        this.orders = orders;
        this.coupons = coupons;
    }

    @Override
    public DashboardView get(Long userId, boolean includeCoupon) {
        return new DashboardView(profile.loadName(userId), orders.loadCount(userId), includeCoupon ? coupons.loadCount(userId) : 0);
    }
}
