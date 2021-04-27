package com.example.ctisadmin;

import java.io.Serializable;

public class TestKit implements Serializable {

    String kidID;
    String testName;
    int availableStock;
    String centreId;

    public TestKit() {
    }

    public TestKit(String kidID, String testName, int availableStock, String centreId) {
        this.kidID = kidID;
        this.testName = testName;
        this.availableStock = availableStock;
        this.centreId = centreId;
    }

    public String getKidID() {
        return kidID;
    }

    public String getTestName() {
        return testName;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public String getCentreId() {
        return centreId;
    }
}
