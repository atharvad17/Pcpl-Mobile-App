package com.techinvest.pcpl.model;

import java.io.Serializable;

public class PendingVisit implements Serializable{
	private String RequestID;
	private String ClientName;
	private String ApplicantName;
	private String AreaOrWard;
	private String BuildingName;
	private String BranchName;
	private String CaseNo;
	private String filedname;
	private String ApplicantPhoneNo;

	public PendingVisit() {
	}

	public String getRequestID() {
		return RequestID;
	}

	public void setRequestID(String requestID) {
		RequestID = requestID;
	}

	public String getClientName() {
		return ClientName;
	}

	public void setClientName(String clientName) {
		ClientName = clientName;
	}

	public String getApplicantName() {
		return ApplicantName;
	}

	public void setApplicantName(String applicantName) {
		ApplicantName = applicantName;
	}

	public String getAreaOrWard() {
		return AreaOrWard;
	}

	public void setAreaOrWard(String areaOrWard) {
		AreaOrWard = areaOrWard;
	}

	public String getBuildingName() {
		return BuildingName;
	}

	public void setBuildingName(String buildingName) {
		BuildingName = buildingName;
	}

	public String getBranchName() {
		return BranchName;
	}

	public void setBranchName(String branchName) {
		BranchName = branchName;
	}

	public String getCaseNo() {
		return CaseNo;
	}

	public void setCaseNo(String caseNo) {
		CaseNo = caseNo;
	}

	public String getFiledname() {
		return filedname;
	}

	public void setFiledname(String filedname) {
		this.filedname = filedname;
	}

	public String getApplicantPhoneNo() {
		return ApplicantPhoneNo;
	}

	public void setApplicantPhoneNo(String applicantPhoneNo) {
		ApplicantPhoneNo = applicantPhoneNo;
	}

}
