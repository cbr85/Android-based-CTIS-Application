package com.example.ctisadmin;

import java.io.Serializable;

public class TestCentre implements Serializable {
    String centreId;
    String centreName;

    public TestCentre() {
    }

    public TestCentre(String centreId, String centreName) {
        this.centreId = centreId;
        this.centreName = centreName;
    }

    public String getCentreId() {
        return centreId;
    }

    public void setCentreId(String centreId) {
        this.centreId = centreId;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }
}
