package com.example.case3.coupon.port.in;

public interface UseCouponUseCase {
    long apply(Long userId, String couponCode, long amount);
}
