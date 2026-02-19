package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.in.PlaceOrderUseCase;
import com.example.hexagonal.application.port.out.PublishDomainEventPort;
import com.example.hexagonal.application.port.out.ReserveStockPort;
import com.example.hexagonal.application.port.out.SaveOrderPort;
import com.example.hexagonal.domain.model.order.Order;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaceOrderService implements PlaceOrderUseCase {

    private final SaveOrderPort saveOrderPort;
    private final ReserveStockPort reserveStockPort;
    private final PublishDomainEventPort publishDomainEventPort;

    public PlaceOrderService(SaveOrderPort saveOrderPort,
                             ReserveStockPort reserveStockPort,
                             PublishDomainEventPort publishDomainEventPort) {
        this.saveOrderPort = saveOrderPort;
        this.reserveStockPort = reserveStockPort;
        this.publishDomainEventPort = publishDomainEventPort;
    }

    @Override
    public Long place(Command command) {
        Order order = new Order(
                null,
                command.buyerId(),
                command.lines().stream()
                        .map(line -> new Order.OrderLine(line.productId(), line.quantity(), line.unitPrice()))
                        .collect(Collectors.toList()),
                Order.OrderStatus.CREATED
        );

        order.place();
        order.lines().forEach(line -> reserveStockPort.reserve(line.productId(), line.quantity()));

        Long orderId = saveOrderPort.save(order);
        publishDomainEventPort.publish(new OrderPlacedEvent(orderId, order.buyerId(), order.totalAmount().toPlainString()));

        return orderId;
    }

    public record OrderPlacedEvent(Long orderId, Long buyerId, String totalAmount) {
    }
}
