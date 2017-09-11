package com.lanzdev.domain;

public class Wall implements Identified<Integer> {

    private Integer id;
    private String wallDomain;
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

    @Override
    public String toString( ) {
        return "Wall{" +
                "id=" + id +
                ", wallDomain='" + wallDomain + '\'' +
                ", approved=" + approved +
                ", popularity=" + popularity +
                '}';
    }
}
