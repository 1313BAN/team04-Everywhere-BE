package com.ssafy.enjoytrip.everywhere.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import com.ssafy.enjoytrip.everywhere.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND + userId));

		return new CustomUserDetails(userEntity);
	}

}