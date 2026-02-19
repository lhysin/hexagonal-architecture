package com.example.hexagonal.application.port.in;

import java.math.BigDecimal;
import java.util.List;

public interface PlaceOrderUseCase {

    Long place(Command command);

    record Command(Long buyerId, List<Line> lines) {
    }

    record Line(Long productId, int quantity, BigDecimal unitPrice) {
    }
}
