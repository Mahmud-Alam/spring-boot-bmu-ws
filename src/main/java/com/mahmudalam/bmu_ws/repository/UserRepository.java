package com.mahmudalam.bmu_ws.repository;

import com.mahmudalam.bmu_ws.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Optional<User> findOptionalByEmail(String email);

    boolean existsByEmail(String email);
}