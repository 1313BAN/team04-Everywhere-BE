package com.ssafy.enjoytrip.everywhere.image.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LocalImageUploadService implements ImageUploadService {

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/uploads/";

    @Override
    public List<String> upload(List<MultipartFile> files) {
        return files.stream()
                .map(this::saveFile)
                .collect(Collectors.toList());
    }

    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일입니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storedFileName = UUID.randomUUID() + extension;

        try {
            Path savePath = Paths.get(UPLOAD_DIR + storedFileName);
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            file.transferTo(savePath);
            return "/images/" + storedFileName; // 실제론 클라이언트에 제공할 URL 경로
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패: " + e.getMessage());
        }
    }
}