package com.ssafy.enjoytrip.everywhere.board.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record BoardCreateRequest(
        String title,
        String content,
        List<MultipartFile> images
) {
}