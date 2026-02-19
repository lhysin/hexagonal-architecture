package com.example.case5.adapter.in.web;

import com.example.case5.order.application.port.in.PlaceComplexOrderUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/case5/orders")
public class ComplexOrderController {
    private final PlaceComplexOrderUseCase useCase;

    public ComplexOrderController(PlaceComplexOrderUseCase useCase) { this.useCase = useCase; }

    @PostMapping
    String place(@RequestParam String requestId,
                 @RequestParam Long userId,
                 @RequestParam Long productId,
                 @RequestParam int quantity,
                 @RequestParam(defaultValue = "WELCOME") String couponCode) {
        return useCase.place(requestId, userId, productId, quantity, couponCode);
    }
}
