package com.example.hexagonal.domain.service;

import java.math.BigDecimal;

public final class TransferPolicy {

    private static final BigDecimal DAILY_LIMIT = new BigDecimal("1000000");

    private TransferPolicy() {
    }

    public static void validate(BigDecimal todayTransferred, BigDecimal requestAmount) {
        if (todayTransferred.add(requestAmount).compareTo(DAILY_LIMIT) > 0) {
            throw new IllegalStateException("1일 이체 한도를 초과했습니다.");
        }
    }
}
