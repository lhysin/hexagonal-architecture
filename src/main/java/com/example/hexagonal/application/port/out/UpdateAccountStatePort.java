package com.example.hexagonal.application.port.out;

import com.example.hexagonal.domain.model.Account;

public interface UpdateAccountStatePort {

    void update(Account account);
}
