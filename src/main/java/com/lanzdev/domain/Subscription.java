package com.lanzdev.domain;

import com.lanzdev.domain.Identified;

public class Subscription implements Identified<Integer> {

    private Integer id;
    private Long chatId;
    private String wallDomain;
    private Long lastPostId;
    private Boolean active;

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

    public Boolean isActive( ) {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString( ) {
        return "Subscription{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", wallDomain='" + wallDomain + '\'' +
                ", lastPostId=" + lastPostId +
                ", active=" + active +
                '}';
    }
}
