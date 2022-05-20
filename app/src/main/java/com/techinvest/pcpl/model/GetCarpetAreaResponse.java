package com.techinvest.pcpl.model;

import java.io.Serializable;

public class GetCarpetAreaResponse  implements Serializable{
	String RequestId;
	String RoomDetailsIDs;
	String Lengths;
	String breadths;
	String Area;
	String Names;
	String areaType;
	
	
	public GetCarpetAreaResponse(String requestId, String roomDetailsIDs,
			String lengths, String breadths, String area, String names, String areaType) {
		
		RequestId = requestId;
		RoomDetailsIDs = roomDetailsIDs;
		Lengths = lengths;
		this.breadths = breadths;
		Area = area;
		Names = names;
		this.areaType=areaType;
	}
	public String getNames() {
		return Names;
	}
	public void setNames(String names) {
		Names = names;
	}
	public String getRequestId() {
		return RequestId;
	}
	public void setRequestId(String requestId) {
		RequestId = requestId;
	}
	public String getRoomDetailsIDs() {
		return RoomDetailsIDs;
	}
	public void setRoomDetailsIDs(String roomDetailsIDs) {
		RoomDetailsIDs = roomDetailsIDs;
	}
	public String getLengths() {
		return Lengths;
	}
	public void setLengths(String lengths) {
		Lengths = lengths;
	}
	public String getBreadths() {
		return breadths;
	}
	public void setBreadths(String breadths) {
		this.breadths = breadths;
	}
	public String getArea() {
		return Area;
	}
	public void setArea(String area) {
		Area = area;
	}
	
	
   

}
