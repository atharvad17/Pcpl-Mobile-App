package com.techinvest.pcpl.model;

import java.io.Serializable;

public class RequestDropdownData implements Serializable {
	private String DataType;
	private String DataValue;
	private String Datavalue;
	
	
	
	
	public String getDatavalue() {
		return Datavalue;
	}
	public void setDatavalue(String datavalue) {
		Datavalue = datavalue;
	}
	public String getDataType() {
		return DataType;
	}
	public void setDataType(String dataType) {
		DataType = dataType;
	}
	public String getDataValue() {
		return DataValue;
	}
	public void setDataValue(String dataValue) {
		DataValue = dataValue;
	}
	

}
