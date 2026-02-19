package com.example.hexagonal.scenario.case5.order.application.port.in;

import java.util.List;

public interface PlaceComplexOrderUseCase {
    Result place(Command command);

    record Command(String requestId, Long userId, String couponCode, List<Line> lines) {}
    record Line(Long productId, int quantity, long amount) {}
    record Result(Long orderId, String paymentId, String shipmentId) {}
}
