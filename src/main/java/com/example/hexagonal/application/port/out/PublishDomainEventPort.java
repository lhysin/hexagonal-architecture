package com.example.hexagonal.application.port.out;

public interface PublishDomainEventPort {

    void publish(Object event);
}
