package com.travelvcommerce.contentslaveservice.vo;

public enum UserPersonalizedTypes {
    HISTORY, TAG, LIKE;

    public static boolean contains(String q) {
        for (UserPersonalizedTypes type : UserPersonalizedTypes.values()) {
            if (type.name().equals(q)) {
                return true;
            }
        }
        return false;
    }
}
