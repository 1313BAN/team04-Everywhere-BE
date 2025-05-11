package com.ssafy.enjoytrip.everywhere.board.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardCreateRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.request.BoardModifyRequest;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardDetailResponse;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardListResponse;
import com.ssafy.enjoytrip.everywhere.board.dto.response.BoardSimpleResponse;
import com.ssafy.enjoytrip.everywhere.board.entity.Board;
import com.ssafy.enjoytrip.everywhere.board.mapper.BoardMapper;
import com.ssafy.enjoytrip.everywhere.board.repository.BoardRepository;
import com.ssafy.enjoytrip.everywhere.common.constants.Role;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;
import com.ssafy.enjoytrip.everywhere.image.service.ImageUploadService;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private BoardRepository boardRepository;

	@Mock
	private ImageUploadService imageUploadService;

	@Mock
	private BoardMapper boardMapper;

	@InjectMocks
	private BoardService boardService;

	private UserEntity testUser;
	private Board testBoard;
	private BoardSimpleResponse testBoardSimpleResponse;
	private BoardDetailResponse testBoardDetailResponse;
	private BoardListResponse testBoardListResponse;
	private List<MultipartFile> testImages;
	private List<String> testImageUrls;

	@BeforeEach
	void setUp() {
		testUser = UserEntity.builder()
			.userId("testUserId")
			.nickname("Test User")
			.role(Role.USER)
			.build();

		testBoard = Board.builder()
			.id(1L)
			.title("Test Title")
			.content("Test Content")
			.writer(testUser)
			.build();

		testImages = List.of(
			new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes())
		);

		testImageUrls = List.of("http://test-image-url.com/1");

		testBoardSimpleResponse = new BoardSimpleResponse(
			1L,
			"Test Title",
			testUser.getUserId(),
			LocalDateTime.now()
		);

		testBoardDetailResponse = new BoardDetailResponse(
			1L,
			"Test Title",
			"Test Content",
			testUser.getUserId(),
			testImageUrls,
			LocalDateTime.now()
		);

		testBoardListResponse = new BoardListResponse(List.of(testBoardSimpleResponse));
	}

	@Test
	@DisplayName("모든 게시글 조회")
	void testFindAll() {
		// given
		List<Board> boards = List.of(testBoard);
		BoardListResponse expected = BoardMapper.toList(boards);
		when(boardRepository.findAll()).thenReturn(boards);

		// when
		BoardListResponse result = boardService.findAll();

		// then
		assertThat(result).isEqualTo(expected);
		verify(boardRepository).findAll();
	}

	@Test
	@DisplayName("ID로 게시글 조회 성공")
	void testFindById_Success() {
		// given
		when(boardRepository.findById(anyLong())).thenReturn(Optional.of(testBoard));
		when(boardMapper.toDetail(testBoard)).thenReturn(testBoardDetailResponse);

		// when
		BoardDetailResponse result = boardService.findById(1L);

		// then
		assertThat(result).isEqualTo(testBoardDetailResponse);
		verify(boardRepository).findById(1L);
		verify(boardMapper).toDetail(testBoard);
	}

	@Test
	@DisplayName("ID로 게시글 조회 실패 - 게시글 없음")
	void testFindById_Fail_BoardNotFound() {
		// given
		when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> boardService.findById(1L))
			.isInstanceOf(ApiException.class);
		verify(boardRepository).findById(1L);
	}

	@Test
	@DisplayName("게시글 생성 성공")
	void testCreate_Success() {
		// given
		BoardCreateRequest createRequest = new BoardCreateRequest("New Title", "New Content", testImages);

		when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
		when(imageUploadService.upload(anyList())).thenReturn(testImageUrls);
		when(boardMapper.toEntity(any(BoardCreateRequest.class), any(UserEntity.class), anyList())).thenReturn(
			testBoard);
		when(boardMapper.toDetail(any(Board.class))).thenReturn(testBoardDetailResponse);

		// when
		BoardDetailResponse result = boardService.create(createRequest, "testUserId");

		// then
		assertThat(result).isEqualTo(testBoardDetailResponse);
		verify(userRepository).findById("testUserId");
		verify(imageUploadService).upload(testImages);
		verify(boardMapper).toEntity(createRequest, testUser, testImageUrls);
		verify(boardRepository).save(testBoard);
		verify(boardMapper).toDetail(testBoard);
	}

	@Test
	@DisplayName("게시글 생성 실패 - 사용자 없음")
	void testCreate_Fail_UserNotFound() {
		// given
		BoardCreateRequest createRequest = new BoardCreateRequest("New Title", "New Content", testImages);
		when(userRepository.findById(anyString())).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> boardService.create(createRequest, "nonExistentUserId"))
			.isInstanceOf(ApiException.class);

		verify(userRepository).findById("nonExistentUserId");
	}

	@Test
	@DisplayName("게시글 수정 성공")
	void testUpdate_Success() {
		// given
		BoardModifyRequest modifyRequest = new BoardModifyRequest("Updated Title", "Updated Content", testImages);

		when(boardRepository.findById(anyLong())).thenReturn(Optional.of(testBoard));
		when(boardRepository.findWriterUserIdByBoardId(anyLong())).thenReturn(testUser.getUserId());
		when(imageUploadService.upload(anyList())).thenReturn(testImageUrls);
		when(boardMapper.toDetail(any(Board.class))).thenReturn(testBoardDetailResponse);

		// when
		BoardDetailResponse result = boardService.update(1L, modifyRequest, testUser.getUserId());

		// then
		assertThat(result).isEqualTo(testBoardDetailResponse);
		verify(boardRepository).findById(1L);
		verify(boardRepository).findWriterUserIdByBoardId(1L);
		verify(imageUploadService).upload(testImages);
		verify(boardMapper).toDetail(testBoard);
	}

	@Test
	@DisplayName("게시글 수정 실패 - 게시글 없음")
	void testUpdate_Fail_BoardNotFound() {
		// given
		BoardModifyRequest modifyRequest = new BoardModifyRequest("Updated Title", "Updated Content", testImages);
		when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> boardService.update(1L, modifyRequest, testUser.getUserId()))
			.isInstanceOf(ApiException.class);

		verify(boardRepository).findById(1L);
	}

	@Test
	@DisplayName("게시글 수정 실패 - 권한 없음")
	void testUpdate_Fail_NonValidatedUser() {
		// given
		BoardModifyRequest modifyRequest = new BoardModifyRequest("Updated Title", "Updated Content", testImages);
		String differentUserId = "differentUserId";

		when(boardRepository.findById(anyLong())).thenReturn(Optional.of(testBoard));
		when(boardRepository.findWriterUserIdByBoardId(anyLong())).thenReturn(testUser.getUserId());

		// when & then
		assertThatThrownBy(() -> boardService.update(1L, modifyRequest, differentUserId))
			.isInstanceOf(ApiException.class);

		verify(boardRepository).findById(1L);
		verify(boardRepository).findWriterUserIdByBoardId(1L);
	}

	@Test
	@DisplayName("게시글 삭제 성공")
	void testDelete_Success() {
		// given
		when(boardRepository.findById(anyLong())).thenReturn(Optional.of(testBoard));
		when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
		doNothing().when(boardRepository).delete(any(Board.class));

		// when
		boardService.delete(1L, testUser.getUserId());

		// then
		verify(boardRepository).findById(1L);
		verify(userRepository).findById(testUser.getUserId());
		verify(boardRepository).delete(testBoard);
	}

	@Test
	@DisplayName("게시글 삭제 실패 - 게시글 없음")
	void testDelete_Fail_BoardNotFound() {
		// given
		when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> boardService.delete(1L, testUser.getUserId()))
			.isInstanceOf(ApiException.class);

		verify(boardRepository).findById(1L);
	}

	@Test
	@DisplayName("게시글 삭제 실패 - 사용자 없음")
	void testDelete_Fail_UserNotFound() {
		// given
		when(boardRepository.findById(anyLong())).thenReturn(Optional.of(testBoard));
		when(userRepository.findById(anyString())).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> boardService.delete(1L, "nonExistentUserId"))
			.isInstanceOf(ApiException.class);

		verify(boardRepository).findById(1L);
		verify(userRepository).findById("nonExistentUserId");
	}

	@Test
	@DisplayName("게시글 삭제 실패 - 권한 없음")
	void testDelete_Fail_NonValidatedUser() {
		// given
		UserEntity differentUser = UserEntity.builder()
			.userId("diffUserId")
			.nickname("Test User")
			.role(Role.USER)
			.build();

		when(boardRepository.findById(anyLong())).thenReturn(Optional.of(testBoard));
		when(userRepository.findById(anyString())).thenReturn(Optional.of(differentUser));

		// when & then
		assertThatThrownBy(() -> boardService.delete(1L, differentUser.getUserId()))
			.isInstanceOf(ApiException.class);

		verify(boardRepository).findById(1L);
		verify(userRepository).findById(differentUser.getUserId());
	}
}