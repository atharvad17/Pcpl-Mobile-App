
package com.techinvest.pcpl.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Jj implements Serializable {

    @SerializedName("RequestID")
    @Expose
    private String requestID;
    @SerializedName("DocumentID")
    @Expose
    private String documentID;
    @SerializedName("ClientName")
    @Expose
    private String clientName;
    @SerializedName("AreaOrWard")
    @Expose
    private String areaOrWard;
    @SerializedName("CREATED_DATE")
    @Expose
    private String cREATEDDATE;
    @SerializedName("BranchName")
    @Expose
    private String branchName;
    @SerializedName("ClientreferenceNo")
    @Expose
    private String clientreferenceNo;

    @SerializedName("ApplicantName")
    @Expose
    private String applicantName;

    @SerializedName("Column1")
    @Expose
    private String Column1;



    public Jj(String areaOrWard, String applicantName, String branchName, String clientName, String Column1)
    {
        this.areaOrWard=areaOrWard;
        this.applicantName=applicantName;
        this.branchName=branchName;
        this.clientName=clientName;
        this.Column1=Column1;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAreaOrWard() {
        return areaOrWard;
    }

    public void setAreaOrWard(String areaOrWard) {
        this.areaOrWard = areaOrWard;
    }

    public String getCREATEDDATE() {
        return cREATEDDATE;
    }

    public void setCREATEDDATE(String cREATEDDATE) {
        this.cREATEDDATE = cREATEDDATE;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getClientreferenceNo() {
        return clientreferenceNo;
    }

    public void setClientreferenceNo(String clientreferenceNo) {
        this.clientreferenceNo = clientreferenceNo;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getColumn1() {return Column1;}

    public void setColumn1(String column1) {Column1 = column1;}
}
