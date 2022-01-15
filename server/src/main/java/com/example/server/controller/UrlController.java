package com.example.server.controller;

import com.example.server.model.PregenUrl;
import com.example.server.model.Url;
import com.example.server.repository.UrlRepository;
import com.example.server.response.ListResult;
import com.example.server.response.Result;
import com.example.server.response.SingleResult;
import com.example.server.service.PregenService;
import com.example.server.service.ResponseService;
import com.example.server.service.UrlService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@CrossOrigin
@EnableCaching
@RestController
public class UrlController {
    private final UrlService urlService;
    private final PregenService pregenService;
    private final ResponseService responseService;
    private final UrlRepository urlRepository;

    @Autowired
    public UrlController(UrlService urlService, PregenService pregenService, ResponseService responseService, UrlRepository urlRepository) {
        this.urlService = urlService;
        this.pregenService = pregenService;
        this.responseService = responseService;
        this.urlRepository = urlRepository;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "request short url", notes = "return short url from long url")
    @GetMapping(value = "/url/short")
    @ResponseBody
    public SingleResult<Url> getShortUrl(@ApiParam(value = "request_url", required = true) @RequestParam(defaultValue = "") String requestUrl,
                                         @ApiParam(value = "user_id", required = true) @RequestParam(defaultValue = "") String userId,
                                         @ApiParam(value = "level", required = true) @RequestParam(defaultValue = "") String level,
                                         @ApiParam(value = "expire_time", required = false) @RequestParam(defaultValue = "7") String expireTime,
                                         @ApiParam(value = "ip_address", required = false) @RequestParam(defaultValue = "") String ipAddress) {
        PregenUrl pregenUrl = pregenService.getPregenUrl();
        return responseService.getSingleResult(urlService.getShortUrl(pregenUrl.getShortUrl(), requestUrl, userId, level, expireTime, ipAddress));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "request short url bulk", notes = "return bulk short urls from long urls")
    @GetMapping(value = "/url/shortBulk")
    @ResponseBody
    public ListResult<Url> getShortUrlBulk(@ApiParam(value = "request_url", required = true) @RequestParam(defaultValue = "") String[] requestUrl,
                                         @ApiParam(value = "user_id", required = true) @RequestParam(defaultValue = "") String userId,
                                         @ApiParam(value = "level", required = true) @RequestParam(defaultValue = "") String level,
                                         @ApiParam(value = "expire_time", required = false) @RequestParam(defaultValue = "7") String expireTime,
                                         @ApiParam(value = "ip_address", required = false) @RequestParam(defaultValue = "") String ipAddress) {
        return responseService.getListResult(urlService.getShortUrlBulk(requestUrl, userId, level, expireTime, ipAddress));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "get all short urls", notes = "return all urls")
    @GetMapping(value = "/urls")
    @ResponseBody
    public ListResult<Url> getAllShortUrls() {
        return responseService.getListResult(urlService.findAllUrl());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "get url information", notes = "return urls created by certain user")
    @GetMapping(value = "/urls/{userId}")
    @ResponseBody
    public ListResult<Url> getShortUrlsById(@ApiParam(value = "user id", required = true) @PathVariable String userId) throws Exception {
        return responseService.getListResult(urlService.findUrlsById(userId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "change url information", notes = "return info changed url")
    @PutMapping(value = "/url")
    @ResponseBody
    public SingleResult<Url> changeUrlInfo(@ApiParam(value = "user id", required = true) @RequestParam String userId,
                                            @ApiParam(value = "short url", required = true) @RequestParam String shortUrl,
                                            @ApiParam(value = "long url", required = true) @RequestParam String longUrl) throws IOException {
        return responseService.getSingleResult(urlService.changeUrlInfo(shortUrl, longUrl, userId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "delete url", notes = "return to check whether success or not")
    @DeleteMapping(value = "/url/{shortUrl}")
    @ResponseBody
    public Result delete(@ApiParam(value = "short Url", required = true) @PathVariable String shortUrl) {
        //TODO: delete
        urlService.deleteUrl(shortUrl);
        return responseService.getSuccessResult();
    }


    @ApiOperation(value = "request long url", notes = "return long url from short url")
    @GetMapping(value = "/url/redirect/{key}")
    public void redirectLongUrl(HttpServletResponse response,
                                @ApiParam(value = "short key", required = true) @PathVariable String key) throws IOException {
        try {
            Url url = urlRepository.findById(key);
            response.sendRedirect(url.getLongUrl());
        } catch (Exception ex) {
            Url url = urlService.getUrlByKey(key);
            response.sendRedirect(url.getLongUrl());
        }
    }

    @ApiOperation(value = "create 100 more short key", notes = "create ten more short key")
    @GetMapping(value = "/url/createShortKey")
    public void createShortKey(HttpServletResponse response) throws IOException {
        PregenService ps = new PregenService();
        ps.bulkInsertPregen(0, 100);
    }
}
