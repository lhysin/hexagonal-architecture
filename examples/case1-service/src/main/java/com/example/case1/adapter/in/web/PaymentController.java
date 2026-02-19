package com.example.case1.adapter.in.web;

import com.example.case1.application.port.in.ApprovePaymentUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/case1/payments")
public class PaymentController {
    private final ApprovePaymentUseCase useCase;

    public PaymentController(ApprovePaymentUseCase useCase) { this.useCase = useCase; }

    @PostMapping("/approve")
    String approve(@RequestParam Long paymentId) { return useCase.approve(paymentId); }
}
