package com.zest.productapi.product_management_api.repository;

import com.zest.productapi.product_management_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}

