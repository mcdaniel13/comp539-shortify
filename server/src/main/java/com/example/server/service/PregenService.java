package com.example.server.service;

import com.example.server.dao.PregenUrlDao;
import com.example.server.dao.UrlDao;
import com.example.server.model.PregenUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PregenService {
    private final UrlDao mainDao = UrlDao.make();
    private final PregenUrlDao pregenDao = PregenUrlDao.make();
    Random random;

    @Autowired
    public PregenService() throws IOException {
        random = new SecureRandom();
    }

    public boolean checkUrl(String url) {
        return !mainDao.checkKey(url) && !pregenDao.checkKey(url);
    }

    public void insertPregen() {
        String shortUrl = generateShortUrl();
        if (checkUrl(shortUrl)) {
            PregenUrl urlModel = PregenUrl.builder()
                    .shortUrl(shortUrl)
                    .timeStamp(System.currentTimeMillis() + "")
                    .build();
            pregenDao.insertValue(urlModel);
        }
    }

    public void bulkInsertPregen(int start, int count) {
        List<PregenUrl> list = new ArrayList<>();
        for (int i = start; i < start + count; i++) {
            String shortUrl = generateShortUrl();
            if (checkUrl(shortUrl)) {
                PregenUrl urlModel = PregenUrl.builder()
                        .shortUrl(shortUrl)
                        .timeStamp(System.currentTimeMillis() + "")
                        .build();
                list.add(urlModel);
            }
        }
        pregenDao.bulkInsertValue(list);
    }

    private String generateShortUrl() {
        String charArr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(62);
            res.append(charArr.charAt(index));
        }
        return res.toString();
    }

    public PregenUrl getPregenUrl() {
        return pregenDao.getAndDeleteKey();
    }

    public void deletePregenKeyFromDB(String key) {
        pregenDao.deleteKey(key);
    }
}

