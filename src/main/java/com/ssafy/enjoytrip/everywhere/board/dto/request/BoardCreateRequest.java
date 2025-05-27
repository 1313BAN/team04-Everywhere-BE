package com.ssafy.enjoytrip.everywhere.board.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

public record BoardCreateRequest(
	String title,
	String content
) {
}