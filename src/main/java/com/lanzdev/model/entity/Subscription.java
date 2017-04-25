package com.lanzdev.model.entity;

import com.lanzdev.model.Identified;

public class Subscription implements Identified<Integer> {

    private Integer id;
    private Long chatId;
    private String wallDomain;

    public Subscription( ) {
    }

    public Integer getId( ) {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getChatId( ) {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getWallDomain( ) {
        return wallDomain;
    }

    public void setWallDomain(String wallDomain) {
        this.wallDomain = wallDomain;
    }
}
