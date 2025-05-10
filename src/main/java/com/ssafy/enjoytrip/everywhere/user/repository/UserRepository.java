package com.ssafy.enjoytrip.everywhere.user.repository;

import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends
        JpaRepository<UserEntity, String> {
    boolean existsByUserId(String userId);

    @Modifying
    @Query("update UserEntity u set u.refreshToken = :refreshToken where u.userId = :userId")
    void updateRefreshToken(@Param("userId") String userId, @Param("refreshToken") String refreshToken);
}