package com.travelvcommerce.userservice.security;
import com.travelvcommerce.userservice.entity.Seller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.travelvcommerce.userservice.entity.Users;

import java.util.Collection;
import java.util.Collections;

//user 객체 생성
public class UserPrincipal implements UserDetails {
    private Users user;

    public UserPrincipal(Users user) {
        super();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getRoleName()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //식별자기 때문에 Email 사용
    @Override
    public String getUsername() {
        return user.getEmail();
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

    public static UserPrincipal create(Users user) {
        return new UserPrincipal(user);
    }
}
