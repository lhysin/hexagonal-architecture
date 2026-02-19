package com.example.hexagonal.scenario.case3.inventory.port.in;

public interface ReserveStockUseCase {
    void reserve(Long productId, int quantity);
}
