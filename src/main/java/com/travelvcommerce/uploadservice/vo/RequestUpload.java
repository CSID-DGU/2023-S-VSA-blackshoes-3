package com.travelvcommerce.uploadservice.vo;

import lombok.Data;

import java.util.List;

@Data
public class RequestUpload {
    private String videoName;

    private String sellerName;

    private List<String> tagIdList;

    private List<RequestAd> adList;
}
