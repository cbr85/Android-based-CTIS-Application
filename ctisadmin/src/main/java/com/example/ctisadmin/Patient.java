package com.example.ctisadmin;

import java.io.Serializable;

public class Patient implements Serializable {
    String patientId;
    String userName;
    String password;
    String fullName;
    String patientType;
    String symptoms;
    String centreId;

    public Patient() {
    }

    public Patient(String patientId, String userName, String password, String fullName, String patientType, String symptoms, String centreId) {
        this.patientId = patientId;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.patientType = patientType;
        this.symptoms = symptoms;
        this.centreId = centreId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }
}
