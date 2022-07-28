package com.codestates.demo.config;

import com.codestates.demo.filter.FirstFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Profile("bearer")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigBearer {

    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Security filter 중에서 BasicAuthenticationFilter 전에 실행
        // addFilterAfter() 사용해서 후에 실행시킬 수도 있다
        http.addFilterBefore(new FirstFilter(), BasicAuthenticationFilter.class);

        http.csrf().disable(); // form 태그로만 요청 가능, postman 등의 요청이 불가능
        http.headers().frameOptions().disable(); // h2 연결시 필요

        // session / cookie를 만들지 않고 STATELESS로 진행
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // CorsFilter 필터 추가
                .and().addFilter(corsFilter)
                // form Login 사용 안함
                .formLogin().disable()
                // http 통신을 할 때 headers에 Authorization 값을 ID, Password를 입력하는 방식 (사용 안함)
                .httpBasic().disable()
                // 접근 제한할 URI 설정
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
