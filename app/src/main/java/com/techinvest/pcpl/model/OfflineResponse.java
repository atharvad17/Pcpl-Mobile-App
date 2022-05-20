package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class OfflineResponse implements Serializable {
	List<Offlinedatamodel>offlinedata;

	public List<Offlinedatamodel> getOfflinedata() {
		return offlinedata;
	}

	public void setOfflinedata(List<Offlinedatamodel> offlinedata) {
		this.offlinedata = offlinedata;
	}

}
