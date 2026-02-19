package com.example.hexagonal.domain.model.order;

import java.math.BigDecimal;
import java.util.List;

public final class Order {

    private final Long id;
    private final Long buyerId;
    private final List<OrderLine> lines;
    private OrderStatus status;

    public Order(Long id, Long buyerId, List<OrderLine> lines, OrderStatus status) {
        this.id = id;
        this.buyerId = buyerId;
        this.lines = List.copyOf(lines);
        this.status = status;
    }

    public Long id() {
        return id;
    }

    public Long buyerId() {
        return buyerId;
    }

    public List<OrderLine> lines() {
        return lines;
    }

    public OrderStatus status() {
        return status;
    }

    public BigDecimal totalAmount() {
        return lines.stream()
                .map(OrderLine::lineAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void place() {
        if (lines.isEmpty()) {
            throw new IllegalStateException("주문 라인이 비어있습니다.");
        }
        status = OrderStatus.PLACED;
    }

    public enum OrderStatus {
        CREATED, PLACED
    }

    public record OrderLine(Long productId, int quantity, BigDecimal unitPrice) {
        public BigDecimal lineAmount() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
