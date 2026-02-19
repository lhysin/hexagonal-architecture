package com.example.case5.payment.port.in;

public interface AuthorizePaymentUseCase { String authorize(Long userId, long amount, String requestId); }
