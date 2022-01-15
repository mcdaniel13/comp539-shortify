package com.example.server.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    @ApiModelProperty(value = "check whether success/fail")
    private boolean success;

    @ApiModelProperty(value = "response code")
    private int code;

    @ApiModelProperty(value = "response message")
    private String msg;
}
