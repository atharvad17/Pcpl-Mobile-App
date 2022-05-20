package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class LocationDataResponse implements Serializable {
    String status;
    String Remarks;
    List<Location1> Value;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public List<Location1> getValue() {
        return Value;
    }

    public void setValue(List<Location1> value) {
        Value = value;
    }
}
