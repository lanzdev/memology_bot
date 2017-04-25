package com.lanzdev.managers.mysql.implementation;

import com.lanzdev.dao.entity.WallDao;
import com.lanzdev.dao.mysql.implementation.MySqlWallDao;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.model.entity.Wall;

import java.util.List;

public class MySqlWallManager implements WallManager {

    private WallDao dao = new MySqlWallDao();

    @Override
    public Wall add(Wall object) {
        return dao.create(object);
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
}
