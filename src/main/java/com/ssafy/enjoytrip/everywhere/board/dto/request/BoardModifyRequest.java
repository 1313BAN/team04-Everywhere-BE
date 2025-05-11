package com.ssafy.enjoytrip.everywhere.board.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record BoardModifyRequest(
	String title,
	String content,
	List<MultipartFile> images
) {
}