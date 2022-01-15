package com.example.server.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends Result {
    private T data;
}
