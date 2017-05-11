package com.lanzdev.managers.mysql.impl;

import com.lanzdev.dao.entity.WallDao;
import com.lanzdev.dao.mysql.impl.MySqlWallDao;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.domain.Wall;

import java.util.List;

public class MySqlWallManager implements WallManager {

    private WallDao dao = new MySqlWallDao();

    @Override
    public void add(Wall object) {
        dao.create(object);
    }

    @Override
    public void update(Wall object) {
        dao.update(object);
    }

    @Override
    public Wall getById(Integer id) {
        return dao.get(id);
    }

    @Override
    public List<Wall> getAll( ) {
        return dao.getAll();
    }

    @Override
    public List<Wall> getAllApproved( ) {
        return dao.getAllApproved();
    }

    @Override
    public Wall getByDomain(String domain) {
        return dao.getByDomain(domain);
    }

    @Override
    public void delete(Wall object) {
        dao.delete(object);
    }
}
