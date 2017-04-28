package com.lanzdev.model.entity;

import com.lanzdev.model.Identified;

public class Subscription implements Identified<Integer> {

    private Integer id;
    private Long chatId;
    private String wallDomain;
    private Long lastPostId;

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

    public Long getLastPostId( ) {
        return lastPostId;
    }

    public void setLastPostId(Long lastPostId) {
        this.lastPostId = lastPostId;
    }

    @Override
    public String toString( ) {
        return "Subscription{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", wallDomain='" + wallDomain + '\'' +
                ", lastPostId=" + lastPostId +
                '}';
    }
}
