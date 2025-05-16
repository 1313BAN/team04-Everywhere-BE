package com.ssafy.enjoytrip.everywhere.map.dto.response;

import java.util.List;

public record AttractionsResponse(
        List<AttractionSimpleResponse> attractions
) {}
