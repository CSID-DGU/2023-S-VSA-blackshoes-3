package com.travelvcommerce.uploadservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DenormalizedAdDto implements Serializable {
    private String adId;
    private String adUrl;
    private String adContent;
    private String startTime;
    private String endTime;
}
