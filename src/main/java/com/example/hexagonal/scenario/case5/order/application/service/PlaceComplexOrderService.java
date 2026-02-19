package com.example.hexagonal.scenario.case5.order.application.service;

import com.example.hexagonal.scenario.case5.coupon.port.in.ApplyCouponUseCase;
import com.example.hexagonal.scenario.case5.inventory.port.in.ReserveStockUseCase;
import com.example.hexagonal.scenario.case5.notification.port.in.SendOrderPlacedUseCase;
import com.example.hexagonal.scenario.case5.order.application.port.in.PlaceComplexOrderUseCase;
import com.example.hexagonal.scenario.case5.payment.port.in.AuthorizePaymentUseCase;
import com.example.hexagonal.scenario.case5.shipping.port.in.CreateShipmentUseCase;

public class PlaceComplexOrderService implements PlaceComplexOrderUseCase {
    private final ReserveStockUseCase reserveStockUseCase;
    private final ApplyCouponUseCase applyCouponUseCase;
    private final AuthorizePaymentUseCase authorizePaymentUseCase;
    private final CreateShipmentUseCase createShipmentUseCase;
    private final SendOrderPlacedUseCase sendOrderPlacedUseCase;

    public PlaceComplexOrderService(ReserveStockUseCase reserveStockUseCase,
                                    ApplyCouponUseCase applyCouponUseCase,
                                    AuthorizePaymentUseCase authorizePaymentUseCase,
                                    CreateShipmentUseCase createShipmentUseCase,
                                    SendOrderPlacedUseCase sendOrderPlacedUseCase) {
        this.reserveStockUseCase = reserveStockUseCase;
        this.applyCouponUseCase = applyCouponUseCase;
        this.authorizePaymentUseCase = authorizePaymentUseCase;
        this.createShipmentUseCase = createShipmentUseCase;
        this.sendOrderPlacedUseCase = sendOrderPlacedUseCase;
    }

    @Override
    public Result place(Command command) {
        long total = command.lines().stream().mapToLong(Line::amount).sum();
        command.lines().forEach(line -> reserveStockUseCase.reserve(line.productId(), line.quantity(), command.requestId()));

        long discounted = applyCouponUseCase.apply(command.userId(), command.couponCode(), total, command.requestId());
        String paymentId = authorizePaymentUseCase.authorize(command.userId(), discounted, command.requestId());

        Long orderId = System.nanoTime();
        String shipmentId = createShipmentUseCase.create(orderId, paymentId, command.requestId());

        sendOrderPlacedUseCase.send(orderId, command.userId());
        return new Result(orderId, paymentId, shipmentId);
    }
}
