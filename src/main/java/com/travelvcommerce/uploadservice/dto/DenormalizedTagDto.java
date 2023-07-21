package com.travelvcommerce.uploadservice.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DenormalizedTagDto implements Serializable {
    private String tagId;
    private String tagName;
}
