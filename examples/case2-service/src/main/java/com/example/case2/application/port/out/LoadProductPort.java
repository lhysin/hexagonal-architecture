package com.example.case2.application.port.out;

import com.example.case2.application.port.in.GetProductDetailUseCase.ProductDetail;

public interface LoadProductPort {
    ProductDetail load(Long productId);
}
