package com.example.server.service.security;

import com.example.server.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
class CustomUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    public CustomUserDetailsService() throws IOException {
        this.userDao = UserDao.make();
    }
    public UserDetails loadUserByUsername(String userPk) {
        return (UserDetails) userDao.getValueByKey(userPk).orElseThrow();
    }
}
