package com.example.hexagonal.scenario.case5.inventory.port.in;

public interface ReserveStockUseCase {
    void reserve(Long productId, int quantity, String requestId);
}
