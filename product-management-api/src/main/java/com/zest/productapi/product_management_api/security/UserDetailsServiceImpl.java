package com.zest.productapi.product_management_api.security;

import com.zest.productapi.product_management_api.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repo;

    public UserDetailsServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return repo.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }
}