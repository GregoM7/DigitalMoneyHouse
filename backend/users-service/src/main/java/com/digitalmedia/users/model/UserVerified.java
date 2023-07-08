package com.digitalmedia.users.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "verifiedusers")
public class UserVerified {

    @Id
    @Column(name = "id_user",nullable = false)
    private Long idUser;

    @Column(name = "f_verification")
    private String fecha;

    @Column(name = "code",nullable = false)
    private String code;

    public UserVerified(Long idUser, String fecha, String code) {
        this.idUser = idUser;
        this.fecha = fecha;
        this.code = code;
    }

    public UserVerified() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
