package com.example.hexagonal.scenario.case2.application.port.out;

import com.example.hexagonal.scenario.case2.application.port.in.GetProductDetailUseCase.ProductDetailView;

public interface LoadProductDetailPort {
    ProductDetailView load(Long productId);
}
