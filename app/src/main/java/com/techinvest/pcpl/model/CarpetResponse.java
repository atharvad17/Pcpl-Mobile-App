package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class CarpetResponse implements Serializable {
	
	String status;
	String Remarks;
	List<GetCarpetAreaResponse>ValuesCarpetArea;
	
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
	public List<GetCarpetAreaResponse> getValuesCarpetArea() {
		return ValuesCarpetArea;
	}
	public void setValuesCarpetArea(List<GetCarpetAreaResponse> valuesCarpetArea) {
		ValuesCarpetArea = valuesCarpetArea;
	}
	
	
	
	

}
