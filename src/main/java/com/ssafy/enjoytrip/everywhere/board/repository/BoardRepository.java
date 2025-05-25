package com.ssafy.enjoytrip.everywhere.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.enjoytrip.everywhere.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
	@Query("SELECT b FROM Board b JOIN FETCH b.writer")
	List<Board> findAllWithWriter();

	Optional<Board> findById(Long id);

	@Query("SELECT b.writer.userId FROM Board b WHERE b.id = :id")
	String findWriterUserIdByBoardId(@Param("id") Long id);
	List<Board> findByWriter_UserId(String userId);

	@Query("SELECT b FROM Board b JOIN FETCH b.writer LEFT JOIN FETCH b.imageUrls WHERE b.id = :id")
	Optional<Board> findByIdWithWriter(@Param("id") Long id);

}