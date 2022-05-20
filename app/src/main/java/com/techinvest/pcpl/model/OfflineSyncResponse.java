package com.techinvest.pcpl.model;

import java.util.List;

public class OfflineSyncResponse {
	
	String status;
	String Remarks;
	List<RequestDetailsResponse>RequestData ;
	List<LocationDetailsResponse>LocationData;
	List<ProjectTabResponse>ProjectData;
	List<BuildingResponse>BuildingData;
	List<FlatSpecificationResponse>FlatData;
	List<RoomdetailResponse>RoomData;
	List<CarpetResponse>CarpetAreaTab;
	List<CarpetResponse>FlowerBedAreaTab;
	
	
	
	
	
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
	public List<RequestDetailsResponse> getRequestData() {
		return RequestData;
	}
	public void setRequestData(List<RequestDetailsResponse> requestData) {
		RequestData = requestData;
	}
	public List<LocationDetailsResponse> getLocationData() {
		return LocationData;
	}
	public void setLocationData(List<LocationDetailsResponse> locationData) {
		LocationData = locationData;
	}
	public List<ProjectTabResponse> getProjectData() {
		return ProjectData;
	}
	public void setProjectData(List<ProjectTabResponse> projectData) {
		ProjectData = projectData;
	}
	public List<BuildingResponse> getBuildingData() {
		return BuildingData;
	}
	public void setBuildingData(List<BuildingResponse> buildingData) {
		BuildingData = buildingData;
	}
	public List<FlatSpecificationResponse> getFlatData() {
		return FlatData;
	}
	public void setFlatData(List<FlatSpecificationResponse> flatData) {
		FlatData = flatData;
	}
	public List<RoomdetailResponse> getRoomData() {
		return RoomData;
	}
	public void setRoomData(List<RoomdetailResponse> roomData) {
		RoomData = roomData;
	}
	public List<CarpetResponse> getCarpetAreaTab() {
		return CarpetAreaTab;
	}
	public void setCarpetAreaTab(List<CarpetResponse> carpetAreaTab) {
		CarpetAreaTab = carpetAreaTab;
	}
	public List<CarpetResponse> getFlowerBedAreaTab() {
		return FlowerBedAreaTab;
	}
	public void setFlowerBedAreaTab(List<CarpetResponse> flowerBedAreaTab) {
		FlowerBedAreaTab = flowerBedAreaTab;
	}
	
	
	
	
	
	
	
	
	

}
