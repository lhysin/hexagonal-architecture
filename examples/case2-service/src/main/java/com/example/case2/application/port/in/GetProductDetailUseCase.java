package com.example.case2.application.port.in;

public interface GetProductDetailUseCase {
    ProductDetail get(Long productId);

    record ProductDetail(Long id, String name, int stock, String priceText) {}
}
