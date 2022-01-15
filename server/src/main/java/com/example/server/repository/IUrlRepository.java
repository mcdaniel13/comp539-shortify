package com.example.server.repository;

import com.example.server.model.Url;

import java.util.Map;

public interface IUrlRepository {
    void save(Url url);
    Map<String, Url> findAll();
    Url findById(String id);
    void update(Url url);
    void delete(String id);
}
