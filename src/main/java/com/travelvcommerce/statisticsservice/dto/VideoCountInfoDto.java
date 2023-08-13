package com.travelvcommerce.statisticsservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VideoCountInfoDto {
    private String videoId;
    private long views;
    private long likes;
    private long adClicks;
}
