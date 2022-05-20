package com.techinvest.pcpl.model;

import java.io.Serializable;

public class GetRoomdetailResponse implements Serializable{
	private String RequestId;
	private String RoomType;
	private String FlooringType;
	private String DoorType;
	private String WindowType;
	private String KitchenPlatformType;
	private String WCType;
	private String RoomDetailsID;
	
	
	public GetRoomdetailResponse(String requestId, String roomType,
			String flooringType, String doorType, String windowType,
			String kitchenPlatformType, String wCType, String roomDetailsID) {
		//super();
		RequestId = requestId;
		RoomType = roomType;
		FlooringType = flooringType;
		DoorType = doorType;
		WindowType = windowType;
		KitchenPlatformType = kitchenPlatformType;
		WCType = wCType;
		RoomDetailsID = roomDetailsID;
	}
	public String getRoomDetailsID() {
		return RoomDetailsID;
	}
	public void setRoomDetailsID(String roomDetailsID) {
		RoomDetailsID = roomDetailsID;
	}
	public String getRequestId() {
		return RequestId;
	}
	public void setRequestId(String requestId) {
		RequestId = requestId;
	}
	public String getRoomType() {
		return RoomType;
	}
	public void setRoomType(String roomType) {
		RoomType = roomType;
	}
	public String getFlooringType() {
		return FlooringType;
	}
	public void setFlooringType(String flooringType) {
		FlooringType = flooringType;
	}
	public String getDoorType() {
		return DoorType;
	}
	public void setDoorType(String doorType) {
		DoorType = doorType;
	}
	public String getWindowType() {
		return WindowType;
	}
	public void setWindowType(String windowType) {
		WindowType = windowType;
	}
	public String getKitchenPlatformType() {
		return KitchenPlatformType;
	}
	public void setKitchenPlatformType(String kitchenPlatformType) {
		KitchenPlatformType = kitchenPlatformType;
	}
	public String getWCType() {
		return WCType;
	}
	public void setWCType(String wCType) {
		WCType = wCType;
	}
	

}
