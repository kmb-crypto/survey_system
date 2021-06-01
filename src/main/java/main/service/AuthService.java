package main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


}
