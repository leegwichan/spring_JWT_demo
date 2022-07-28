package com.codestates.demo.filter;

import org.springframework.context.annotation.Profile;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Profile("bearer")
public class FirstFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException{

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setCharacterEncoding("UTF-8");

        // POST 요청 중에서
        if(req.getMethod().equals("POST")){
            // request Header 에서 Authorization 탭에서 값을 가져옴
            String headerAuth = req.getHeader("Authorization");

            if (headerAuth.equals("codestates")){
                chain.doFilter(req, res); // 필터 계속 진행
            } else {
                // response에 "인증 실패"를 Body에 넣고 응답함
                PrintWriter writer = res.getWriter();
                writer.println("인증 실패");
            }
        }
    }
}
