package com.example.case2.adapter.in.web;

import com.example.case2.application.port.in.GetProductDetailUseCase;
import com.example.case2.application.port.in.GetProductDetailUseCase.ProductDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/case2/products")
public class ProductController {
    private final GetProductDetailUseCase useCase;

    public ProductController(GetProductDetailUseCase useCase) { this.useCase = useCase; }

    @GetMapping("/{id}")
    ProductDetail get(@PathVariable Long id) { return useCase.get(id); }
}
