package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class ProjectTabResponse implements Serializable {
	
	String status;
	String Remarks;
	List<GetProjectDetailsResponse>Values;
	List<RequestDropdownData>BuilderName;
	List<RequestDropdownData>Architect;
	List<RequestDropdownData>PlanNo;
	List<RequestDropdownData>CCNo;
	List<RequestDropdownData>SocRegNo;
	List<RequestDropdownData>ShareNo;
	List<RequestDropdownData>SocName;
	List<RequestDropdownData>ShareDNo;
	List<RequestDropdownData>UnitDetails;
	List<RequestDropdownData>Plinth;
	List<RequestDropdownData>LiftInstalled;
	
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
	public List<GetProjectDetailsResponse> getValues() {
		return Values;
	}
	public void setValues(List<GetProjectDetailsResponse> values) {
		Values = values;
	}
	public List<RequestDropdownData> getBuilderName() {
		return BuilderName;
	}
	public void setBuilderName(List<RequestDropdownData> builderName) {
		BuilderName = builderName;
	}
	public List<RequestDropdownData> getArchitect() {
		return Architect;
	}
	public void setArchitect(List<RequestDropdownData> architect) {
		Architect = architect;
	}
	public List<RequestDropdownData> getPlanNo() {
		return PlanNo;
	}
	public void setPlanNo(List<RequestDropdownData> planNo) {
		PlanNo = planNo;
	}
	public List<RequestDropdownData> getCCNo() {
		return CCNo;
	}
	public void setCCNo(List<RequestDropdownData> cCNo) {
		CCNo = cCNo;
	}
	public List<RequestDropdownData> getSocRegNo() {
		return SocRegNo;
	}
	public void setSocRegNo(List<RequestDropdownData> socRegNo) {
		SocRegNo = socRegNo;
	}
	public List<RequestDropdownData> getShareNo() {
		return ShareNo;
	}
	public void setShareNo(List<RequestDropdownData> shareNo) {
		ShareNo = shareNo;
	}
	public List<RequestDropdownData> getSocName() {
		return SocName;
	}
	public void setSocName(List<RequestDropdownData> socName) {
		SocName = socName;
	}
	public List<RequestDropdownData> getShareDNo() {
		return ShareDNo;
	}
	public void setShareDNo(List<RequestDropdownData> shareDNo) {
		ShareDNo = shareDNo;
	}
	public List<RequestDropdownData> getUnitDetails() {
		return UnitDetails;
	}
	public void setUnitDetails(List<RequestDropdownData> unitDetails) {
		UnitDetails = unitDetails;
	}
	public List<RequestDropdownData> getPlinth() {
		return Plinth;
	}
	public void setPlinth(List<RequestDropdownData> plinth) {
		Plinth = plinth;
	}
	public List<RequestDropdownData> getLiftInstalled() {
		return LiftInstalled;
	}
	public void setLiftInstalled(List<RequestDropdownData> liftInstalled) {
		LiftInstalled = liftInstalled;
	}
	
	
	

}
