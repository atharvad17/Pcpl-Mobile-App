package com.techinvest.pcpl.model;

import java.io.Serializable;

public class RequestDataDetail implements Serializable {

 private String RequestId;
 private String EngineerName;
 private String BankName;
 private String CustomerName;
 private String NameOnBoard;
 private String OwnerName;
 private String ContactName;
 private String Address1;
 private String BuildingName;
 private String Address2;
 private String Address3;
 private String Location;
 private String Area;
 private String Pincode;
 private String PersonMet;
 private String PeopertyType;
 private String Occupied;
 private String Occupant;
 
 public RequestDataDetail(){
	 
 }

public RequestDataDetail(String requestId, String engineerName,
		String bankName, String customerName, String nameOnBoard,
		String ownerName, String contactName, String address1,
		String buildingName, String address2, String address3, String location,
		String area, String pincode, String personMet, String peopertyType,
		String occupied, String occupant) {
	super();
	RequestId = requestId;
	EngineerName = engineerName;
	BankName = bankName;
	CustomerName = customerName;
	NameOnBoard = nameOnBoard;
	OwnerName = ownerName;
	ContactName = contactName;
	Address1 = address1;
	BuildingName = buildingName;
	Address2 = address2;
	Address3 = address3;
	Location = location;
	Area = area;
	Pincode = pincode;
	PersonMet = personMet;
	PeopertyType = peopertyType;
	Occupied = occupied;
	Occupant = occupant;
}
public String getRequestId() {
	return RequestId;
}
public void setRequestId(String requestId) {
	RequestId = requestId;
}
public String getEngineerName() {
	return EngineerName;
}
public void setEngineerName(String engineerName) {
	EngineerName = engineerName;
}
public String getBankName() {
	return BankName;
}
public void setBankName(String bankName) {
	BankName = bankName;
}
public String getCustomerName() {
	return CustomerName;
}
public void setCustomerName(String customerName) {
	CustomerName = customerName;
}
public String getNameOnBoard() {
	return NameOnBoard;
}
public void setNameOnBoard(String nameOnBoard) {
	NameOnBoard = nameOnBoard;
}
public String getOwnerName() {
	return OwnerName;
}
public void setOwnerName(String ownerName) {
	OwnerName = ownerName;
}
public String getContactName() {
	return ContactName;
}
public void setContactName(String contactName) {
	ContactName = contactName;
}
public String getAddress1() {
	return Address1;
}
public void setAddress1(String address1) {
	Address1 = address1;
}
public String getBuildingName() {
	return BuildingName;
}
public void setBuildingName(String buildingName) {
	BuildingName = buildingName;
}
public String getAddress2() {
	return Address2;
}
public void setAddress2(String address2) {
	Address2 = address2;
}
public String getAddress3() {
	return Address3;
}
public void setAddress3(String address3) {
	Address3 = address3;
}
public String getLocation() {
	return Location;
}
public void setLocation(String location) {
	Location = location;
}
public String getArea() {
	return Area;
}
public void setArea(String area) {
	Area = area;
}
public String getPincode() {
	return Pincode;
}
public void setPincode(String pincode) {
	Pincode = pincode;
}
public String getPersonMet() {
	return PersonMet;
}
public void setPersonMet(String personMet) {
	PersonMet = personMet;
}
public String getPeopertyType() {
	return PeopertyType;
}
public void setPeopertyType(String peopertyType) {
	PeopertyType = peopertyType;
}
public String getOccupied() {
	return Occupied;
}
public void setOccupied(String occupied) {
	Occupied = occupied;
}
public String getOccupant() {
	return Occupant;
}
public void setOccupant(String occupant) {
	Occupant = occupant;
}
	
	
	
}
