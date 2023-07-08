package com.digitalmedia.users.model;

import javax.persistence.Column;

public class Login {
    @Column(name = "com.dmh.email.service.email", nullable = false)
    String email;
    @Column(name = "password", nullable = false)
    String password;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
