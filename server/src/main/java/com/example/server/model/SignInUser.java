package com.example.server.model;

import com.example.server.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInUser {
    private String userId;
    private String name;
    private String token;
    private String level;
}
