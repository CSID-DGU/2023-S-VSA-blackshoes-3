package com.travelvcommerce.contentslaveservice.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserPersonalizedData {
    private String idType;
    private List<String> idList;
}
