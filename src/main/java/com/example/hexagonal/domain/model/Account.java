package com.example.hexagonal.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Account {

    private final Long id;
    private BigDecimal balance;

    public Account(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Long id() {
        return id;
    }

    public BigDecimal balance() {
        return balance;
    }

    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        Objects.requireNonNull(amount, "amount");
        balance = balance.add(amount);
    }
}
