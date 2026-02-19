package com.example.hexagonal.scenario.case5.coupon.port.in;

public interface ApplyCouponUseCase {
    long apply(Long userId, String couponCode, long amount, String requestId);
}
