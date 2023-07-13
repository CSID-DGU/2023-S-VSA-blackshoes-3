package com.travelvcommerce.uploadservice.vo;

import lombok.Data;

public enum TagTypes {
    REGION,
    THEME;

    public static boolean contains(String type) {
        for (TagTypes tagType : TagTypes.values()) {
            if (tagType.name().equals(type.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
