package com.example.case1.application.port.out;

import com.example.case1.domain.Payment;

public interface SavePaymentPort {
    void save(Payment payment);
}
