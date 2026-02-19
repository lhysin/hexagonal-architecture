package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.out.CachePort;
import java.time.Duration;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class IssueCouponService {

    private final CachePort cachePort;

    public IssueCouponService(CachePort cachePort) {
        this.cachePort = cachePort;
    }

    public String issue(Long userId) {
        String coupon = "WELCOME-" + UUID.randomUUID();
        cachePort.set("coupon:user:" + userId, coupon, Duration.ofHours(24));
        return coupon;
    }
}
