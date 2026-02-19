package com.example.hexagonal.adapter.in.web;

import com.example.hexagonal.application.port.in.PlaceOrderUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @PostMapping
    ResponseEntity<Long> place(@RequestBody @Valid PlaceOrderRequest request) {
        Long orderId = placeOrderUseCase.place(new PlaceOrderUseCase.Command(
                request.buyerId(),
                request.lines().stream().map(line -> new PlaceOrderUseCase.Line(line.productId(), line.quantity(), line.unitPrice())).toList()
        ));
        return ResponseEntity.ok(orderId);
    }

    public record PlaceOrderRequest(@NotNull Long buyerId, @NotEmpty List<OrderLineRequest> lines) {
    }

    public record OrderLineRequest(@NotNull Long productId, @Min(1) int quantity, @NotNull BigDecimal unitPrice) {
    }
}
