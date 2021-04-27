package com.example.ctis;

import java.io.Serializable;

public class CovidTest implements Serializable {
    String testID;
    String testDate;
    String result;
    String resultDate;
    String status;
    String patientID;
    String centerOfficeID;
    String testKitID;

    public CovidTest() {
    }

    public CovidTest(String testID, String testDate, String result, String resultDate, String status, String patientID, String centerOfficeID, String testKitID) {
        this.testID = testID;
        this.testDate = testDate;
        this.result = result;
        this.resultDate = resultDate;
        this.status = status;
        this.patientID = patientID;
        this.centerOfficeID = centerOfficeID;
        this.testKitID = testKitID;
    }

    public String getTestID() {
        return testID;
    }

    public String getTestDate() {
        return testDate;
    }

    public String getResult() {
        return result;
    }

    public String getResultDate() {
        return resultDate;
    }

    public String getStatus() {
        return status;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getCenterOfficeID() {
        return centerOfficeID;
    }

    public String getTestKitID() {
        return testKitID;
    }
}
