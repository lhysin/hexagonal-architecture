package com.example.hexagonal.adapter.out.messaging;

import com.example.hexagonal.application.port.out.PublishDomainEventPort;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventPublisherAdapter implements PublishDomainEventPort {

    private final JdbcClient jdbcClient;

    public OutboxEventPublisherAdapter(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void publish(Object event) {
        jdbcClient.sql("""
                        insert into outbox(event_type, payload, published)
                        values (:eventType, :payload, false)
                        """)
                .param("eventType", event.getClass().getSimpleName())
                .param("payload", event.toString())
                .update();
    }
}
