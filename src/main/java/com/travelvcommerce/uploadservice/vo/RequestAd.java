package com.travelvcommerce.uploadservice.vo;

import lombok.Data;

@Data
public class RequestAd {
    private String adUrl;

    private String adContent;

    private String startTime;

    private String endTime;
}
