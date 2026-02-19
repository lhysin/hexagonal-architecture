package com.example.hexagonal.scenario.case5.notification.port.in;

public interface SendOrderPlacedUseCase {
    void send(Long orderId, Long userId);
}
