package com.ssafy.enjoytrip.everywhere.map.mapper;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.entity.Attraction;
import com.ssafy.enjoytrip.everywhere.map.entity.AttractionRedis;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttractionMapper {

    AttractionSimpleResponse toSimpleResponse(Attraction attraction);
    AttractionSimpleResponse toSimpleResponse(AttractionRedis redis);    // Redis용 ✅
}
