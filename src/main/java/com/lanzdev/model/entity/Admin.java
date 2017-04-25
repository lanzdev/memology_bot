package com.lanzdev.model.entity;

import com.lanzdev.model.Identified;

public class Admin implements Identified<Integer> {

    private Integer id;
    private String login;
    private String password;

    public Admin( ) {
    }

    public Integer getId( ) {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin( ) {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword( ) {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
