package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class CommonRequestData implements Serializable {
	
	List<SetOfflineRequest>SetDetails;

	public List<SetOfflineRequest> getSetDetails() {
		return SetDetails;
	}

	public void setSetDetails(List<SetOfflineRequest> setDetails) {
		SetDetails = setDetails;
	}
	
	
	
	
	

}
