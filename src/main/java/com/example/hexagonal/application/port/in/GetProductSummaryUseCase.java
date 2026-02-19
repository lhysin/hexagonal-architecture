package com.example.hexagonal.application.port.in;

public interface GetProductSummaryUseCase {

    ProductSummaryView get(Long productId);

    record ProductSummaryView(Long productId, String name, int stock, String displayPrice) {
    }
}
