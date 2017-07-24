package com.example.model;

import java.util.Date;
import java.util.List;

public class User {

    private Long id;
    private String account;
    private String password;
    private Date birth;
    private List<Role> roles;

    public User(Long id, String account, String password) {
        this.id = id;
        this.account = account;
        this.password = password;
    }

    public User(Long id, String account, String password, Date birth) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.birth = birth;
    }

    public User(Long id, String account, String password, Date birth, List<Role> roles) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.birth = birth;
        this.roles = roles;
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

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
