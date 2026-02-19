package com.example.case2.application.service;

import com.example.case2.application.port.in.GetProductDetailUseCase;
import com.example.case2.application.port.out.CachePort;
import com.example.case2.application.port.out.LoadProductPort;
import org.springframework.stereotype.Service;

@Service
public class GetProductDetailService implements GetProductDetailUseCase {
    private final CachePort cachePort;
    private final LoadProductPort loadProductPort;

    public GetProductDetailService(CachePort cachePort, LoadProductPort loadProductPort) {
        this.cachePort = cachePort;
        this.loadProductPort = loadProductPort;
    }

    @Override
    public ProductDetail get(Long productId) {
        String key = "p:" + productId;
        return cachePort.get(key).map(v -> {
            String[] p = v.split("\\|");
            return new ProductDetail(productId, p[0], Integer.parseInt(p[1]), p[2]);
        }).orElseGet(() -> {
            ProductDetail d = loadProductPort.load(productId);
            cachePort.set(key, d.name() + "|" + d.stock() + "|" + d.priceText());
            return d;
        });
    }
}
