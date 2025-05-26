package com.ssafy.enjoytrip.everywhere.common.start;

import com.ssafy.enjoytrip.everywhere.map.service.cache.AttractionCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttractionCacheInitRunner {

    private final AttractionCacheService attractionCacheService;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        attractionCacheService.cacheAttractions();
    }
}

