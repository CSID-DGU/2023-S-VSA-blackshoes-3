package com.travelvcommerce.uploadservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncodingProgressDto {
    private float encodedPercentage;
}
