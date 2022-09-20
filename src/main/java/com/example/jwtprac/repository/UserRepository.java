package com.example.jwtprac.repository;

import com.example.jwtprac.entity.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    // username을 기준으로, User 정보를 가져올 때 권한도 함께 가져오는 메서드
    @EntityGraph(attributePaths = "authorities")  // 해당 쿼리가 수행될 때, Lazy로 가져오지 않고 Eager로 authorities 정보를 함께가져오게 된다.
    Optional<Users> findOneWithAuthoritiesByUsername(String username);
}
