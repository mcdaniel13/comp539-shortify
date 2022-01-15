package com.example.server.controller;

import com.example.server.model.SignInUser;
import com.example.server.response.Result;
import com.example.server.response.SingleResult;
import com.example.server.service.ResponseService;
import com.example.server.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@CrossOrigin
@RestController
public class SignController {
    private final ResponseService responseService;
    private final UserService userService;

    @Autowired
    public SignController(ResponseService responseService, UserService userService) throws IOException {
        this.responseService = responseService;
        this.userService = userService;
    }

    @ApiOperation(value = "sign in", notes = "sign in with user id")
    @PostMapping(value = "/signin")
    @ResponseBody
    public SingleResult<SignInUser> signInUser(@ApiParam(value = "user id", required = true) @RequestParam String userId,
                                             @ApiParam(value = "password", required = true) @RequestParam String password) throws Exception {
        return responseService.getSingleResult(userService.loginUser(userId, password));
    }

    @ApiOperation(value = "sign up", notes = "sign up new user")
    @PostMapping(value = "/signup")
    @ResponseBody
    public Result signUpUser(@ApiParam(value = "user id", required = true) @RequestParam String userId,
                         @ApiParam(value = "password", required = true) @RequestParam String password,
                         @ApiParam(value = "name", required = true) @RequestParam String name) throws Exception {
        return responseService.getSingleResult(userService.createUser(userId, password, name, "1", "ROLE_USER"));
    }
}
