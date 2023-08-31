package com.bookend.user.repository;

import com.bookend.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 기존 가입 여부 확인을 위함
}
