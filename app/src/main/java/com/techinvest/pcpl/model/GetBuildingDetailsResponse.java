package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;

public class GetBuildingDetailsResponse implements Serializable {
	
	 private String RequestId;
	 private String StructureType;
	 private String Floors;
	 private String NoOfFlatsFloor;
	 private String NoOfLift;
	 private String NoOfWings;
	 private String InternalConstructionQuality;
	 private String ExternalConstructionQuality;
	 private String BuildingMaintenance;
	 private String QualityOfInfrastruncture;
	 private String WaterSupply;
	 private String CivicAmenities;
	 private String RentalValue;
	 
	 
	public GetBuildingDetailsResponse(String requestId, String structureType,
			String floors, String noOfFlatsFloor, String noOfLift,
			String noOfWings, String internalConstructionQuality,
			String externalConstructionQuality, String buildingMaintenance,
			String qualityOfInfrastruncture, String waterSupply,
			String civicAmenities, String rentalValue) {
		//super();
		RequestId = requestId;
		StructureType = structureType;
		Floors = floors;
		NoOfFlatsFloor = noOfFlatsFloor;
		NoOfLift = noOfLift;
		NoOfWings = noOfWings;
		InternalConstructionQuality = internalConstructionQuality;
		ExternalConstructionQuality = externalConstructionQuality;
		BuildingMaintenance = buildingMaintenance;
		QualityOfInfrastruncture = qualityOfInfrastruncture;
		WaterSupply = waterSupply;
		CivicAmenities = civicAmenities;
		RentalValue = rentalValue;
	}
	public String getRequestId() {
		return RequestId;
	}
	public void setRequestId(String requestId) {
		RequestId = requestId;
	}
	public String getStructureType() {
		return StructureType;
	}
	public void setStructureType(String structureType) {
		StructureType = structureType;
	}
	public String getFloors() {
		return Floors;
	}
	public void setFloors(String floors) {
		Floors = floors;
	}
	public String getNoOfFlatsFloor() {
		return NoOfFlatsFloor;
	}
	public void setNoOfFlatsFloor(String noOfFlatsFloor) {
		NoOfFlatsFloor = noOfFlatsFloor;
	}
	public String getNoOfLift() {
		return NoOfLift;
	}
	public void setNoOfLift(String noOfLift) {
		NoOfLift = noOfLift;
	}
	public String getNoOfWings() {
		return NoOfWings;
	}
	public void setNoOfWings(String noOfWings) {
		NoOfWings = noOfWings;
	}
	public String getInternalConstructionQuality() {
		return InternalConstructionQuality;
	}
	public void setInternalConstructionQuality(String internalConstructionQuality) {
		InternalConstructionQuality = internalConstructionQuality;
	}
	public String getExternalConstructionQuality() {
		return ExternalConstructionQuality;
	}
	public void setExternalConstructionQuality(String externalConstructionQuality) {
		ExternalConstructionQuality = externalConstructionQuality;
	}
	public String getBuildingMaintenance() {
		return BuildingMaintenance;
	}
	public void setBuildingMaintenance(String buildingMaintenance) {
		BuildingMaintenance = buildingMaintenance;
	}
	public String getQualityOfInfrastruncture() {
		return QualityOfInfrastruncture;
	}
	public void setQualityOfInfrastruncture(String qualityOfInfrastruncture) {
		QualityOfInfrastruncture = qualityOfInfrastruncture;
	}
	public String getWaterSupply() {
		return WaterSupply;
	}
	public void setWaterSupply(String waterSupply) {
		WaterSupply = waterSupply;
	}
	public String getCivicAmenities() {
		return CivicAmenities;
	}
	public void setCivicAmenities(String civicAmenities) {
		CivicAmenities = civicAmenities;
	}
	public String getRentalValue() {
		return RentalValue;
	}
	public void setRentalValue(String rentalValue) {
		RentalValue = rentalValue;
	}
	
	 
	 
	 
	
	
	
	
	
	

}
