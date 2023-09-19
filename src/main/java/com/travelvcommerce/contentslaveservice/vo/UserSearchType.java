package com.travelvcommerce.contentslaveservice.vo;

public enum UserSearchType {
    VIDEONAME, SELLERNAME, TAGNAME, GPT;

    public static boolean contains(String searchType) {
        for (UserSearchType userSearchType : UserSearchType.values()) {
            if (userSearchType.name().equals(searchType)) {
                return true;
            }
        }
        return false;
    }
}
