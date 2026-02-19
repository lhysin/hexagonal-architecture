package com.example.case1.adapter.out;

import com.example.case1.application.port.out.LoadPaymentPort;
import com.example.case1.application.port.out.SavePaymentPort;
import com.example.case1.domain.Payment;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryPaymentAdapter implements LoadPaymentPort, SavePaymentPort {
    private final Map<Long, Payment> store = new ConcurrentHashMap<>();

    @Override
    public Payment load(Long paymentId) {
        return store.computeIfAbsent(paymentId, id -> new Payment(id, Payment.Status.PENDING));
    }

    @Override
    public void save(Payment payment) {
        store.put(payment.id(), payment);
    }
}
