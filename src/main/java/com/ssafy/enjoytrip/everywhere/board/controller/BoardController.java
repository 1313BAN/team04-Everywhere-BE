package com.ssafy.enjoytrip.everywhere.board.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtUtils;
import com.ssafy.enjoytrip.everywhere.auth.security.CustomUserDetails;
import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardCreateRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardModifyRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardDetailResponse;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardListResponse;
import com.ssafy.enjoytrip.everywhere.board.service.BoardService;
import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

	private final JwtUtils jwtUtils;
	private final BoardService service;

	@GetMapping
	public ApiResponse<BoardListResponse> list() {
		return ApiResponse.success(service.findAll());
	}

	@GetMapping("/{id}")
	public ApiResponse<BoardDetailResponse> detail(@PathVariable Long id) {
		return ApiResponse.success(service.findById(id));
	}

	@PostMapping
	public ApiResponse<BoardDetailResponse> create(
		@RequestPart("data") BoardCreateRequest request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images,
		@AuthenticationPrincipal(expression = "username") String userId
	) {
		return ApiResponse.success(service.create(request, images, userId));
	}

	@PutMapping("/{id}")
	@Transactional
	public ApiResponse<BoardDetailResponse> update(@PathVariable Long id,
		@RequestPart("data") BoardModifyRequest request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images,
		@AuthenticationPrincipal(expression = "username") String userId) {
    return ApiResponse.success(service.update(id, request, images, userId));
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> delete(@PathVariable Long id,
									@AuthenticationPrincipal(expression = "username") String userId) {
		service.delete(id, userId);
		return ApiResponse.success();
	}

}