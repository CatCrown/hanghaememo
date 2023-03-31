package com.sparta.hanghaememo.security;


import com.sparta.hanghaememo.entity.User;
import com.sparta.hanghaememo.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    //UserDetailsImpl 클래스는 UserDetails 인터페이스를 구현한 클래스입니다.
    //회원가입 및 로그인을 위한 사용자 정보를 관리하고 인증을 수행하는 데 필요한 메서드입니다.
   // 이 클래스는 User 객체를 매개변수로 받아 UserDetails 객체로 변환합니다.

    private final User user;
    private final String username;

    public UserDetailsImpl(User user, String username) {
        this.user = user;
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();


        // SimpleGrantedAuthority 클래스를 사용하여 사용자 권한을 설정합니다.
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return null;
    }



    // 계정이 만료되지 않았는지
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    //잠겨있지 않은지
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    //자격증명이 만료되지 않았는지
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    //계정이 활성화되어 있는지
    @Override
    public boolean isEnabled() {
        return false;
    }
}