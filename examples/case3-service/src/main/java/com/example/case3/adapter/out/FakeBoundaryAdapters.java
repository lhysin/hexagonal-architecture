package com.example.case3.adapter.out;

import com.example.case3.coupon.port.in.UseCouponUseCase;
import com.example.case3.inventory.port.in.ReserveStockUseCase;
import org.springframework.stereotype.Component;

@Component
class InventoryAdapter implements ReserveStockUseCase {
    @Override public void reserve(Long productId, int quantity) { }
}

@Component
class CouponAdapter implements UseCouponUseCase {
    @Override public long apply(Long userId, String couponCode, long amount) { return amount - 500; }
}
