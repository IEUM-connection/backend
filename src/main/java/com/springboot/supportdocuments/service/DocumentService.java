package com.springboot.supportdocuments.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    String store(MultipartFile file);
}
