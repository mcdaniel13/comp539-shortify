package com.example.server.service;

import com.example.server.response.ListResult;
import com.example.server.response.Result;
import com.example.server.response.SingleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    @Autowired
    public ResponseService() {}

    public enum Response {
        SUCCESS(0, "SUCCESS"),
        FAIL(-1, "FAIL");

        int code;
        String msg;

        Response(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
    }

    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    public Result getSuccessResult() {
        Result result = new Result();
        setSuccessResult(result);
        return result;
    }

    public Result getFailResult(int code, String msg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    private void setSuccessResult(Result result) {
        result.setSuccess(true);
        result.setCode(Response.SUCCESS.getCode());
        result.setMsg(Response.SUCCESS.getMsg());
    }

}
