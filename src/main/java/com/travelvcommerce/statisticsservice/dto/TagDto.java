package com.travelvcommerce.statisticsservice.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class TagDto implements Serializable {
    private String tagId;
    private String tagName;
}
