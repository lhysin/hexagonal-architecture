package com.example.hexagonal.application.port.out;

import com.example.hexagonal.domain.model.Account;

public interface LoadAccountPort {

    Account loadAccount(Long accountId);

    java.math.BigDecimal loadTodayTransferred(Long accountId);
}
