package com.travelvcommerce.statisticsservice.dto.count;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class VideoCountInfoDto  implements Serializable {
    private String videoId;
    private long views;
    private long likes;
    private long adClicks;
}
