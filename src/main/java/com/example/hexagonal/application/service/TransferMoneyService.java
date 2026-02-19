package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.in.TransferMoneyUseCase;
import com.example.hexagonal.application.port.out.LoadAccountPort;
import com.example.hexagonal.application.port.out.PublishDomainEventPort;
import com.example.hexagonal.application.port.out.UpdateAccountStatePort;
import com.example.hexagonal.domain.model.Account;
import com.example.hexagonal.domain.service.TransferPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 계좌 이체 유스케이스를 처리합니다.
 * <p>
 * 출금/입금 계좌를 조회한 뒤 한도 정책을 검증하고, 계좌 상태를 갱신한 다음
 * 이체 완료 이벤트를 발행합니다.
 */
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

    /**
     * 계좌 이체를 수행합니다.
     *
     * @param command 출금 계좌, 입금 계좌, 이체 금액을 포함한 명령입니다. null일 수 없습니다.
     * @throws IllegalStateException 일 이체 한도를 초과하면 발생합니다.
     * @throws IllegalArgumentException 잔액이 부족하면 발생합니다.
     */
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
