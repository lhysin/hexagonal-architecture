package com.example.hexagonal.application.port.in;

import java.math.BigDecimal;

public interface TransferMoneyUseCase {

    void transfer(Command command);

    record Command(Long fromAccountId, Long toAccountId, BigDecimal amount) {
    }
}
