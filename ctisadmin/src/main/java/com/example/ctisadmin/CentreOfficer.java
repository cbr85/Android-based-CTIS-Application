package com.example.ctisadmin;

import java.io.Serializable;

public class CentreOfficer implements Serializable {
    String centreOfficerId;
    String userName;
    String password;
    String fullName;
    String position;
    String centreId;

    public CentreOfficer() {
    }

    public CentreOfficer(String centreOfficerId, String userName, String password, String fullName, String position) {
        this.centreOfficerId = centreOfficerId;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.position = position;
    }

    public CentreOfficer(String centreOfficerId, String userName, String password, String fullName, String position, String centreId) {
        this.centreOfficerId = centreOfficerId;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.position = position;
        this.centreId = centreId;
    }

    public String getCentreOfficerId() {
        return centreOfficerId;
    }

    public void setCentreOfficerId(String centreOfficerId) {
        this.centreOfficerId = centreOfficerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }
}
