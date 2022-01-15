package com.example.server.service;

import com.example.server.dao.PregenUrlDao;
import com.example.server.dao.UrlDao;
import com.example.server.model.PregenUrl;
import com.example.server.model.Url;
import com.example.server.module.UrlEncoder;
import com.example.server.module.Base62Encoder;
import com.example.server.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UrlService {
    private final UrlDao urlDao;
    private final PregenUrlDao pregenDao;

    private final UrlEncoder urlEncoder;
    private Base62Encoder base62Encoder;
    private Base64.Encoder base64Encoder;
    private Base64.Decoder base64Decoder;
    private UrlValidator urlValidator;
    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(UrlEncoder urlEncoder, UrlRepository urlRepository) throws IOException {
        this.urlDao = UrlDao.make();
        this.pregenDao = PregenUrlDao.make();
        this.urlEncoder = urlEncoder;
        this.base62Encoder = new Base62Encoder();
        this.base64Encoder = Base64.getEncoder();
        this.base64Decoder = Base64.getDecoder();
        this.urlRepository = urlRepository;
        this.urlValidator = new UrlValidator();
    }

    public Url getShortUrl(String shortUrl, String requestUrl, String userId, String level, String expireTime, String ipAddress) {
        //TODO: change expireTime due to level
        if (level.equals("1")) {
            expireTime = "7";
        } else if (level.equals("2")) {
            expireTime = "14";
        } else if (level.equals("3")) {
            expireTime = "21";
        } else if (level.equals("4")) {
            expireTime = "28";
        }
        Url url = Url.builder()
                .shortUrl(shortUrl)
                .longUrl(requestUrl)
                .userId(userId)
                .expireTime(new Date(System.currentTimeMillis() + (long) Integer.parseInt(expireTime) * 24 * 60 * 60 * 1000).toInstant().toString())
                .ipAddress(ipAddress)
                .timeStamp(System.currentTimeMillis() + "")
                .build();
        urlRepository.save(url);
        urlDao.insertValue(url.getShortUrl(), url);
        return url;
    }

    public List<Url> getShortUrlBulk(String[] requestUrl, String userId, String level, String expireTime, String ipAddress) {
        //TODO: change expireTime due to level
        if (level.equals("1")) {
            expireTime = "7";
        } else if (level.equals("2")) {
            expireTime = "14";
        } else if (level.equals("3")) {
            expireTime = "21";
        } else if (level.equals("4")) {
            expireTime = "28";
        }

        List<Url> returnList = new ArrayList<>();
        Set<String> set = pregenDao.getAndDeleteKey(requestUrl.length);
        List<String> list = new ArrayList<>(set);
        for (int i = 0; i < requestUrl.length; i++) {
            String shortUrl = list.get(i);
            Url url = Url.builder()
                    .shortUrl(shortUrl)
                    .longUrl(requestUrl[i])
                    .userId(userId)
                    .expireTime(new Date(System.currentTimeMillis() + (long) Integer.parseInt(expireTime) * 24 * 60 * 60 * 1000).toInstant().toString())
                    .ipAddress(ipAddress)
                    .timeStamp(System.currentTimeMillis() + "")
                    .build();
//            urlRepository.save(url);
            urlDao.insertValue(url.getShortUrl(), url);
            returnList.add(url);
        }

        return returnList;
    }

    public String getShortUrl(String url) {
        if (urlValidator.isValid(url)) {
            Random random = new SecureRandom();
            String charArr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            StringBuilder res = new StringBuilder();
            byte[] bytes = new byte[8];
            random.nextBytes(bytes);
            for (int i = 0; i < 8; i++) {
                res.append(charArr.charAt((bytes[i] & 0xFF)));
            }
            return res.toString();
        } else {
            return "Not a valid long URL";
        }
    }

    public List<Url> findAllUrl() {
        return urlDao.readTable();
    }

    public List<Url> findUrlsById(String userId) {
        //TODO: change it to readValueById
        return urlDao.readTable(userId);
    }

    public Url changeUrlInfo(String shortUrl, String longUrl, String userId) {
        Url url = urlDao.getValueByKey(shortUrl);
        if (Objects.equals(userId, url.getUserId())) {
            url = Url.builder()
                    .shortUrl(url.getShortUrl())
                    .longUrl(longUrl)
                    .userId(userId)
                    .expireTime(url.getExpireTime())
                    .ipAddress(url.getIpAddress())
                    .timeStamp(url.getTimeStamp())
                    .build();
        }
        //TODO: delete previous one
        urlDao.insertValue(url.getShortUrl(), url);
        return url;
    }

    public Url getUrlByKey(String key) {
        return urlDao.getValueByKey(key);
    }

    public void deleteUrl(String shortUrl) {
        urlDao.deleteValue(shortUrl);
    }
}
