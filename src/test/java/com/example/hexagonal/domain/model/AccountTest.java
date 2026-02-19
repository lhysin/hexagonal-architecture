package com.example.hexagonal.domain.model;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Account 테스트")
class AccountTest {

    @Nested
    @DisplayName("withdraw 메서드")
    class WithdrawMethod {

        @Test
        @DisplayName("잔액이 충분하면 출금 후 잔액이 감소해야 한다")
        void shouldDecreaseBalanceWhenBalanceIsEnough() {
            // given
            Account account = new Account(1L, new BigDecimal("1000"));

            // when
            account.withdraw(new BigDecimal("300"));

            // then
            assertThat(account.balance()).isEqualByComparingTo("700");
        }

        @Test
        @DisplayName("잔액이 부족하면 예외를 던져야 한다")
        void shouldThrowExceptionWhenBalanceIsNotEnough() {
            // given
            Account account = new Account(1L, new BigDecimal("100"));

            // when & then
            assertThatThrownBy(() -> account.withdraw(new BigDecimal("300")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("잔액이 부족");
        }
    }
}
