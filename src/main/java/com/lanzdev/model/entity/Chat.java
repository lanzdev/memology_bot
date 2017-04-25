package com.lanzdev.model.entity;

import com.lanzdev.model.Identified;

public class Chat implements Identified<Long> {

    private Long id;
    private String firstName;
    private String lastName;
    private Boolean suspended;

    public Chat( ) {
    }

    public Long getId( ) {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName( ) {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName( ) {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean isSuspended( ) {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }
}
