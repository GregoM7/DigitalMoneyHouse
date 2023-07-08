package com.digitalmoney.accountservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class AccountDestiny {
    private Integer accountDestinyId;
    private Timestamp date;
    private String CVU;
    private String CBU;
    private String lastName;
    private String Name;

    public Integer getAccountDestinyID() {
        return accountDestinyId;
    }

    public void setAccountDestinyID(Integer accountDestinyID) {
        this.accountDestinyId = accountDestinyID;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getCvu() {
        return CVU;
    }

    public void setCvu(String cvu) {
        this.CVU = cvu;
    }

    public String getCBU() {
        return CBU;
    }

    public void setCBU(String CBU) {
        this.CBU = CBU;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
