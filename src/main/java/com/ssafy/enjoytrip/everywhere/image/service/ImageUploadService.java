package com.ssafy.enjoytrip.everywhere.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
	List<String> upload(List<MultipartFile> files);
}