package com.lanzdev.managers.entity;

import com.lanzdev.managers.Manager;
import com.lanzdev.domain.Wall;

import java.util.List;

public interface WallManager extends Manager<Wall, Integer> {

    List<Wall> getAllApproved();

    Wall getByDomain(String domain);
}
