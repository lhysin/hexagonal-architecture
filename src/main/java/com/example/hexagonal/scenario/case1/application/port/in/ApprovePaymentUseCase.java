package com.example.hexagonal.scenario.case1.application.port.in;

public interface ApprovePaymentUseCase {
    void approve(Command command);

    record Command(Long paymentId) {}
}
