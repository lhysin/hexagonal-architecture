package com.example.hexagonal.scenario.case3.order.application.port.in;

import java.util.List;

public interface PlaceOrderUseCase {
    Long place(Command command);

    record Command(Long userId, String couponCode, List<Line> lines) {}
    record Line(Long productId, int quantity, long amount) {}
}
