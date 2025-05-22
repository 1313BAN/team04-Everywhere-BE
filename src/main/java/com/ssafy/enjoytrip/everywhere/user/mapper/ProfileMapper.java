package com.ssafy.enjoytrip.everywhere.user.mapper;

import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardListResponse;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardSimpleResponse;
import com.ssafy.enjoytrip.everywhere.board.entity.Board;
import com.ssafy.enjoytrip.everywhere.user.dto.response.ProfileResponse;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileMapper {

    public ProfileResponse toResponse(UserEntity user, List<Board> boards) {
        BoardListResponse boardList = new BoardListResponse(boards.stream()
                .map(board -> new BoardSimpleResponse(
                        board.getId(),
                        board.getTitle(),
                        board.getWriter().getNickname(),
                        board.getCreatedAt()
                ))
                .collect(Collectors.toList()));

        return new ProfileResponse(
                user.getUserId(),
                user.getNickname(),
                boardList
        );
    }
}
