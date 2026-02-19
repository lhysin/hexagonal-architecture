package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.in.GetProductSummaryUseCase;
import com.example.hexagonal.application.port.out.CachePort;
import com.example.hexagonal.application.port.out.LoadProductSummaryPort;
import java.time.Duration;
import org.springframework.stereotype.Service;

@Service
public class GetProductSummaryService implements GetProductSummaryUseCase {

    private final CachePort cachePort;
    private final LoadProductSummaryPort loadProductSummaryPort;

    public GetProductSummaryService(CachePort cachePort, LoadProductSummaryPort loadProductSummaryPort) {
        this.cachePort = cachePort;
        this.loadProductSummaryPort = loadProductSummaryPort;
    }

    @Override
    public ProductSummaryView get(Long productId) {
        String key = "product:summary:" + productId;

        return cachePort.get(key)
                .map(cached -> {
                    String[] parts = cached.split("\\|");
                    return new ProductSummaryView(productId, parts[0], Integer.parseInt(parts[1]), parts[2]);
                })
                .orElseGet(() -> {
                    ProductSummaryView loaded = loadProductSummaryPort.load(productId);
                    cachePort.set(key, loaded.name() + "|" + loaded.stock() + "|" + loaded.displayPrice(), Duration.ofMinutes(10));
                    return loaded;
                });
    }
}
