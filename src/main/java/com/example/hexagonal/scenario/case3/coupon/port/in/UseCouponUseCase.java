package com.example.hexagonal.scenario.case3.coupon.port.in;

public interface UseCouponUseCase {
    long apply(Long userId, String couponCode, long totalAmount);
}
