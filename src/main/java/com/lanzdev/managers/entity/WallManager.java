package com.lanzdev.managers.entity;

import com.lanzdev.managers.Manager;
import com.lanzdev.model.entity.Wall;

import java.util.List;

public interface WallManager extends Manager<Wall, Integer> {

    List<Wall> getAllApproved();
}
