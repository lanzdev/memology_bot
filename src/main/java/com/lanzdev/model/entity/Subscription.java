package com.lanzdev.model.entity;

public class Subscription {

    private Integer id;
    private Integer chatId;
    private String wallDomain;

    public Subscription( ) {
    }

    public Integer getId( ) {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChatId( ) {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getWallDomain( ) {
        return wallDomain;
    }

    public void setWallDomain(String wallDomain) {
        this.wallDomain = wallDomain;
    }
}
