package com.example.case2.application.port.out;

import java.util.Optional;

public interface CachePort {
    Optional<String> get(String key);
    void set(String key, String value);
}
