package com.ssafy.enjoytrip.everywhere.board.controller;

import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtTokenProvider;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtUtils;
import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardCreateRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardModifyRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardDetailResponse;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardListResponse;
import com.ssafy.enjoytrip.everywhere.board.service.BoardService;
import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final JwtTokenProvider jwtTokenProvider;
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
    public ApiResponse<BoardDetailResponse> create(@ModelAttribute BoardCreateRequest request,
                                                   @RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getUserId(token);
        return ApiResponse.success(service.create(request, userId));
    }

    @PutMapping("/{id}")
    public ApiResponse<BoardDetailResponse> update(@PathVariable Long id,
                                                   @ModelAttribute BoardModifyRequest request,
                                                   @RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getUserId(token);
        return ApiResponse.success(service.update(id, request, userId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id,
                                    @RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getUserId(token);
        service.delete(id, userId);
        return ApiResponse.success();
    }


}