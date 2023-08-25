package com.travelvcommerce.userservice.entity;



public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    SELLER("ROLE_SELLER");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
