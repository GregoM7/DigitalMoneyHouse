package com.digitalmedia.users.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PasswordRequest {
    private String password;
    private String passwordRepet;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepet() {
        return passwordRepet;
    }

    public void setPasswordRepet(String passwordRepet) {
        this.passwordRepet = passwordRepet;
    }
}
