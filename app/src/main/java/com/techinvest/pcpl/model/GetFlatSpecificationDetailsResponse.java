package com.techinvest.pcpl.model;

import java.io.Serializable;

public class GetFlatSpecificationDetailsResponse implements Serializable {
	
	private String RequestId;
	private String RoofingType;
	private String PlumbingType;
	private String ElectrificationType;
	private String CompoundWall;
	private String NumberOfGates;
	private String SwimmingPool;
	private String Gym;
	private String garden;
	private String ClubHouse;
	private String CarPark;
	private String NoOfcarPark;
	private String CarParkValue;
	private String Remarks1;
	private String Remarks2;
	private String Remarks3;
	private String OpenSpacePavement;
	
	
	public GetFlatSpecificationDetailsResponse(String requestId,
			String roofingType, String plumbingType,
			String electrificationType, String compoundWall,
			String numberOfGates, String swimmingPool, String gym,
			String garden, String clubHouse, String carPark,
			String noOfcarPark, String carParkValue, String remarks1,
			String remarks2, String remarks3, String openSpacePavement) {
		//super();
		RequestId = requestId;
		RoofingType = roofingType;
		PlumbingType = plumbingType;
		ElectrificationType = electrificationType;
		CompoundWall = compoundWall;
		NumberOfGates = numberOfGates;
		SwimmingPool = swimmingPool;
		Gym = gym;
		this.garden = garden;
		ClubHouse = clubHouse;
		CarPark = carPark;
		NoOfcarPark = noOfcarPark;
		CarParkValue = carParkValue;
		Remarks1 = remarks1;
		Remarks2 = remarks2;
		Remarks3 = remarks3;
		OpenSpacePavement = openSpacePavement;
	}
	public String getOpenSpacePavement() {
		return OpenSpacePavement;
	}
	public void setOpenSpacePavement(String openSpacePavement) {
		OpenSpacePavement = openSpacePavement;
	}
	public String getRequestId() {
		return RequestId;
	}
	public void setRequestId(String requestId) {
		RequestId = requestId;
	}
	public String getRoofingType() {
		return RoofingType;
	}
	public void setRoofingType(String roofingType) {
		RoofingType = roofingType;
	}
	public String getPlumbingType() {
		return PlumbingType;
	}
	public void setPlumbingType(String plumbingType) {
		PlumbingType = plumbingType;
	}
	public String getElectrificationType() {
		return ElectrificationType;
	}
	public void setElectrificationType(String electrificationType) {
		ElectrificationType = electrificationType;
	}
	public String getCompoundWall() {
		return CompoundWall;
	}
	public void setCompoundWall(String compoundWall) {
		CompoundWall = compoundWall;
	}
	public String getNumberOfGates() {
		return NumberOfGates;
	}
	public void setNumberOfGates(String numberOfGates) {
		NumberOfGates = numberOfGates;
	}
	public String getSwimmingPool() {
		return SwimmingPool;
	}
	public void setSwimmingPool(String swimmingPool) {
		SwimmingPool = swimmingPool;
	}
	public String getGym() {
		return Gym;
	}
	public void setGym(String gym) {
		Gym = gym;
	}
	public String getGarden() {
		return garden;
	}
	public void setGarden(String garden) {
		this.garden = garden;
	}
	public String getClubHouse() {
		return ClubHouse;
	}
	public void setClubHouse(String clubHouse) {
		ClubHouse = clubHouse;
	}
	public String getCarPark() {
		return CarPark;
	}
	public void setCarPark(String carPark) {
		CarPark = carPark;
	}
	public String getNoOfcarPark() {
		return NoOfcarPark;
	}
	public void setNoOfcarPark(String noOfcarPark) {
		NoOfcarPark = noOfcarPark;
	}
	public String getCarParkValue() {
		return CarParkValue;
	}
	public void setCarParkValue(String carParkValue) {
		CarParkValue = carParkValue;
	}
	public String getRemarks1() {
		return Remarks1;
	}
	public void setRemarks1(String remarks1) {
		Remarks1 = remarks1;
	}
	public String getRemarks2() {
		return Remarks2;
	}
	public void setRemarks2(String remarks2) {
		Remarks2 = remarks2;
	}
	public String getRemarks3() {
		return Remarks3;
	}
	public void setRemarks3(String remarks3) {
		Remarks3 = remarks3;
	}
	
	
	
	
	
	
	

}
