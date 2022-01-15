package com.example.server.controller;

import com.example.server.dao.UserDao;
import com.example.server.model.User;
import com.example.server.response.ListResult;
import com.example.server.response.Result;
import com.example.server.response.SingleResult;
import com.example.server.service.ResponseService;
import com.example.server.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@EnableCaching
@RestController
public class UserController {
    private final UserService userService;
    private final ResponseService responseService;


    @Autowired
    public UserController(UserService userService, ResponseService responseService) {
        this.userService = userService;
        this.responseService = responseService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "find all users", notes = "return all users in the system")
    @GetMapping(value = "/users")
    @ResponseBody
    public ListResult<User> findAllUsers() {
        return responseService.getListResult(userService.findAllUsers()); // userJpaRepo.findAll()
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "get user information", notes = "return request user info")
    @GetMapping(value = "/user/{userId}")
    @ResponseBody
    public SingleResult<User> findUserById(@ApiParam(value = "user id", required = true) @PathVariable String userId) throws Exception {
        return responseService.getSingleResult(userService.findUserById(userId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "create user", notes = "return created user")
    @PostMapping(value = "/user")
    @ResponseBody
    public SingleResult<User> createUser(@ApiParam(value = "user id", required = true) @RequestParam(defaultValue = "") String userId,
                                         @ApiParam(value = "password", required = true) @RequestParam(defaultValue = "") String password,
                                         @ApiParam(value = "name", required = true) @RequestParam(defaultValue = "") String name,
                                         @ApiParam(value = "name", required = false) @RequestParam(defaultValue = "1") String level,
                                         @ApiParam(value = "role", required = false) @RequestParam(defaultValue = "ROLE_USER") String role) throws Exception {
        return responseService.getSingleResult(userService.createUser(userId, password, name, level, role));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "change user information", notes = "return info changed user")
    @PutMapping(value = "/user")
    @ResponseBody
    public SingleResult<User> changeUserInfo(@ApiParam(value = "user id", required = true) @RequestParam String userId,
                                             @ApiParam(value = "user password", required = true) @RequestParam String password,
                                             @ApiParam(value = "user name", required = true) @RequestParam String name,
                                             @ApiParam(value = "user role", required = false) @RequestParam String role) {
        return responseService.getSingleResult(userService.changeUserInfo(userId, password, name));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "delete user", notes = "return to check whether success or not")
    @DeleteMapping(value = "/user/{userId}")
    @ResponseBody
    public Result deleteUser(@ApiParam(value = "user number", required = true) @PathVariable String userId) {
        userService.deleteUser(userId);
        return responseService.getSuccessResult();
    }

}
