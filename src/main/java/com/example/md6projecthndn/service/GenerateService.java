package com.example.md6projecthndn.service;

import java.util.List;

public interface GenerateService<T> {
    List<T> findAll();

    void save(T t);
}
