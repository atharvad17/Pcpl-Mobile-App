package com.techinvest.pcpl.model;

import java.io.Serializable;

public class Offlinedatamodel implements Serializable {
	String requestid;
	String responsejson;
	
	public Offlinedatamodel(){
	}
	
	public Offlinedatamodel(String requestId2, String result) {
		this.requestid=requestId2;
		this.responsejson=result;
	}
	public String getRequestid() {
		return requestid;
	}
	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}
	public String getResponsejson() {
		return responsejson;
	}
	public void setResponsejson(String responsejson) {
		this.responsejson = responsejson;
	}

}
