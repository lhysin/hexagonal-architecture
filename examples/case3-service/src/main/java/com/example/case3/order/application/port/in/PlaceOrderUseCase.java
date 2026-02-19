package com.example.case3.order.application.port.in;

public interface PlaceOrderUseCase {
    String place(Long userId, Long productId, int quantity, String couponCode);
}
