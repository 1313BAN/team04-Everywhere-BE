package com.ssafy.enjoytrip.everywhere.map.mapper;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.entity.Attraction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttractionMapper {

    AttractionMapper INSTANCE = Mappers.getMapper(AttractionMapper.class);
    AttractionSimpleResponse toSimpleResponse(Attraction attraction);
}
