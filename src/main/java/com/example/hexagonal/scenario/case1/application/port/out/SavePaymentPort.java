package com.example.hexagonal.scenario.case1.application.port.out;

import com.example.hexagonal.scenario.case1.domain.Payment;

public interface SavePaymentPort {
    void save(Payment payment);
}
