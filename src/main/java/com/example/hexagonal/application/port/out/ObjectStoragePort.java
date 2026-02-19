package com.example.hexagonal.application.port.out;

public interface ObjectStoragePort {

    String put(String bucket, String key, byte[] content, String contentType);
}
