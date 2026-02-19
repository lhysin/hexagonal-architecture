package com.example.hexagonal.scenario.case1.domain;

public final class Payment {
    private final Long id;
    private final long amount;
    private Status status;

    public Payment(Long id, long amount, Status status) {
        this.id = id;
        this.amount = amount;
        this.status = status;
    }

    public Long id() { return id; }
    public long amount() { return amount; }
    public Status status() { return status; }

    public void approve() {
        if (status != Status.PENDING) {
            throw new IllegalStateException("결제 대기 상태에서만 승인할 수 있습니다.");
        }
        status = Status.APPROVED;
    }

    public enum Status { PENDING, APPROVED }
}
