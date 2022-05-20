package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class DisplayImagesResponse  implements Serializable{
	String status;
	String Remarks;
	List<ImageDetailResponse>ValuesForImages;
	
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
	public List<ImageDetailResponse> getValuesForImages() {
		return ValuesForImages;
	}
	public void setValuesForImages(List<ImageDetailResponse> valuesForImages) {
		ValuesForImages = valuesForImages;
	}
	
	

}
