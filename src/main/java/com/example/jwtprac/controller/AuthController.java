package com.example.jwtprac.controller;

import com.example.jwtprac.dto.LoginDto;
import com.example.jwtprac.dto.TokenDto;
import com.example.jwtprac.jwt.JwtFilter;
import com.example.jwtprac.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // 받아온 정보로 새로운 AuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // 아래 라인이 실행될 때, CustomUserDetails의 loadUserByUsername 메소드가 실행 됨
        // CustomUserDetails의 loadUserByUsername의 결과값으로 인해 Authentication 객체가 생성되는 것임!!
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);  // SecurityContext에 저장

        String jwt = tokenProvider.createToken(authentication);  // jwt 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.PREFIX + jwt);  // 헤더에 넣어줌

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);  // 바디에도 넣어줌
    }
}
