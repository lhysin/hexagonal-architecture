package com.example.hexagonal.application.port.out;

import com.example.hexagonal.application.port.in.GetProductSummaryUseCase.ProductSummaryView;

public interface LoadProductSummaryPort {

    ProductSummaryView load(Long productId);
}
