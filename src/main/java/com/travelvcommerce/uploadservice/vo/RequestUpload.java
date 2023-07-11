package com.travelvcommerce.uploadservice.vo;

import lombok.Data;

import java.util.List;

@Data
public class RequestUpload {
    private String videoName;

    private List<String> tagIdList;
}
