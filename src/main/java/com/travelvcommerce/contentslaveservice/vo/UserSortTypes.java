package com.travelvcommerce.contentslaveservice.vo;

public enum UserSortTypes {
    RECENT, VIEWS, LIKES;


    public static boolean contains(String q) {
        for (UserSortTypes type : UserSortTypes.values()) {
            if (type.name().equals(q)) {
                return true;
            }
        }
        return false;
    }
}
