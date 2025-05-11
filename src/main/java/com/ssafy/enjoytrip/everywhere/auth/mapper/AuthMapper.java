package com.ssafy.enjoytrip.everywhere.auth.mapper;

import com.ssafy.enjoytrip.everywhere.auth.dto.response.LoginResponse;
import com.ssafy.enjoytrip.everywhere.auth.jwt.JwtToken;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuthMapper {
    LoginResponse toResponse(JwtToken token);
}