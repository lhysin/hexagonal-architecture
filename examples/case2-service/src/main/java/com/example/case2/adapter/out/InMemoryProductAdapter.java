package com.example.case2.adapter.out;

import com.example.case2.application.port.in.GetProductDetailUseCase.ProductDetail;
import com.example.case2.application.port.out.CachePort;
import com.example.case2.application.port.out.LoadProductPort;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryProductAdapter implements LoadProductPort, CachePort {
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    @Override
    public ProductDetail load(Long productId) { return new ProductDetail(productId, "sample", 10, "9900"); }

    @Override
    public Optional<String> get(String key) { return Optional.ofNullable(cache.get(key)); }

    @Override
    public void set(String key, String value) { cache.put(key, value); }
}
