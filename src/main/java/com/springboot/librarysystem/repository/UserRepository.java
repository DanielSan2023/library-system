package com.springboot.librarysystem.repository;

import com.springboot.librarysystem.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends JpaRepository<UserInfo, Long> {

    boolean existsByPersonIdIgnoreCase(String personId);
}
