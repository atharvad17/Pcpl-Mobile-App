package com.techinvest.pcpl.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class login
{
	String UserID;
	String email;
	
	public login()
	{
		// TODO Auto-generated constructor stub
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	 
	 
	 
}
