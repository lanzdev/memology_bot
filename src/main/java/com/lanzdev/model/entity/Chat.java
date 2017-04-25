package com.lanzdev.model.entity;

public class Chat {

    private Long id;
    private String firstName;
    private String lastName;
    private Boolean isSuspended;

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

    public Boolean getSuspended( ) {
        return isSuspended;
    }

    public void setSuspended(Boolean suspended) {
        isSuspended = suspended;
    }
}
