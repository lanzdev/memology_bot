package com.lanzdev.vk.group;

public class GroupItem {

    private Integer id;
    private String name;
    private String screenName;

    public GroupItem( ) {
    }

    public Integer getId( ) {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
