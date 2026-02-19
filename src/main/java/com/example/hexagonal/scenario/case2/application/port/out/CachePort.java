package com.example.hexagonal.scenario.case2.application.port.out;

import java.time.Duration;
import java.util.Optional;

public interface CachePort {
    Optional<String> get(String key);
    void set(String key, String value, Duration ttl);
}
