package com.codestates.demo.oauth;

import com.codestates.demo.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Profile({"login", "token"})
@AllArgsConstructor
@Getter
//  "/login" 주소에 요청이 오면 대신 로그인을 진행
public class PrincipalDetails implements UserDetails {

    private Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        member.getRoleList().forEach(n -> {
            authorities.add(() -> n);
        });
        return authorities;
    }

    @Override
    // 비밀번호 정보 리턴
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    // ID 정보 리턴
    public String getUsername() {
        return member.getUsername();
    }

    // 따로 규칙이 없는 경우, return true;
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    // 암호 사용 기간이 지났는지에 관해 확인
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // 특정 사이트 규칙에 따라 return false로 설정. (ex. 1년 동안 로그인을 하지 않았을 경우)
    public boolean isEnabled(){
        return true;
    }
}
