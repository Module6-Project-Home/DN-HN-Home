package com.example.md6projecthndn.service;

import java.util.Optional;

public interface IGenerateService<T> {
    Iterable<T> findAll();

    void save(T t) ;

    T findById(Long id);


    void delete(Long id);

}
