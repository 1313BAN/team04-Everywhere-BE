package com.ssafy.enjoytrip.everywhere.board.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record BoardModifyRequest(
        String title,
        String content,
        List<MultipartFile> images
) {
}