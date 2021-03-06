package com.lanzdev.domain;

public class Chat implements Identified<Long> {

    private Long id;
    private String firstName;
    private String lastName;
    private Boolean suspended;
    private String lastCommand;
    private Boolean admin;

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

    public void suspend() {
        this.suspended = true;
    }

    public void proceed() {
        this.suspended = false;
    }

    public String getLastCommand( ) {
        return lastCommand;
    }

    public void setLastCommand(String lastCommand) {
        this.lastCommand = lastCommand;
    }

    public Boolean isAdmin( ) {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
