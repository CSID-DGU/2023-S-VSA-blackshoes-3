package com.travelvcommerce.contentslaveservice.vo;

public enum SellerSortType {
    RECENT, VIEWS, LIKES, ADCLICKS;

    public static boolean contains(String q) {
        for (SellerSortType type : SellerSortType.values()) {
            if (type.name().equals(q)) {
                return true;
            }
        }
        return false;
    }
}
