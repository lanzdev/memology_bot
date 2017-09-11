package com.lanzdev.vk.group;

public class PublicItem {

    private Integer id;
    private Integer gid;
    private String name;
    private String screenName;

    public PublicItem( ) {
    }

    public Integer getId( ) {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGid( ) {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName( ) {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName( ) {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
