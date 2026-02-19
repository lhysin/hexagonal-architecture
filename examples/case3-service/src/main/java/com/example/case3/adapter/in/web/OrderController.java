package com.example.case3.adapter.in.web;

import com.example.case3.order.application.port.in.PlaceOrderUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/case3/orders")
public class OrderController {
    private final PlaceOrderUseCase useCase;

    public OrderController(PlaceOrderUseCase useCase) { this.useCase = useCase; }

    @PostMapping
    String place(@RequestParam Long userId,
                 @RequestParam Long productId,
                 @RequestParam int quantity,
                 @RequestParam(required = false, defaultValue = "WELCOME") String couponCode) {
        return useCase.place(userId, productId, quantity, couponCode);
    }
}
