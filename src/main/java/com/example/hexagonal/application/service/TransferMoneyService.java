package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.in.TransferMoneyUseCase;
import com.example.hexagonal.application.port.out.LoadAccountPort;
import com.example.hexagonal.application.port.out.PublishDomainEventPort;
import com.example.hexagonal.application.port.out.UpdateAccountStatePort;
import com.example.hexagonal.domain.model.Account;
import com.example.hexagonal.domain.service.TransferPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransferMoneyService implements TransferMoneyUseCase {

    private final LoadAccountPort loadAccountPort;
    private final UpdateAccountStatePort updateAccountStatePort;
    private final PublishDomainEventPort publishDomainEventPort;

    public TransferMoneyService(LoadAccountPort loadAccountPort,
                                UpdateAccountStatePort updateAccountStatePort,
                                PublishDomainEventPort publishDomainEventPort) {
        this.loadAccountPort = loadAccountPort;
        this.updateAccountStatePort = updateAccountStatePort;
        this.publishDomainEventPort = publishDomainEventPort;
    }

    @Override
    public void transfer(Command command) {
        Account source = loadAccountPort.loadAccount(command.fromAccountId());
        Account target = loadAccountPort.loadAccount(command.toAccountId());

        TransferPolicy.validate(loadAccountPort.loadTodayTransferred(source.id()), command.amount());

        source.withdraw(command.amount());
        target.deposit(command.amount());

        updateAccountStatePort.update(source);
        updateAccountStatePort.update(target);

        publishDomainEventPort.publish(new MoneyTransferredEvent(source.id(), target.id(), command.amount().toPlainString()));
    }

    public record MoneyTransferredEvent(Long fromAccountId, Long toAccountId, String amount) {
    }
}
