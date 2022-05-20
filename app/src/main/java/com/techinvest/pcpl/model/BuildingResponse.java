package com.techinvest.pcpl.model;

import java.util.List;

public class BuildingResponse {
	
	String status;
	String Remarks;
	List<GetBuildingDetailsResponse>Values;
	List<RequestDropdownData>CivicAmenities;
	List<RequestDropdownData>ECQ;
	List<RequestDropdownData>ICQ;
	List<RequestDropdownData>Floors;
	List<RequestDropdownData>InfrstructureQuality;
	List<RequestDropdownData>RentalValue;
	List<RequestDropdownData>StructureType;
	List<RequestDropdownData>WaterSupply;
	List<RequestDropdownData>BuildingMaintenance;
	
	
	public List<RequestDropdownData> getBuildingMaintenance() {
		return BuildingMaintenance;
	}
	public void setBuildingMaintenance(List<RequestDropdownData> buildingMaintenance) {
		BuildingMaintenance = buildingMaintenance;
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
	public List<GetBuildingDetailsResponse> getValues() {
		return Values;
	}
	public void setValues(List<GetBuildingDetailsResponse> values) {
		Values = values;
	}
	public List<RequestDropdownData> getCivicAmenities() {
		return CivicAmenities;
	}
	public void setCivicAmenities(List<RequestDropdownData> civicAmenities) {
		CivicAmenities = civicAmenities;
	}
	public List<RequestDropdownData> getECQ() {
		return ECQ;
	}
	public void setECQ(List<RequestDropdownData> eCQ) {
		ECQ = eCQ;
	}
	public List<RequestDropdownData> getICQ() {
		return ICQ;
	}
	public void setICQ(List<RequestDropdownData> iCQ) {
		ICQ = iCQ;
	}
	public List<RequestDropdownData> getFloors() {
		return Floors;
	}
	public void setFloors(List<RequestDropdownData> floors) {
		Floors = floors;
	}
	public List<RequestDropdownData> getInfrstructureQuality() {
		return InfrstructureQuality;
	}
	public void setInfrstructureQuality(
			List<RequestDropdownData> infrstructureQuality) {
		InfrstructureQuality = infrstructureQuality;
	}
	public List<RequestDropdownData> getRentalValue() {
		return RentalValue;
	}
	public void setRentalValue(List<RequestDropdownData> rentalValue) {
		RentalValue = rentalValue;
	}
	public List<RequestDropdownData> getStructureType() {
		return StructureType;
	}
	public void setStructureType(List<RequestDropdownData> structureType) {
		StructureType = structureType;
	}
	public List<RequestDropdownData> getWaterSupply() {
		return WaterSupply;
	}
	public void setWaterSupply(List<RequestDropdownData> waterSupply) {
		WaterSupply = waterSupply;
	}

}
