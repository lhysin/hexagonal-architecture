package com.example.hexagonal.config;

import com.example.hexagonal.application.port.in.TransferMoneyUseCase;
import com.example.hexagonal.application.service.TransferMoneyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlexibleRuleExampleConfig {

    /**
     * 유연 규칙 예시:
     * 복잡도가 낮은 도메인에서는 서비스 자체를 UseCase 타입으로 노출해도 무방하다.
     * 다만 트래픽 급증/복수 어댑터 확장이 예상되면 별도 커맨드 핸들러로 분리한다.
     */
    @Bean
    TransferMoneyUseCase transferMoneyUseCase(TransferMoneyService transferMoneyService) {
        return transferMoneyService;
    }
}
