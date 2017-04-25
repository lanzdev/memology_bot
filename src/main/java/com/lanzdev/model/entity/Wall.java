package com.lanzdev.model.entity;

import com.lanzdev.model.Identified;

public class Wall implements Identified<Integer> {

    private Integer id;
    private String wallDomain;
    private Long lastPostId;
    private Boolean approved;
    private Integer popularity;

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

    public Long getLastPostId( ) {
        return lastPostId;
    }

    public void setLastPostId(Long lastPostId) {
        this.lastPostId = lastPostId;
    }

    public Boolean isApproved( ) {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Integer getPopularity( ) {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }
}
