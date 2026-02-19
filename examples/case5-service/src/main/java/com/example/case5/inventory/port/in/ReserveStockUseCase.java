package com.example.case5.inventory.port.in;

public interface ReserveStockUseCase { void reserve(Long productId, int qty, String requestId); }
