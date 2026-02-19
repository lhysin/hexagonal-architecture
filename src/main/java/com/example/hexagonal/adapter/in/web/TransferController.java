package com.example.hexagonal.adapter.in.web;

import com.example.hexagonal.application.port.in.TransferMoneyUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferMoneyUseCase transferMoneyUseCase;

    public TransferController(TransferMoneyUseCase transferMoneyUseCase) {
        this.transferMoneyUseCase = transferMoneyUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequest request) {
        transferMoneyUseCase.transfer(new TransferMoneyUseCase.Command(request.fromAccountId(), request.toAccountId(), request.amount()));
        return ResponseEntity.accepted().build();
    }

    public record TransferRequest(@NotNull Long fromAccountId,
                                  @NotNull Long toAccountId,
                                  @NotNull @Positive BigDecimal amount) {
    }
}
