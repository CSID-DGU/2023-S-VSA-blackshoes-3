package com.travelvcommerce.userservice.security;
import com.travelvcommerce.userservice.entity.Seller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

//user 객체 생성
public class SellerPrincipal implements UserDetails {
    private Seller seller;

    public SellerPrincipal(Seller seller) {
        super();
        this.seller = seller;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(seller.getRole().getRoleName()));
    }

    @Override
    public String getPassword() {
        return seller.getPassword();
    }

    //식별자기 때문에 Email 사용
    @Override
    public String getUsername() {
        return seller.getEmail();
    }

    public String getId() {
        return seller.getSellerId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static SellerPrincipal create(Seller seller) {
        return new SellerPrincipal(seller);
    }
}
