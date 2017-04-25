package com.lanzdev.model.entity;

public class Wall {

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
