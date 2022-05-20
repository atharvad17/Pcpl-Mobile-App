package com.techinvest.pcpl.model;

public class GetLocationResponse {
	String RequestId;
	String LCTYPE;
	String CLASS;
	String SITENAME;
	String VIEWNAME;
	String North;
	String South;
	String West;
	String East;
	String NearRailWayStation;
	String DistancesRailway;
	String NearBusStop;
	String DistancesBus;
	String AutoAvailability;
	String Lattitude;
	String Longitude;
	String Hospital;
	String Distancehosp;
	
	
	
	public GetLocationResponse(String requestId, String lCTYPE, String cLASS,
			String sITENAME, String vIEWNAME, String north, String south,
			String west, String east, String nearRailWayStation,
			String distancesRailway, String nearBusStop, String distancesBus,
			String autoAvailability, String lattitude, String longitude,
			String hospital, String distancehosp) {
		//super();
		RequestId = requestId;
		LCTYPE = lCTYPE;
		CLASS = cLASS;
		SITENAME = sITENAME;
		VIEWNAME = vIEWNAME;
		North = north;
		South = south;
		West = west;
		East = east;
		NearRailWayStation = nearRailWayStation;
		DistancesRailway = distancesRailway;
		NearBusStop = nearBusStop;
		DistancesBus = distancesBus;
		AutoAvailability = autoAvailability;
		Lattitude = lattitude;
		Longitude = longitude;
		Hospital = hospital;
		Distancehosp = distancehosp;
	}
	public String getRequestId() {
		return RequestId;
	}
	public void setRequestId(String requestId) {
		RequestId = requestId;
	}
	
	public String getVIEWNAME() {
		return VIEWNAME;
	}
	public void setVIEWNAME(String vIEWNAME) {
		VIEWNAME = vIEWNAME;
	}
	public String getWest() {
		return West;
	}
	public void setWest(String west) {
		West = west;
	}
	
	
	
	public String getLCTYPE() {
		return LCTYPE;
	}
	public void setLCTYPE(String lCTYPE) {
		LCTYPE = lCTYPE;
	}
	public String getCLASS() {
		return CLASS;
	}
	public void setCLASS(String cLASS) {
		CLASS = cLASS;
	}
	public String getSITENAME() {
		return SITENAME;
	}
	public void setSITENAME(String sITENAME) {
		SITENAME = sITENAME;
	}
	public String getNorth() {
		return North;
	}
	public void setNorth(String north) {
		North = north;
	}
	public String getSouth() {
		return South;
	}
	public void setSouth(String south) {
		South = south;
	}
	public String getEast() {
		return East;
	}
	public void setEast(String east) {
		East = east;
	}
	public String getNearRailWayStation() {
		return NearRailWayStation;
	}
	public void setNearRailWayStation(String nearRailWayStation) {
		NearRailWayStation = nearRailWayStation;
	}
	public String getDistancesRailway() {
		return DistancesRailway;
	}
	public void setDistancesRailway(String distancesRailway) {
		DistancesRailway = distancesRailway;
	}
	public String getNearBusStop() {
		return NearBusStop;
	}
	public void setNearBusStop(String nearBusStop) {
		NearBusStop = nearBusStop;
	}
	public String getDistancesBus() {
		return DistancesBus;
	}
	public void setDistancesBus(String distancesBus) {
		DistancesBus = distancesBus;
	}
	public String getAutoAvailability() {
		return AutoAvailability;
	}
	public void setAutoAvailability(String autoAvailability) {
		AutoAvailability = autoAvailability;
	}
	public String getLattitude() {
		return Lattitude;
	}
	public void setLattitude(String lattitude) {
		Lattitude = lattitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getHospital() {
		return Hospital;
	}
	public void setHospital(String hospital) {
		Hospital = hospital;
	}
	public String getDistancehosp() {
		return Distancehosp;
	}
	public void setDistancehosp(String distancehosp) {
		Distancehosp = distancehosp;
	}
	
	

}
