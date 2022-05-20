package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class SetOfflineRequest implements Serializable {
	
	List<RequestDataDetail>RequestTab;
	List<GetBuildingDetailsResponse>BuildingTab;
	List<GetLocationResponse>LocationTab;
	List<GetFlatSpecificationDetailsResponse>FlatTab;
	List<GetProjectDetailsResponse>ProjectTab;
	List<GetRoomdetailResponse>RoomDetail;
	List<GetCarpetAreaResponse>RoomArea;
	List<GetCarpetAreaResponse>RoomAreaFB;
	
	
	public SetOfflineRequest(List<RequestDataDetail> requestTab,
			List<GetBuildingDetailsResponse> buildingTab,
			List<GetLocationResponse> locationTab,
			List<GetFlatSpecificationDetailsResponse> flatTab,
			List<GetProjectDetailsResponse> projectTab,
			List<GetRoomdetailResponse> roomDetail,
			List<GetCarpetAreaResponse> roomArea,
			List<GetCarpetAreaResponse> roomAreaFB) {
	
		RequestTab = requestTab;
		BuildingTab = buildingTab;
		LocationTab = locationTab;
		FlatTab = flatTab;
		ProjectTab = projectTab;
		RoomDetail = roomDetail;
		RoomArea = roomArea;
		RoomAreaFB = roomAreaFB;
	}

	
	
	public List<GetCarpetAreaResponse> getRoomAreaFB() {
		return RoomAreaFB;
	}
	public void setRoomAreaFB(List<GetCarpetAreaResponse> roomAreaFB) {
		RoomAreaFB = roomAreaFB;
	}
	public List<RequestDataDetail> getRequestTab() {
		return RequestTab;
	}
	public void setRequestTab(List<RequestDataDetail> requestTab) {
		RequestTab = requestTab;
	}
	public List<GetBuildingDetailsResponse> getBuildingTab() {
		return BuildingTab;
	}
	public void setBuildingTab(List<GetBuildingDetailsResponse> buildingTab) {
		BuildingTab = buildingTab;
	}
	public List<GetLocationResponse> getLocationTab() {
		return LocationTab;
	}
	public void setLocationTab(List<GetLocationResponse> locationTab) {
		LocationTab = locationTab;
	}
	public List<GetFlatSpecificationDetailsResponse> getFlatTab() {
		return FlatTab;
	}
	public void setFlatTab(List<GetFlatSpecificationDetailsResponse> flatTab) {
		FlatTab = flatTab;
	}
	public List<GetProjectDetailsResponse> getProjectTab() {
		return ProjectTab;
	}
	public void setProjectTab(List<GetProjectDetailsResponse> projectTab) {
		ProjectTab = projectTab;
	}
	public List<GetRoomdetailResponse> getRoomDetail() {
		return RoomDetail;
	}
	public void setRoomDetail(List<GetRoomdetailResponse> roomDetail) {
		RoomDetail = roomDetail;
	}
	public List<GetCarpetAreaResponse> getRoomArea() {
		return RoomArea;
	}
	public void setRoomArea(List<GetCarpetAreaResponse> roomArea) {
		RoomArea = roomArea;
	}

}
