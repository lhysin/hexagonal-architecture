package com.example.hexagonal.application.port.out;

import java.time.Duration;
import java.util.Optional;

public interface CachePort {

    void set(String key, String value, Duration ttl);

    Optional<String> get(String key);
}
