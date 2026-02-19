package com.example.case5.adapter.out;

import com.example.case5.coupon.port.in.ApplyCouponUseCase;
import com.example.case5.inventory.port.in.ReserveStockUseCase;
import com.example.case5.notification.port.in.SendOrderPlacedUseCase;
import com.example.case5.payment.port.in.AuthorizePaymentUseCase;
import com.example.case5.shipping.port.in.CreateShipmentUseCase;
import org.springframework.stereotype.Component;

@Component
class InventoryAdapter implements ReserveStockUseCase { @Override public void reserve(Long productId, int qty, String requestId) { } }

@Component
class CouponAdapter implements ApplyCouponUseCase { @Override public long apply(Long userId, String couponCode, long amount, String requestId) { return amount - 1000; } }

@Component
class PaymentAdapter implements AuthorizePaymentUseCase { @Override public String authorize(Long userId, long amount, String requestId) { return "pay-" + requestId; } }

@Component
class ShippingAdapter implements CreateShipmentUseCase { @Override public String create(Long orderId, String paymentId, String requestId) { return "ship-" + orderId; } }

@Component
class NotificationAdapter implements SendOrderPlacedUseCase { @Override public void send(Long orderId, Long userId) { } }
