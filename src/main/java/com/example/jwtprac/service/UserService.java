package com.example.jwtprac.service;

import com.example.jwtprac.dto.UserDto;
import com.example.jwtprac.entity.Authority;
import com.example.jwtprac.entity.Users;
import com.example.jwtprac.repository.UserRepository;
import com.example.jwtprac.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입 로직
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        // 없으면 권한 생성
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        // 권한도 넣고 유저 만듬
        Users user = Users.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickName())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        // 저장
        return UserDto.from(userRepository.save(user));
    }

    // username을 파라미터로 받아서, username을 가진 유저 객체와 권한 가져옴
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    // 현재 SecurityContext에 저장된 username의 정보만 가져올 수 있음
    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}
