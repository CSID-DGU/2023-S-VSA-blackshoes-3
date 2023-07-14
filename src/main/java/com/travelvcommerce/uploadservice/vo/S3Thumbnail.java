package com.travelvcommerce.uploadservice.vo;

import lombok.Data;

@Data
public class S3Thumbnail {
    private String s3Url;
    private String cloudFrontUrl;
}
