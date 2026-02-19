package com.example.hexagonal.adapter.out.persistence.order;

import com.example.hexagonal.application.port.out.ReserveStockPort;
import com.example.hexagonal.application.port.out.SaveOrderPort;
import com.example.hexagonal.domain.model.order.Order;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class OrderJdbcAdapter implements SaveOrderPort, ReserveStockPort {

    private final JdbcClient jdbcClient;

    public OrderJdbcAdapter(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Long save(Order order) {
        Long orderId = jdbcClient.sql("""
                        insert into orders(buyer_id, status, total_amount)
                        values (:buyerId, :status, :totalAmount)
                        returning id
                        """)
                .param("buyerId", order.buyerId())
                .param("status", order.status().name())
                .param("totalAmount", order.totalAmount())
                .query(Long.class)
                .single();

        order.lines().forEach(line -> jdbcClient.sql("""
                        insert into order_line(order_id, product_id, quantity, unit_price)
                        values (:orderId, :productId, :quantity, :unitPrice)
                        """)
                .param("orderId", orderId)
                .param("productId", line.productId())
                .param("quantity", line.quantity())
                .param("unitPrice", line.unitPrice())
                .update());

        return orderId;
    }

    @Override
    public void reserve(Long productId, int quantity) {
        int updated = jdbcClient.sql("""
                        update product
                        set stock = stock - :qty
                        where id = :id and stock >= :qty
                        """)
                .param("id", productId)
                .param("qty", quantity)
                .update();

        if (updated == 0) {
            throw new IllegalStateException("재고가 부족합니다. productId=" + productId);
        }
    }
}
