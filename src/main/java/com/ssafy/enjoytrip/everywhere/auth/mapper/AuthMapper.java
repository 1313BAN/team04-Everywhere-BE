package com.ssafy.enjoytrip.everywhere.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import com.ssafy.enjoytrip.everywhere.auth.dto.response.LoginResponse;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtToken;

@Mapper(
	componentModel = ComponentModel.SPRING,
	unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuthMapper {
	LoginResponse toResponse(JwtToken token);
}