package com.example.server.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class PregenUrl {
    private String shortUrl;
    private String timeStamp;
}
