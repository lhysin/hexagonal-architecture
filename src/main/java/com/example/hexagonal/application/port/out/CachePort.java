package com.example.hexagonal.application.port.out;

import java.time.Duration;
import java.util.Optional;

/**
 * 캐시 저장소와의 상호작용을 추상화한 출력 포트입니다.
 */
public interface CachePort {

    /**
     * 캐시에 값을 저장합니다.
     *
     * @param key 캐시 키입니다. null일 수 없습니다.
     * @param value 저장할 문자열 값입니다. null일 수 없습니다.
     * @param ttl 키 만료 시간입니다. null일 수 없습니다.
     */
    void set(String key, String value, Duration ttl);

    /**
     * 캐시에서 값을 조회합니다.
     *
     * @param key 조회할 캐시 키입니다. null일 수 없습니다.
     * @return 캐시 값이 존재하면 Optional에 값을 담아 반환하고, 없으면 빈 Optional을 반환합니다.
     */
    Optional<String> get(String key);
}
