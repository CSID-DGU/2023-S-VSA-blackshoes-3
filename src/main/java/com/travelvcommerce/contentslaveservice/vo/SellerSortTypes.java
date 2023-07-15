package com.travelvcommerce.contentslaveservice.vo;

public enum SellerSortTypes {
    RECENT, VIEWS, LIKES, ADCLICKS;

    public static boolean contains(String q) {
        for (SellerSortTypes type : SellerSortTypes.values()) {
            if (type.name().equals(q)) {
                return true;
            }
        }
        return false;
    }
}
