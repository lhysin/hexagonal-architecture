package com.example.hexagonal.scenario.case2.application.port.in;

public interface GetProductDetailUseCase {
    ProductDetailView get(Long productId);

    record ProductDetailView(Long productId, String name, int stock, String priceText) {}
}
