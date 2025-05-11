package com.ssafy.enjoytrip.everywhere.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {
    List<String> upload(List<MultipartFile> files);
}