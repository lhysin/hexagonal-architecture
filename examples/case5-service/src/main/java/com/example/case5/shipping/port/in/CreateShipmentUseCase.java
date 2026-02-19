package com.example.case5.shipping.port.in;

public interface CreateShipmentUseCase { String create(Long orderId, String paymentId, String requestId); }
