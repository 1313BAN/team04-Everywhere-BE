package com.ssafy.enjoytrip.everywhere.board.repository;

import com.ssafy.enjoytrip.everywhere.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findByWriterUserId(String userId, Pageable pageable);

    Optional<Board> findById(Long id);
}