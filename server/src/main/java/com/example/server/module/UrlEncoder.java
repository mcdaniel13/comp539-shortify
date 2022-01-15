package com.example.server.module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UrlEncoder {
    private String decoding(String str) {
        return "decoding";
    }

    private String encoding(String str) {
        return "encoding";
    }
    public String urlDecoder(String str) {
        return decoding(str);
    }

    public String urlEncoder(String str) {
        return encoding(str);
    }
}
