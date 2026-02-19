package com.example.case3.order.application.service;

import com.example.case3.coupon.port.in.UseCouponUseCase;
import com.example.case3.inventory.port.in.ReserveStockUseCase;
import com.example.case3.order.application.port.in.PlaceOrderUseCase;
import org.springframework.stereotype.Service;

@Service
public class PlaceOrderService implements PlaceOrderUseCase {
    private final ReserveStockUseCase reserveStock;
    private final UseCouponUseCase useCoupon;

    public PlaceOrderService(ReserveStockUseCase reserveStock, UseCouponUseCase useCoupon) {
        this.reserveStock = reserveStock;
        this.useCoupon = useCoupon;
    }

    @Override
    public String place(Long userId, Long productId, int quantity, String couponCode) {
        reserveStock.reserve(productId, quantity);
        long discounted = useCoupon.apply(userId, couponCode, quantity * 1000L);
        return "order-placed:" + discounted;
    }
}
