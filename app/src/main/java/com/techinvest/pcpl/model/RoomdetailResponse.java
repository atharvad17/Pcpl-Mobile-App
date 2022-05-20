package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class RoomdetailResponse implements Serializable {
	
	String status;
	String Remarks;
	List<GetRoomdetailResponse>Values;
	List<GetCarpetAreaResponse>ValuesCarpetArea;
	
	List<GetCarpetAreaResponse>ValuesFlowerBed;
	List<RequestDropdownData>RoomType;
	List<RequestDropdownData>Flooring;
	List<RequestDropdownData>Doors;
	List<RequestDropdownData>WindowType;
	List<RequestDropdownData>WCType;
	List<RequestDropdownData>KitchenType;
	
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
	public List<GetRoomdetailResponse> getValues() {
		return Values;
	}
	public void setValues(List<GetRoomdetailResponse> values) {
		Values = values;
	}
	public List<GetCarpetAreaResponse> getValuesCarpetArea() {
		return ValuesCarpetArea;
	}
	public void setValuesCarpetArea(List<GetCarpetAreaResponse> valuesCarpetArea) {
		ValuesCarpetArea = valuesCarpetArea;
	}
	public List<GetCarpetAreaResponse> getValuesFlowerBed() {
		return ValuesFlowerBed;
	}
	public void setValuesFlowerBed(List<GetCarpetAreaResponse> valuesFlowerBed) {
		ValuesFlowerBed = valuesFlowerBed;
	}
	public List<RequestDropdownData> getRoomType() {
		return RoomType;
	}
	public void setRoomType(List<RequestDropdownData> roomType) {
		RoomType = roomType;
	}
	public List<RequestDropdownData> getFlooring() {
		return Flooring;
	}
	public void setFlooring(List<RequestDropdownData> flooring) {
		Flooring = flooring;
	}
	public List<RequestDropdownData> getDoors() {
		return Doors;
	}
	public void setDoors(List<RequestDropdownData> doors) {
		Doors = doors;
	}
	public List<RequestDropdownData> getWindowType() {
		return WindowType;
	}
	public void setWindowType(List<RequestDropdownData> windowType) {
		WindowType = windowType;
	}
	public List<RequestDropdownData> getWCType() {
		return WCType;
	}
	public void setWCType(List<RequestDropdownData> wCType) {
		WCType = wCType;
	}
	public List<RequestDropdownData> getKitchenType() {
		return KitchenType;
	}
	public void setKitchenType(List<RequestDropdownData> kitchenType) {
		KitchenType = kitchenType;
	}
	
	
  

}
