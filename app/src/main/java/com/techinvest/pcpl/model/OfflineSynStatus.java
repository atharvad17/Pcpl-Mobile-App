package com.techinvest.pcpl.model;

import java.io.Serializable;

public class OfflineSynStatus  implements Serializable{
	 String requestId;
	 String synOfflineStatus;
	 
	
	
	public OfflineSynStatus(String requestId, String synOfflineStatus) {
		super();
		this.requestId = requestId;
		this.synOfflineStatus = synOfflineStatus;
	}
	public OfflineSynStatus() {
		// TODO Auto-generated constructor stub
	}
	public String getSynOfflineStatus() {
		return synOfflineStatus;
	}
	public void setSynOfflineStatus(String synOfflineStatus) {
		this.synOfflineStatus = synOfflineStatus;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	

}
