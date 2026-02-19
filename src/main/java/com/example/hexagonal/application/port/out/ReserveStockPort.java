package com.example.hexagonal.application.port.out;

public interface ReserveStockPort {

    void reserve(Long productId, int quantity);
}
