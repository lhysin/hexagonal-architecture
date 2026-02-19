package com.example.case1.domain;

public class Payment {
    private final Long id;
    private Status status;

    public Payment(Long id, Status status) {
        this.id = id;
        this.status = status;
    }

    public Long id() { return id; }
    public Status status() { return status; }

    public void approve() {
        if (status != Status.PENDING) throw new IllegalStateException("PENDING only");
        status = Status.APPROVED;
    }

    public enum Status { PENDING, APPROVED }
}
