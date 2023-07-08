package com.digitalmedia.users.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class UserRequest {

    @NotEmpty(message = "Campo vacio")
    private String name;

    @NotEmpty(message = "Campo vacio")
    private String lastname;

    @NotEmpty(message = "Campo vacio")
    private Integer dni;

    @NotEmpty(message = "Campo vacio")
    private String email;

    @NotEmpty(message = "Campo vacio")
    private String phoneNumber;

    @NotEmpty(message = "Campo vacio")
    private String password;

    @NotEmpty(message = "Campo vacio")
    private String alias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
