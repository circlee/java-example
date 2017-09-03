package com.example.model;

public class User {

    private Long id;
    private String account;
    private String password;

    @Override
    public String toString() {
        return "User{id=" + id + ", account=" + account + ", password=" + password + "}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
