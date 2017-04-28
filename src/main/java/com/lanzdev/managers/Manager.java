package com.lanzdev.managers;

import java.util.List;

public interface Manager<T, PK> {

    void add(T object);

    void update(T object);

    T getById(PK id);

    List<T> getAll();

    void delete(T object);
}
