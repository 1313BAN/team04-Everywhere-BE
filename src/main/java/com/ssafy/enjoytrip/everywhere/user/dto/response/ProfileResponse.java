package com.ssafy.enjoytrip.everywhere.user.dto.response;

import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardListResponse;

public record ProfileResponse(
        String userId,
        String nickname,
        BoardListResponse boardList
) {
}

