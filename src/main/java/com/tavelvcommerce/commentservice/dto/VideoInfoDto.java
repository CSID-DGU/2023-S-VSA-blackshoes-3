package com.tavelvcommerce.commentservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class VideoInfoDto implements Serializable {
    @Data
    public static class VideoCreateDto implements Serializable {
        private String videoId;
        private String videoName;
        private String sellerId;
    }
}
