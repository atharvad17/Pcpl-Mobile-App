package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class RequestDetailsResponse implements Serializable {
	
	String status;
	String Remarks;
	List<RequestDataDetail>Values;
	List<RequestDropdownData>Options;
	
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
	public List<RequestDataDetail> getValues() {
		return Values;
	}
	public void setValues(List<RequestDataDetail> values) {
		Values = values;
	}
	public List<RequestDropdownData> getOptions() {
		return Options;
	}
	public void setOptions(List<RequestDropdownData> options) {
		Options = options;
	}
	
	
	

}
