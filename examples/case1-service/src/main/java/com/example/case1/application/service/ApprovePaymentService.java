package com.example.case1.application.service;

import com.example.case1.application.port.in.ApprovePaymentUseCase;
import com.example.case1.application.port.out.LoadPaymentPort;
import com.example.case1.application.port.out.SavePaymentPort;
import com.example.case1.domain.Payment;
import org.springframework.stereotype.Service;

@Service
public class ApprovePaymentService implements ApprovePaymentUseCase {
    private final LoadPaymentPort loadPaymentPort;
    private final SavePaymentPort savePaymentPort;

    public ApprovePaymentService(LoadPaymentPort loadPaymentPort, SavePaymentPort savePaymentPort) {
        this.loadPaymentPort = loadPaymentPort;
        this.savePaymentPort = savePaymentPort;
    }

    @Override
    public String approve(Long paymentId) {
        Payment p = loadPaymentPort.load(paymentId);
        p.approve();
        savePaymentPort.save(p);
        return "approved:" + p.id();
    }
}
