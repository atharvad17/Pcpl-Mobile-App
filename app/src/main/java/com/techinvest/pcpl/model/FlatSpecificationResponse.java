package com.techinvest.pcpl.model;

import java.util.List;

public class FlatSpecificationResponse {
	
	String status;
	String Remarks;
	List<GetFlatSpecificationDetailsResponse>Values;
	List<RequestDropdownData>CompoundWall;
	List<RequestDropdownData>Electrification;
	List<RequestDropdownData>OpenSpacePavement;
	List<RequestDropdownData>PlumbingType;
	List<RequestDropdownData>RoofingType;
	
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
	public List<GetFlatSpecificationDetailsResponse> getValues() {
		return Values;
	}
	public void setValues(List<GetFlatSpecificationDetailsResponse> values) {
		Values = values;
	}
	public List<RequestDropdownData> getCompoundWall() {
		return CompoundWall;
	}
	public void setCompoundWall(List<RequestDropdownData> compoundWall) {
		CompoundWall = compoundWall;
	}
	public List<RequestDropdownData> getElectrification() {
		return Electrification;
	}
	public void setElectrification(List<RequestDropdownData> electrification) {
		Electrification = electrification;
	}
	public List<RequestDropdownData> getOpenSpacePavement() {
		return OpenSpacePavement;
	}
	public void setOpenSpacePavement(List<RequestDropdownData> openSpacePavement) {
		OpenSpacePavement = openSpacePavement;
	}
	public List<RequestDropdownData> getPlumbingType() {
		return PlumbingType;
	}
	public void setPlumbingType(List<RequestDropdownData> plumbingType) {
		PlumbingType = plumbingType;
	}
	public List<RequestDropdownData> getRoofingType() {
		return RoofingType;
	}
	public void setRoofingType(List<RequestDropdownData> roofingType) {
		RoofingType = roofingType;
	}
	
	
	
	

}
