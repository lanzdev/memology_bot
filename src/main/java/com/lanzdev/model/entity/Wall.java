package com.lanzdev.model.entity;

import com.lanzdev.model.Identified;

public class Wall implements Identified<Integer> {

    private Integer id;
    private String wallDomain;

    public Wall( ) {
    }

    public Integer getId( ) {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWallDomain( ) {
        return wallDomain;
    }

    public void setWallDomain(String wallDomain) {
        this.wallDomain = wallDomain;
    }
}
