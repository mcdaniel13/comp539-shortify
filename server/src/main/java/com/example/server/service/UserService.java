package com.example.server.service;

import com.example.server.config.security.JwtTokenProvider;
import com.example.server.dao.UserDao;
import com.example.server.model.SignInUser;
import com.example.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) throws IOException {
        this.userDao = UserDao.make();
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    public List<User> findAllUsers() {
        return userDao.readTable();
    }

    public User findUserById(String userId) throws Exception {
        return userDao.getValueByKey(userId).orElseThrow(Exception::new);
    }

    public User createUser(String userId, String password, String name, String level, String role) throws Exception {
        List<String> roles = new ArrayList<>();
        roles.add(role);

        User user = User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .name(name)
                .timeStamp(System.currentTimeMillis() + "")
                .level(level)
                .roles(roles)
                .build();

        if (userDao.checkKey(userId))
            throw new Exception();

        userDao.insertValue(user.getUserId(), user);
        return user;
    }

    public SignInUser loginUser(String userId, String password) throws Exception {
        User user = userDao.getValueByKey(userId).orElseThrow();
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new Exception();
        return SignInUser.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .level(user.getLevel())
                .token(jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getRoles()))
                .build();
    }

    public User changeUserInfo(String userId, String password, String name) {
        User user = User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .name(name)
                .timeStamp(System.currentTimeMillis() + "")
                .build();
        userDao.deleteValue(userId);
        userDao.insertValue(userId, user);
        return user;
    }

    public void deleteUser(String userId) {
        userDao.deleteValue(userId);
    }
}
