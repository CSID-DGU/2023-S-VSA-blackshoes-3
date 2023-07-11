package com.travelvcommerce.uploadservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TagDto {
    private Long id;
    private String tag_id;
    private String type;
    private String content;
}
