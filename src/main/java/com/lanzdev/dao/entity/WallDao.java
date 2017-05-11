package com.lanzdev.dao.entity;

import com.lanzdev.dao.GenericDao;
import com.lanzdev.domain.Wall;

import java.util.List;

public interface WallDao extends GenericDao<Wall, Integer> {

    List<Wall> getAllApproved();
    Wall getByDomain(String domain);
}
