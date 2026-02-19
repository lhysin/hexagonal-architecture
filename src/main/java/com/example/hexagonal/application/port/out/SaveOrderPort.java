package com.example.hexagonal.application.port.out;

import com.example.hexagonal.domain.model.order.Order;

public interface SaveOrderPort {

    Long save(Order order);
}
