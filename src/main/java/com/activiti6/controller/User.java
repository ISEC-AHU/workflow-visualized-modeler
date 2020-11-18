package com.activiti6.controller;

public class User {

    private String username;

    private String password;

    private String email;

    private String organization;

    private String subscribe;

    private String xmlfiles;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getOrganization() {
        return organization;
    }

    public String getXmlfiles() {
        return xmlfiles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public void setXmlfiles(String xmlfiles) {
        this.xmlfiles = xmlfiles;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", organization='" + organization + '\'' +
                ", subscribe='" + subscribe + '\'' +
                ", xmlfiles='" + xmlfiles + '\'' +
                '}';
    }
}
