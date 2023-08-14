package com.travelvcommerce.statisticsservice.dto.count;

import com.travelvcommerce.statisticsservice.entity.Tag;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TotalTagViewCountDto {
    private Tag tag;
    private long totalViewCount;

    @Builder
    public TotalTagViewCountDto(Tag tag, long totalViewCount) {
        this.tag = tag;
        this.totalViewCount = totalViewCount;
    }
}
