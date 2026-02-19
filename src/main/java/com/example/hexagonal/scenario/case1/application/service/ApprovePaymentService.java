package com.example.hexagonal.scenario.case1.application.service;

import com.example.hexagonal.scenario.case1.application.port.in.ApprovePaymentUseCase;
import com.example.hexagonal.scenario.case1.application.port.out.LoadPaymentPort;
import com.example.hexagonal.scenario.case1.application.port.out.SavePaymentPort;
import com.example.hexagonal.scenario.case1.domain.Payment;

public class ApprovePaymentService implements ApprovePaymentUseCase {
    private final LoadPaymentPort loadPaymentPort;
    private final SavePaymentPort savePaymentPort;

    public ApprovePaymentService(LoadPaymentPort loadPaymentPort, SavePaymentPort savePaymentPort) {
        this.loadPaymentPort = loadPaymentPort;
        this.savePaymentPort = savePaymentPort;
    }

    @Override
    public void approve(Command command) {
        Payment payment = loadPaymentPort.load(command.paymentId());
        payment.approve();
        savePaymentPort.save(payment);
    }
}
