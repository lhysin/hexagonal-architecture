package com.example.hexagonal.adapter.in.web;

import com.example.hexagonal.application.port.in.GetProductSummaryUseCase;
import com.example.hexagonal.application.port.in.GetProductSummaryUseCase.ProductSummaryView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductQueryController {

    private final GetProductSummaryUseCase getProductSummaryUseCase;

    public ProductQueryController(GetProductSummaryUseCase getProductSummaryUseCase) {
        this.getProductSummaryUseCase = getProductSummaryUseCase;
    }

    @GetMapping("/{productId}/summary")
    ProductSummaryView summary(@PathVariable Long productId) {
        return getProductSummaryUseCase.get(productId);
    }
}
