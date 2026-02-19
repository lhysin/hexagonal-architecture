package com.example.hexagonal.adapter.out.persistence.account;

import com.example.hexagonal.application.port.out.LoadAccountPort;
import com.example.hexagonal.application.port.out.UpdateAccountStatePort;
import com.example.hexagonal.domain.model.Account;
import java.math.BigDecimal;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class AccountJdbcAdapter implements LoadAccountPort, UpdateAccountStatePort {

    private final JdbcClient jdbcClient;

    public AccountJdbcAdapter(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Account loadAccount(Long accountId) {
        return jdbcClient.sql("""
                        select id, balance
                        from account
                        where id = :id
                        """)
                .param("id", accountId)
                .query((rs, rowNum) -> new Account(rs.getLong("id"), rs.getBigDecimal("balance")))
                .single();
    }

    @Override
    public BigDecimal loadTodayTransferred(Long accountId) {
        return jdbcClient.sql("""
                        select coalesce(sum(amount), 0)
                        from transfer_history
                        where from_account_id = :accountId
                        and transfer_date = current_date
                        """)
                .param("accountId", accountId)
                .query(BigDecimal.class)
                .single();
    }

    @Override
    public void update(Account account) {
        jdbcClient.sql("""
                        update account
                        set balance = :balance
                        where id = :id
                        """)
                .param("balance", account.balance())
                .param("id", account.id())
                .update();
    }
}
