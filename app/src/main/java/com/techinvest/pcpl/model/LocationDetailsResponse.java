package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class LocationDetailsResponse implements Serializable {
	
	String status;
	String Remarks;
	List<GetLocationResponse>Values;
	List<RequestDropdownData>LocationClass;
	List<RequestDropdownData>PropertyClass;
	List<RequestDropdownData>SiteClass;
	List<RequestDropdownData>ViewType;
	List<RequestDropdownData>AutoAvailability;
	List<RequestDropdownData>BusStop;
	List<RequestDropdownData>RailwayStation;
	
	
	public List<RequestDropdownData> getRailwayStation() {
		return RailwayStation;
	}
	public void setRailwayStation(List<RequestDropdownData> railwayStation) {
		RailwayStation = railwayStation;
	}
	public List<RequestDropdownData> getAutoAvailability() {
		return AutoAvailability;
	}
	public void setAutoAvailability(List<RequestDropdownData> autoAvailability) {
		AutoAvailability = autoAvailability;
	}
	public List<RequestDropdownData> getBusStop() {
		return BusStop;
	}
	public void setBusStop(List<RequestDropdownData> busStop) {
		BusStop = busStop;
	}
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
	public List<GetLocationResponse> getValues() {
		return Values;
	}
	public void setValues(List<GetLocationResponse> values) {
		Values = values;
	}
	public List<RequestDropdownData> getLocationClass() {
		return LocationClass;
	}
	public void setLocationClass(List<RequestDropdownData> locationClass) {
		LocationClass = locationClass;
	}
	public List<RequestDropdownData> getPropertyClass() {
		return PropertyClass;
	}
	public void setPropertyClass(List<RequestDropdownData> propertyClass) {
		PropertyClass = propertyClass;
	}
	public List<RequestDropdownData> getSiteClass() {
		return SiteClass;
	}
	public void setSiteClass(List<RequestDropdownData> siteClass) {
		SiteClass = siteClass;
	}
	public List<RequestDropdownData> getViewType() {
		return ViewType;
	}
	public void setViewType(List<RequestDropdownData> viewType) {
		ViewType = viewType;
	}
	
	
	
	

}
