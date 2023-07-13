package com.travelvcommerce.uploadservice.vo;

import lombok.Data;

@Data
public class S3Video {
    private String s3Url;
    private String cloudFrontUrl;
}
