package com.travelvcommerce.contentslaveservice.vo;

public enum VideoIdType {
    VIDEOID, ADID;

    public static boolean contains(String q) {
        for (VideoIdType type : VideoIdType.values()) {
            if (type.name().equals(q)) {
                return true;
            }
        }
        return false;
    }
}
