package com.codestates.demo.controller;

import com.codestates.demo.model.Member;
import com.codestates.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Profile({"login", "token"})
@RestController
@RequiredArgsConstructor
public class RestApiControllerLogin {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }


    @PostMapping("/join")
    public String join(@RequestBody Member member){
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setRoles("ROLE_USER");
        memberRepository.save(member);
        return "회원 가입 완료";
    }


    @Profile("token")
    @GetMapping("/api/v1/user")
    public String user() {
        return "user";
    }

    @Profile("token")
    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }
}
