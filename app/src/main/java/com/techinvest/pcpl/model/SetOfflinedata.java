package com.techinvest.pcpl.model;

import java.io.Serializable;

public class SetOfflinedata implements Serializable {
	String requestid;
	String saveuserdata;
	int uploadStatus;
	
	public String getRequestid() {
		return requestid;
	}
	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}
	public String getSaveuserdata() {
		return saveuserdata;
	}
	public void setSaveuserdata(String saveuserdata) {
		this.saveuserdata = saveuserdata;
	}

	public int getUploadStatus() { return uploadStatus; }
	public void setUploadStatus(int uploadStatus) { this.uploadStatus = uploadStatus; }


}
