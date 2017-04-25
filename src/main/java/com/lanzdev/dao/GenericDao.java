package com.lanzdev.dao;

import java.util.List;

public interface GenericDao<T, PK> {

    T create(T object);

    T get(PK key);

    List<T> getAll();

    void update(T object);

    void delete(T object);
}
