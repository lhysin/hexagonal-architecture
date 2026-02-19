package com.example.case5.order.application.port.in;

public interface PlaceComplexOrderUseCase {
    String place(String requestId, Long userId, Long productId, int quantity, String couponCode);
}
