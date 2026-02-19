package com.example.hexagonal.scenario.case2.application.service;

import com.example.hexagonal.scenario.case2.application.port.in.GetProductDetailUseCase;
import com.example.hexagonal.scenario.case2.application.port.out.CachePort;
import com.example.hexagonal.scenario.case2.application.port.out.LoadProductDetailPort;
import java.time.Duration;

public class GetProductDetailService implements GetProductDetailUseCase {
    private final CachePort cachePort;
    private final LoadProductDetailPort loadProductDetailPort;

    public GetProductDetailService(CachePort cachePort, LoadProductDetailPort loadProductDetailPort) {
        this.cachePort = cachePort;
        this.loadProductDetailPort = loadProductDetailPort;
    }

    @Override
    public ProductDetailView get(Long productId) {
        String key = "scenario2:product:" + productId;
        return cachePort.get(key)
                .map(v -> {
                    String[] p = v.split("\\|");
                    return new ProductDetailView(productId, p[0], Integer.parseInt(p[1]), p[2]);
                })
                .orElseGet(() -> {
                    ProductDetailView loaded = loadProductDetailPort.load(productId);
                    cachePort.set(key, loaded.name() + "|" + loaded.stock() + "|" + loaded.priceText(), Duration.ofMinutes(5));
                    return loaded;
                });
    }
}
