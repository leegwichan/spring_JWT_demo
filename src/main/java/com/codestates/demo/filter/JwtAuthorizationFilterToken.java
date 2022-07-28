package com.codestates.demo.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.codestates.demo.model.Member;
import com.codestates.demo.oauth.PrincipalDetails;
import com.codestates.demo.repository.MemberRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Profile("token")
// BasicAuthenticationFilter : 인증, 권한이 필요하면 실행되는 필터
public class JwtAuthorizationFilterToken extends BasicAuthenticationFilter {

    private MemberRepository memberRepository;

    public JwtAuthorizationFilterToken(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청 됨.");

        String jwtHeader = request.getHeader("Authorization"); // 토큰을 가져옴

        // 정상 토큰인지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request, response);
            return;
        }


        String jwtToken = jwtHeader.replace("Bearer ",""); // 토큰값 정상화
        // username이 잘 왔는지 확인
        String username = JWT.require(Algorithm.HMAC512("cos_jwt_token")).build().verify(jwtToken).getClaim("username").asString();

        // 정상적으로 값이 잘 들어온 경우
        if (username != null){
            Member memberEntity = memberRepository.findByUsername(username);

            PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보를 넘김

            chain.doFilter(request, response);
        }

        super.doFilterInternal(request, response, chain);
    }
}
