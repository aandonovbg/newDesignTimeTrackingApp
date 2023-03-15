package com.example.newDesignApp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageProcessService {
    public byte[] convertMultipartToByteArray(MultipartFile file) throws IOException {
        return file.getBytes();
    }
}
