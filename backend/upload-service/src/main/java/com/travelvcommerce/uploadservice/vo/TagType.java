package com.travelvcommerce.uploadservice.vo;

public enum TagType {
    REGION,
    THEME;

    public static boolean contains(String type) {
        for (TagType tagType : TagType.values()) {
            if (tagType.name().equals(type.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
