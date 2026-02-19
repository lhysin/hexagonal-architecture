package com.example.hexagonal.adapter.out.storage;

import com.example.hexagonal.application.port.out.ObjectStoragePort;
import org.springframework.stereotype.Component;

@Component
public class S3ObjectStorageAdapter implements ObjectStoragePort {

    @Override
    public String put(String bucket, String key, byte[] content, String contentType) {
        // 예제 목적: 실서비스에서는 AWS SDK v2 S3Client를 주입 받아 putObject 수행
        return "https://cdn.example.com/" + bucket + "/" + key;
    }
}
