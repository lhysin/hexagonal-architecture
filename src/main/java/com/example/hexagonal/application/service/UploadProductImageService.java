package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.out.ObjectStoragePort;
import org.springframework.stereotype.Service;

@Service
public class UploadProductImageService {

    private final ObjectStoragePort objectStoragePort;

    public UploadProductImageService(ObjectStoragePort objectStoragePort) {
        this.objectStoragePort = objectStoragePort;
    }

    public String upload(Long productId, byte[] content) {
        return objectStoragePort.put("products", "product-" + productId + ".png", content, "image/png");
    }
}
