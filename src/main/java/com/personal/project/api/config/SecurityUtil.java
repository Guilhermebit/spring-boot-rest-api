package com.personal.project.api.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {

    public String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}
