package com.travelvcommerce.statisticsservice.dto.count;

import com.travelvcommerce.statisticsservice.entity.Video;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TotalAdClickCountDto {
    private Video video;
    private long totalAdClickCount;

    @Builder
    public TotalAdClickCountDto(Video video, long totalAdClickCount) {
        this.video = video;
        this.totalAdClickCount = totalAdClickCount;
    }
}
