package com.example.case5.order.application.service;

import com.example.case5.coupon.port.in.ApplyCouponUseCase;
import com.example.case5.inventory.port.in.ReserveStockUseCase;
import com.example.case5.notification.port.in.SendOrderPlacedUseCase;
import com.example.case5.order.application.port.in.PlaceComplexOrderUseCase;
import com.example.case5.payment.port.in.AuthorizePaymentUseCase;
import com.example.case5.shipping.port.in.CreateShipmentUseCase;
import org.springframework.stereotype.Service;

@Service
public class PlaceComplexOrderService implements PlaceComplexOrderUseCase {
    private final ReserveStockUseCase reserve;
    private final ApplyCouponUseCase coupon;
    private final AuthorizePaymentUseCase payment;
    private final CreateShipmentUseCase shipping;
    private final SendOrderPlacedUseCase notify;

    public PlaceComplexOrderService(ReserveStockUseCase reserve,
                                    ApplyCouponUseCase coupon,
                                    AuthorizePaymentUseCase payment,
                                    CreateShipmentUseCase shipping,
                                    SendOrderPlacedUseCase notify) {
        this.reserve = reserve;
        this.coupon = coupon;
        this.payment = payment;
        this.shipping = shipping;
        this.notify = notify;
    }

    @Override
    public String place(String requestId, Long userId, Long productId, int quantity, String couponCode) {
        reserve.reserve(productId, quantity, requestId);
        long discounted = coupon.apply(userId, couponCode, quantity * 1000L, requestId);
        String paymentId = payment.authorize(userId, discounted, requestId);
        long orderId = System.nanoTime();
        String shipmentId = shipping.create(orderId, paymentId, requestId);
        notify.send(orderId, userId);
        return "ok:" + orderId + ":" + shipmentId;
    }
}
