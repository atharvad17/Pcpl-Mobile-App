package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class PendingDataResponse implements Serializable {
	List<PendingVisit>jj;
	private static final long serialVersionUID = 1L;
     
	public List<PendingVisit> getJj() {
		return jj;
	}

	public void setJj(List<PendingVisit> jj) {
		this.jj = jj;
	}

}
