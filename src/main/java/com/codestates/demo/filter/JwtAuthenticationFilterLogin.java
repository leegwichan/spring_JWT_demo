package com.codestates.demo.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@Profile("login")
@RequiredArgsConstructor
public class JwtAuthenticationFilterLogin extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("login 시도");

        try {
            // request 에서 body 정보를 가져옴
            BufferedReader br = request.getReader();

            String input = null;
            while((input = br.readLine()) != null) {
                System.out.println(input); // 출력
            }
        } catch (IOException e) {
            e.printStackTrace();;
        }
        return super.attemptAuthentication(request, response);
    }
}
