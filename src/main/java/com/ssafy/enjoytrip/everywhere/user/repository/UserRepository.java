package com.ssafy.enjoytrip.everywhere.user.repository;

import com.ssafy.enjoytrip.everywhere.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends
        JpaRepository<User, String> {
    boolean existsByUserId(String userId);

    @Modifying
    @Query("update User u set u.refreshToken = :refreshToken where u.userId = :userId")
    void updateRefreshToken(@Param("userId") String userId, @Param("refreshToken") String refreshToken);
}