package com.example.server.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class Url implements Serializable {
    //key
    private String shortUrl;
    private String longUrl;
    private String userId;
    private String expireTime;
    private String ipAddress;
    private String timeStamp;
}
