package com.travelvcommerce.uploadservice.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3Thumbnail {
    private String s3Url;
    private String cloudfrontUrl;
}
