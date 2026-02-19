package com.example.hexagonal.scenario.case3.order.application.service;

import com.example.hexagonal.scenario.case3.coupon.port.in.UseCouponUseCase;
import com.example.hexagonal.scenario.case3.inventory.port.in.ReserveStockUseCase;
import com.example.hexagonal.scenario.case3.order.application.port.in.PlaceOrderUseCase;

public class PlaceOrderService implements PlaceOrderUseCase {
    private final ReserveStockUseCase reserveStockUseCase;
    private final UseCouponUseCase useCouponUseCase;

    public PlaceOrderService(ReserveStockUseCase reserveStockUseCase, UseCouponUseCase useCouponUseCase) {
        this.reserveStockUseCase = reserveStockUseCase;
        this.useCouponUseCase = useCouponUseCase;
    }

    @Override
    public Long place(Command command) {
        long total = command.lines().stream().mapToLong(PlaceOrderUseCase.Line::amount).sum();
        command.lines().forEach(line -> reserveStockUseCase.reserve(line.productId(), line.quantity()));
        useCouponUseCase.apply(command.userId(), command.couponCode(), total);
        return System.currentTimeMillis();
    }
}
