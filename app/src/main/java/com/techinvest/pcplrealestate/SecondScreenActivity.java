package com.techinvest.pcplrealestate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
//import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.techinvest.pcpl.adapter.LocationDetailAdapter;
import com.techinvest.pcpl.adapter.ViewPagerAdpater;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppDetails;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.BuildingResponse;
import com.techinvest.pcpl.model.CarpetResponse;
import com.techinvest.pcpl.model.CommonRequestData;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.FlatSpecificationResponse;
import com.techinvest.pcpl.model.GetBuildingDetailsResponse;
import com.techinvest.pcpl.model.GetCarpetAreaResponse;
import com.techinvest.pcpl.model.GetFlatSpecificationDetailsResponse;
import com.techinvest.pcpl.model.GetLocationResponse;
import com.techinvest.pcpl.model.GetProjectDetailsResponse;
import com.techinvest.pcpl.model.GetRoomdetailResponse;
import com.techinvest.pcpl.model.LocationDetailsResponse;
import com.techinvest.pcpl.model.OfflineSyncResponse;
import com.techinvest.pcpl.model.Offlinedatamodel;
import com.techinvest.pcpl.model.ProjectTabResponse;
import com.techinvest.pcpl.model.RequestDataDetail;
import com.techinvest.pcpl.model.RequestDetailsResponse;
import com.techinvest.pcpl.model.RequestDropdownData;
import com.techinvest.pcpl.model.RoomdetailResponse;
import com.techinvest.pcpl.model.SetOfflineRequest;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.parserhelper.DataBaseHelper;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcpl.tabs.SlidingTabLayout;
import com.techinvest.pcplrealestate.Requestdetails.SetRequestDetailAsyncTask;

@SuppressLint("NewApi")
public class SecondScreenActivity extends FragmentActivity implements AppConstants {
	// Toolbar toolbar;
	PcplApplication obj = new PcplApplication();
	private String SERVER_URL = AppSetting.getapiURL();
	ViewPager pager;
	com.techinvest.pcpl.adapter.ViewPagerAdpater adapter;
	SlidingTabLayout tabs;
	//CharSequence Titles[] = { "Pending Visit", "Completed Visit" };
	CharSequence Titles[] = {"ALL Visits"};
	int Numboftabs = 1;
	Activity activity;
	List<String> menulistdata;
	ListPopupWindow lstMenu;
	public static DataBaseHelper mypcplData;
	SetOfflinedata setofflineid = new SetOfflinedata();
	String ReqID = setofflineid.getRequestid();

	SharedPreferences mPrefs;
	//Button btnupdate;
	// CommonRequestData offline;
	private ProgressDialog loadingDialog;
	// List<Offlinedatamodel> offlineData;
	public static List<SetOfflinedata> requestDataList;
	public static List<SetOfflinedata> projectDataList;
	public static List<SetOfflinedata> locationDataList;
	public static List<SetOfflinedata> buildingDataList;
	public static List<SetOfflinedata> flatDataList;
	public static List<SetOfflinedata> roomDataList;
	public static List<SetOfflinedata> carpetDataList;
	public static List<SetOfflinedata> flowerbedDataList;

	public static List<SetOfflineRequest> SetDetails;
	public static List<RequestDataDetail> requestTab;
	public static List<GetBuildingDetailsResponse> buildingTab;
	public static List<GetLocationResponse> locationTab;
	public static List<GetFlatSpecificationDetailsResponse> flatTab;
	public static List<GetProjectDetailsResponse> projectTab;
	public static List<GetRoomdetailResponse> roomDetail;
	public static List<GetCarpetAreaResponse> roomArea;
	public static List<GetCarpetAreaResponse> roomAreaFB;

	public static double current_Lattitude = 0.0, current_Longitude = 0.0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_screen);
		activity = SecondScreenActivity.this;

		mPrefs = getPreferences(MODE_PRIVATE);
		requestDataList = new ArrayList<SetOfflinedata>();
		locationDataList = new ArrayList<SetOfflinedata>();
		projectDataList = new ArrayList<SetOfflinedata>();
		buildingDataList = new ArrayList<SetOfflinedata>();
		flatDataList = new ArrayList<SetOfflinedata>();
		roomDataList = new ArrayList<SetOfflinedata>();
		carpetDataList = new ArrayList<SetOfflinedata>();
		flowerbedDataList = new ArrayList<SetOfflinedata>();
		SetDetails = new ArrayList<SetOfflineRequest>();

		// added for testing
		requestTab = new ArrayList<RequestDataDetail>();
		buildingTab = new ArrayList<GetBuildingDetailsResponse>();
		locationTab = new ArrayList<GetLocationResponse>();
		flatTab = new ArrayList<GetFlatSpecificationDetailsResponse>();
		projectTab = new ArrayList<GetProjectDetailsResponse>();
		roomDetail = new ArrayList<GetRoomdetailResponse>();
		roomArea = new ArrayList<GetCarpetAreaResponse>();
		roomAreaFB = new ArrayList<GetCarpetAreaResponse>();

		lstMenu = new ListPopupWindow(SecondScreenActivity.this);
		menulistdata = new ArrayList<String>();
		//menulistdata.add("Update Online");
		//menulistdata.add("Offline Sync");
		menulistdata.add("Clear Offline");
		menulistdata.add("Assign Case");
		menulistdata.add("Logout");


		prepareActivity();
		getPermissions();
		try {
			getlocation();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Allow All Permissions\nTo Work App Properly", Toast.LENGTH_LONG).show();
		}


		mypcplData = new DataBaseHelper(activity);
		try {
			mypcplData.createDataBase();
			mypcplData.openDataBase();

			mypcplData.close();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {
			mypcplData.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}

		AppSetting.setKeyCheckedValue("Developed");
		lstMenu.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v,int position, long id)
			{
				// TODO Auto-generated method stub
				// if(parent.getItemAtPosition(position).toString()!=null)
				String selecteditem = parent.getItemAtPosition(position).toString();

				if (selecteditem.equals("Assign Case"))///SGK
				{
					Intent i = new Intent(SecondScreenActivity.this,visitsActivity.class);
					startActivity(i);
				}
				else if (selecteditem.equals("Update Online"))
				{

					updateOnlineData(AppSetting.getRequestId());
					Gson gson = new Gson();

					if (GetWebServiceData
							.isNetworkAvailable(SecondScreenActivity.this)) {

						if (SetDetails != null && SetDetails.size() > 0)
							new SetOfflineDataAsyncTask().execute();

					} else {
						AppCommonDialog.showSimpleDialog(
								SecondScreenActivity.this, getResources()
										.getString(R.string.app_name),
								getResources()
										.getString(R.string.check_network),
								getResources().getString(R.string.ok), "OK");
					}
				} else if (selecteditem.equals("Clear Offline")) {

			/*		ClearUserDialog(SecondScreenActivity.this, getResources()
									.getString(R.string.app_name), getResources()
									.getString(R.string.settings_logout_descrp),
							getResources().getString(R.string.ok),
							getResources().getString(R.string.no), "OKCANCEL");    */

					mypcplData.DeleteAllRecord();

					Toast.makeText(activity,
							"sucessfully cleared data ",
							Toast.LENGTH_SHORT).show();


				} else if (selecteditem.equals("Logout")) {
					LogoutUserDialog(SecondScreenActivity.this, getResources()
									.getString(R.string.app_name), getResources()
									.getString(R.string.settings_logout_descrp),
							getResources().getString(R.string.ok),
							getResources().getString(R.string.no), "OKCANCEL");
				} else if (selecteditem.equals("Offline Sync")) {

					if (GetWebServiceData
							.isNetworkAvailable(SecondScreenActivity.this)) {

						if (AppSetting.getRequestId() != null)
							new GetOfflineSyncDataAsyncTask().execute();

					} else {
						AppCommonDialog.showSimpleDialog(
								SecondScreenActivity.this, getResources()
										.getString(R.string.app_name),
								getResources()
										.getString(R.string.check_network),
								getResources().getString(R.string.ok), "OK");
					}

				}

				lstMenu.dismiss();

			}
		});


		((TextView) findViewById(R.id.xtxtheaderTittle)).setText("Home");

		((ImageButton) findViewById(R.id.backId))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});

		findViewById(R.id.xbtnupdate).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (menulistdata != null && menulistdata.size() > 0)
					lstMenu.setAdapter(new LocationDetailAdapter(
							SecondScreenActivity.this, menulistdata));
				lstMenu.setAnchorView(((ImageView) findViewById(R.id.xbtnupdate)));
				lstMenu.setWidth(250);
				lstMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
				lstMenu.setModal(true);
				lstMenu.show();

			}
		});
		// Creating The ViewPagerAdapter and Passing Fragment Manager, Titles
		// fot the Tabs and Number Of Tabs.
		// FragmentManager fm = activity.getSupportFragmentManager();
		adapter = new ViewPagerAdpater(getSupportFragmentManager(), Titles, Numboftabs);

		// Assigning ViewPager View and setting the adapter
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		// Assigning the Sliding Tab Layout View
		tabs = (SlidingTabLayout) findViewById(R.id.tabs);
		tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
		// This makes the tabs Space Evenly in
		// Available width

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});

		// Setting the ViewPager For the SlidingTabsLayout
		tabs.setViewPager(pager);


	}

	public void getPermissions() {
		if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(new String[]{
						android.Manifest.permission.ACCESS_COARSE_LOCATION,
						android.Manifest.permission.ACCESS_FINE_LOCATION,
						android.Manifest.permission.READ_EXTERNAL_STORAGE,
						android.Manifest.permission.CAMERA,
						android.Manifest.permission.CALL_PHONE}, 1);
			}
			return;
		}
	}

	public void getlocation() {
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				current_Lattitude = location.getLatitude();
				current_Longitude = location.getLongitude();
				Log.e("location ", current_Lattitude + "//" + current_Longitude);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(SecondScreenActivity.this);
			dialog.setMessage("Please Enable Location For Application");
			dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(myIntent);
				}
			});
			dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				}
			});
			dialog.show();
		}
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(new String[]{
						android.Manifest.permission.ACCESS_COARSE_LOCATION,
						android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);


			}
			return;
		}
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1000, locationListener);
	}


	public void updateOnlineData(String RequestID) {

		requestTab.clear();
		buildingTab.clear();
		locationTab.clear();
		flatTab.clear();
		projectTab.clear();
		roomDetail.clear();
		roomArea.clear();
		roomAreaFB.clear();

		if (requestDataList != null) {
			requestDataList = mypcplData.getRequestAllvalues(RequestID);
			locationDataList = mypcplData.getLocationAllvalues(RequestID);
			projectDataList = mypcplData.getProjectAllvalues(RequestID);
			buildingDataList = mypcplData.getBuildingAllvalues(RequestID);
			flatDataList = mypcplData.getFlatAllvalues(RequestID);
			roomDataList = mypcplData.getRoomAllvalues(RequestID);
			carpetDataList = mypcplData.getCarpetAllvalues(RequestID);
			flowerbedDataList = mypcplData.getFlowerbedAllvalues(RequestID);

			if (requestDataList != null && requestDataList.size() > 0) {
				for (SetOfflinedata dataOffline : requestDataList) {
					if (dataOffline.getSaveuserdata() != null
							&& dataOffline.getSaveuserdata().length() > 0) {
						RequestDetailsResponse serverResponseRequest = new Gson()
								.fromJson(dataOffline.getSaveuserdata(),
										RequestDetailsResponse.class);

						for (RequestDataDetail requestdata : serverResponseRequest
								.getValues()) {
							requestTab.add(requestdata);
						}
					}

				}
			}

			// location data get
			if (locationDataList != null && locationDataList.size() > 0)
			{
				for (SetOfflinedata dataOffline : locationDataList) {
					if (dataOffline.getSaveuserdata() != null
							&& dataOffline.getSaveuserdata().length() > 0) {
						LocationDetailsResponse serverResponseRequest = new Gson()
								.fromJson(dataOffline.getSaveuserdata(),
										LocationDetailsResponse.class);

						for (GetLocationResponse requestdata : serverResponseRequest
								.getValues()) {
							locationTab.add(requestdata);
						}
					}

				}
			}

			// projecttab data get
			if (projectDataList != null && projectDataList.size() > 0)
			{
				for (SetOfflinedata dataOffline : projectDataList) {
					if (dataOffline.getSaveuserdata() != null
							&& dataOffline.getSaveuserdata().length() > 0) {
						ProjectTabResponse serverResponseRequest = new Gson()
								.fromJson(dataOffline.getSaveuserdata(),
										ProjectTabResponse.class);

						for (GetProjectDetailsResponse requestdata : serverResponseRequest
								.getValues()) {
							projectTab.add(requestdata);
						}
					}

				}
			}

			// buildingtab data get
			if (buildingDataList != null && buildingDataList.size() > 0)
			{
				for (SetOfflinedata dataOffline : buildingDataList) {
					if (dataOffline.getSaveuserdata() != null
							&& dataOffline.getSaveuserdata().length() > 0) {
						BuildingResponse serverResponseRequest = new Gson()
								.fromJson(dataOffline.getSaveuserdata(),
										BuildingResponse.class);

						for (GetBuildingDetailsResponse requestdata : serverResponseRequest
								.getValues()) {
							buildingTab.add(requestdata);
						}
					}

				}
			}

			// flattab data get
			if (flatDataList != null && flatDataList.size() > 0) {
				for (SetOfflinedata dataOffline : flatDataList) {
					if (dataOffline.getSaveuserdata() != null
							&& dataOffline.getSaveuserdata().length() > 0) {
						FlatSpecificationResponse serverResponseRequest = new Gson()
								.fromJson(dataOffline.getSaveuserdata(),
										FlatSpecificationResponse.class);

						for (GetFlatSpecificationDetailsResponse requestdata : serverResponseRequest
								.getValues()) {
							flatTab.add(requestdata);
						}
					}

				}
			}

			// Roomtab data get
			if (roomDataList != null && roomDataList.size() > 0) {
				for (SetOfflinedata dataOffline : roomDataList) {
					if (dataOffline.getSaveuserdata() != null
							&& dataOffline.getSaveuserdata().length() > 0) {
						RoomdetailResponse serverResponseRequest = new Gson()
								.fromJson(dataOffline.getSaveuserdata(),
										RoomdetailResponse.class);

						for (GetRoomdetailResponse requestdata : serverResponseRequest
								.getValues()) {
							roomDetail.add(requestdata);
						}
					}

				}
			}

			// Carpet data get
			if (carpetDataList != null && carpetDataList.size() > 0) {
				for (SetOfflinedata dataOffline : carpetDataList) {
					if (dataOffline.getSaveuserdata() != null
							&& dataOffline.getSaveuserdata().length() > 0) {
						CarpetResponse serverResponseRequest = new Gson()
								.fromJson(dataOffline.getSaveuserdata(),
										CarpetResponse.class);

						for (GetCarpetAreaResponse requestdata : serverResponseRequest
								.getValuesCarpetArea()) {
							roomArea.add(requestdata);
						}
					}

				}
			}

			// Carpet data get
			if (flowerbedDataList != null && flowerbedDataList.size() > 0) {
				for (SetOfflinedata dataOffline : flowerbedDataList) {
					if (dataOffline.getSaveuserdata() != null
							&& dataOffline.getSaveuserdata().length() > 0) {
						CarpetResponse serverResponseRequest = new Gson()
								.fromJson(dataOffline.getSaveuserdata(),
										CarpetResponse.class);

						for (GetCarpetAreaResponse requestdata : serverResponseRequest
								.getValuesCarpetArea()) {
							roomAreaFB.add(requestdata);
						}
					}

				}
			}
			if (requestTab != null) {
				SetDetails.add(new SetOfflineRequest(requestTab, buildingTab,
						locationTab, flatTab, projectTab, roomDetail, roomArea,
						roomAreaFB));
			}
		}

	}

	private void prepareActivity() {
		AppDetails.setContext(this);
		AppDetails.setActivity(this);

	}

	// Requestdetail Task
	public class SetOfflineDataAsyncTask extends
			AsyncTask<String, Void, String> {
		String requestTab;
		String locationTab;
		String projectTab;
		String buildingTab;
		String flatTab;
		String roomTab;
		String carpetTab;
		String flowerbedTab;

		/*
		 * public SetOfflineDataAsyncTask(String requestTab, String locationTab,
		 * String projectTab, String buildingTab, String flatTab, String
		 * roomTab, String carpetTab, String flowerbedTab) {
		 * this.requestTab=requestTab; this.locationTab=locationTab;
		 * this.projectTab=projectTab; this.buildingTab=buildingTab;
		 * this.flatTab=flatTab; this.roomTab=roomTab; this.carpetTab=carpetTab;
		 * this.flowerbedTab=flowerbedTab; }
		 */

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				loadingDialog = new ProgressDialog(SecondScreenActivity.this);
				loadingDialog.setCancelable(false);
				loadingDialog.setMessage("Loading..");
				loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				loadingDialog.show();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String data = "";
			Gson gsondata = new Gson();
			List<NameValuePair> nameValuePairs = null;
			CommonRequestData savetab = new CommonRequestData();
			try {
				nameValuePairs = new ArrayList<NameValuePair>(1);
			} catch (Exception e) {
			}
			savetab.setSetDetails(SetDetails);
			// JsonObject obj=new JsonObject();

			String strsave = gsondata.toJson(savetab);
			nameValuePairs
					.add(new BasicNameValuePair("AllRequestData", strsave));
			/*
			 * nameValuePairs.add(new BasicNameValuePair("RequestTab",
			 * requestTab)); nameValuePairs.add(new
			 * BasicNameValuePair("LocationTab", locationTab));
			 * nameValuePairs.add(new BasicNameValuePair("ProjectTab",
			 * projectTab)); nameValuePairs.add(new
			 * BasicNameValuePair("BuildingTab", buildingTab));
			 * nameValuePairs.add(new BasicNameValuePair("FlatTab", flatTab));
			 * nameValuePairs.add(new BasicNameValuePair("RoomTab", roomTab));
			 * nameValuePairs.add(new BasicNameValuePair("CarpetTab",
			 * carpetTab));
			 */

			Log.e("Request data display offline second","REQUEST_DATA Second= " + strsave);
			//SERVER_URL=obj.getServerURL();
			System.out.println("Request before : " + nameValuePairs.toString());
			return GetWebServiceData.getServerResponse(SERVER_URL
					+ "/LoadAllRequests", nameValuePairs, data);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (result != null) {
				Log.i("Response", result);
				try {
					JSONObject serverResponsere = new JSONObject(result);

					CommonResponse serverResponseRequest = new Gson().fromJson(
							result, CommonResponse.class);

					String loginResult = serverResponsere.getString("status");
					if (loginResult.equals("OK")) {
						Log.d("reuest detail", "get data");
						//Toast.makeText(activity, "sucessfully " + loginResult,Toast.LENGTH_SHORT).show();
                   AppCommonDialog.showSimpleDialog(SecondScreenActivity.this, getResources().getString(R.string.app_name),
		                                        "sucessfully "+loginResult,
		                                       getResources().getString(R.string.ok), "OK");
						boolean bdelete = mypcplData.DeleteAllRecord();
						if (bdelete)
						{
							Toast.makeText(activity,
									"successfully update and clear local data ",
									Toast.LENGTH_SHORT).show();
							if (SetDetails != null && SetDetails.size() > 0) {
								SetDetails.clear();

							}
							AppSetting.setUpdateOfflinedata(false);
						}

						/*
						 * if (serverResponseRequest != null&&
						 * serverResponseRequest.getStatus()!=null) {
						 * AppCommonDialog.showSimpleDialog(getActivity(),
						 * getResources
						 * ().getString(R.string.app_name),"Data Update Sucessfully"
						 * , getResources().getString(R.string.ok), "OK"); }
						 */
					} else {
						// Toast.makeText(getApplicationContext(),
						// faultObject.getString("faultstring"),
						// Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("LoginActivity", "JSON Result parse error");
					e.printStackTrace();
				}

			}
		}

	}

	androidx.appcompat.app.AlertDialog alertLogin;

	public void LogoutUserDialog(SecondScreenActivity homeActivity,
			String alerttitle, String messagetxt, String btnlogoutTxt,
			String btncanceltxt, String btnvalue) {
		if (alertLogin != null)
			alertLogin.dismiss();
		alertLogin = AppCommonDialog.showCustomAlertDialog(homeActivity,
				alerttitle, messagetxt, btnlogoutTxt, btncanceltxt, btnvalue);
		alertLogin.show();





		Button btnLogout = (Button) alertLogin.findViewById(R.id.btn_cancel);
		Button btnCancel = (Button) alertLogin.findViewById(R.id.xbtnok);

		btnLogout.setText(getResources().getString(R.string.yes));
		btnCancel.setText(getResources().getString(R.string.no));

		btnLogout.setOnClickListener(new OnClickListener() {


			@Override
			public void onClick(View v) {
				alertLogin.dismiss();
				AppSetting.setRemeberMe(false);
				//AppSetting.setUserLoginEmail("");
				//AppSetting.setUserId("");
				//AppSetting.setUserName("");



				Intent restaurantintent = new Intent(SecondScreenActivity.this,
						LoginActivity.class);
				restaurantintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(restaurantintent);
				finish();
			}
		});
	}

	Dialog alertClear;

	public void ClearUserDialog(SecondScreenActivity homeActivity,
								 String alerttitle, String messagetxt, String btnlogoutTxt,
								 String btncanceltxt, String btnvalue) {
		if (alertClear != null)
			alertClear.dismiss();
		alertClear = AppCommonDialog.showCustomAlertDialog(homeActivity,
				alerttitle, messagetxt, btnlogoutTxt, btncanceltxt, btnvalue);
		alertClear.show();


		Button btnClear = (Button) alertClear.findViewById(R.id.btn_cancel);
		Button btnCancel = (Button) alertClear.findViewById(R.id.xbtnok);

		btnClear.setText(getResources().getString(R.string.yes));
		btnCancel.setText(getResources().getString(R.string.no));

		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertClear.dismiss();
				AppSetting.setRemeberMe(false);
				//AppSetting.setUserLoginEmail("");
				//AppSetting.setUserId("");
				//AppSetting.setUserName("");



				Intent restaurantintent = new Intent(SecondScreenActivity.this,
						LoginActivity.class);
				restaurantintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(restaurantintent);
				finish();
			}
		});
	}


	// Requestdetail Task
		public class GetOfflineSyncDataAsyncTask extends
				AsyncTask<String, Void, String> {




			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				try {
					loadingDialog = new ProgressDialog(SecondScreenActivity.this);
					loadingDialog.setCancelable(false);
					loadingDialog.setMessage("Loading..");
					loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

					loadingDialog.show();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String data = "";
				Gson gsondata = new Gson();
				List<NameValuePair> nameValuePairs = null;
				CommonRequestData savetab = new CommonRequestData();
				try {
					nameValuePairs = new ArrayList<NameValuePair>(1);
				} catch (Exception e) {
				}



				nameValuePairs.add(new BasicNameValuePair("EngineerID",AppSetting.getUserId()));




				System.out.println("Request before : " + nameValuePairs.toString());
				return GetWebServiceData.getServerResponse(SERVER_URL
						+ "/syncOfflineFromOnline", nameValuePairs, data);

			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				try {
					if (loadingDialog != null && loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (result != null) {
					Log.i("Response", result);
					try {
						JSONObject serverResponsere = new JSONObject(result);

						OfflineSyncResponse serverResponseRequest = new Gson().fromJson(
								result, OfflineSyncResponse.class);




						String loginResult = serverResponsere.getString("status");
						if (loginResult.equals("OK")) {

							//Toast.makeText(activity, "sucessfully " + loginResult,Toast.LENGTH_SHORT).show();
							//boolean bdelete = mypcplData.DeleteRecord();
							AppCommonDialog.showSimpleDialog(SecondScreenActivity.this, getResources()
									.getString(R.string.app_name),
									 "successfully "+loginResult,
									getResources().getString(R.string.ok), "OK");
						//	if (bdelete) {
								//Toast.makeText(activity,"sucessfully clear local data ",Toast.LENGTH_SHORT).show();
						//	}

                 if(serverResponseRequest!=null){
	                String requestTabRequest = null;
	                String requestLocationTab = null;
	                String requestProjectTab = null;
	                String requestBuildingTab = null;
	                String requestFlatTab = null;
	                String requestRoomTab = null;
	                String roomDetailid=null;
	                String carpetDetailid=null;
	                String flowerDetailid=null;

	                Gson gson = new Gson();
							for (RequestDetailsResponse requestdata : serverResponseRequest.getRequestData()) {
								requestdata.setStatus("OK");

								requestTabRequest = gson.toJson(requestdata);
								System.out.println("Request Syn Offline RequestTab : " +requestTabRequest);
								for (RequestDataDetail requestdatadetail : requestdata.getValues()) {

									  if (requestTabRequest!=null&&requestdatadetail!=null&& requestdatadetail.getRequestId()!=null) {
								        	SecondScreenActivity.mypcplData.insertdata(requestTabRequest, requestdatadetail.getRequestId());

										}
								}

							}
			// location data save
							for(LocationDetailsResponse locationDetail:serverResponseRequest.getLocationData()){
								locationDetail.setStatus("OK");

								requestLocationTab = gson.toJson(locationDetail);
								System.out.println("Request Syn Offline location : " +requestLocationTab);
								for (GetLocationResponse locationdata : locationDetail.getValues()) {
									 if (requestLocationTab!=null&& locationdata!=null && locationdata.getRequestId()!=null) {
										 SecondScreenActivity.mypcplData.insertLocationdata(requestLocationTab, locationdata.getRequestId());

										}
									}
							}


							// Project tab data save				
							for(ProjectTabResponse projectDetail:serverResponseRequest.getProjectData()){
								projectDetail.setStatus("OK");

								requestProjectTab = gson.toJson(projectDetail);
								System.out.println("Request Syn Offline Project : " +requestLocationTab);
								for (GetProjectDetailsResponse projectdata : projectDetail.getValues()) {
									 if (requestProjectTab!=null&& projectdata!=null && projectdata.getRequestId()!=null) {
										 SecondScreenActivity.mypcplData.insertProjectdata(requestProjectTab, projectdata.getRequestId());

										}
									}
							}

							// Building  tab data save				
							for(BuildingResponse buildingDetail:serverResponseRequest.getBuildingData()){
								buildingDetail.setStatus("OK");

								requestBuildingTab = gson.toJson(buildingDetail);
								System.out.println("Request Syn Offline building : " +requestBuildingTab);
								for (GetBuildingDetailsResponse buindingdata : buildingDetail.getValues()) {
									 if (requestBuildingTab!=null&& buindingdata!=null && buindingdata.getRequestId()!=null) {
										 SecondScreenActivity.mypcplData.insertBuildingdata(requestBuildingTab, buindingdata.getRequestId());

										}
									}
							}

							// Flat  tab data save				
							for(FlatSpecificationResponse flatDetail:serverResponseRequest.getFlatData()){
								flatDetail.setStatus("OK");

								requestFlatTab = gson.toJson(flatDetail);
								System.out.println("Request Syn Offline flat : " +requestFlatTab);
								for (GetFlatSpecificationDetailsResponse flatdata : flatDetail.getValues()) {
									 if (requestFlatTab!=null&& flatdata!=null && flatdata.getRequestId()!=null) {
										 SecondScreenActivity.mypcplData.insertFlatdata(requestFlatTab, flatdata.getRequestId());

										}
									}
							}

							// Room  tab data save				
							for(RoomdetailResponse roomDetail:serverResponseRequest.getRoomData()){
								List<GetRoomdetailResponse> roomTab = null;
								roomDetail.setStatus("OK");


								System.out.println("Request Syn Offline Room : " +requestBuildingTab);
								for (GetRoomdetailResponse roomdata : roomDetail.getValues()) {
									roomTab=new ArrayList<GetRoomdetailResponse>();
									roomTab.add(roomdata);
									roomDetail.setValues(roomTab);
									requestRoomTab = gson.toJson(roomDetail);
									roomDetailid = roomdata.getRoomDetailsID();
									if(roomDetailid==null){
										roomDetailid=String.valueOf(genNumber());
									}
									 if (requestRoomTab!=null&& roomdata!=null &&roomDetailid!=null  && roomdata.getRequestId()!=null) {
										 SecondScreenActivity.mypcplData.insertRoomdata(requestRoomTab, roomdata.getRequestId(),roomDetailid);


										}
									}
							}


							// Carpet  tab data save				
							for(CarpetResponse carpetDetail:serverResponseRequest.getCarpetAreaTab()){
								List<GetCarpetAreaResponse> carpetTab = null;
								carpetDetail.setStatus("OK");

								if(carpetDetail!=null){
								for (GetCarpetAreaResponse carpetdata : carpetDetail.getValuesCarpetArea()) {
									carpetTab=new ArrayList<GetCarpetAreaResponse>();
									if(carpetdata!=null){
									carpetTab.add(carpetdata);
									carpetDetail.setValuesCarpetArea(carpetTab);
									requestRoomTab = gson.toJson(carpetDetail);

									System.out.println("Request Syn Offline Carpet : " +requestRoomTab);
									carpetDetailid = carpetdata.getRoomDetailsIDs();

									if(carpetDetailid==null){
										carpetDetailid=String.valueOf(genNumber());
									}
									 if (requestRoomTab!=null&& carpetdata!=null &&carpetDetailid!=null  && carpetdata.getRequestId()!=null) {
										 SecondScreenActivity.mypcplData.insertCarpetdata(requestRoomTab, carpetdata.getRequestId(),carpetDetailid);


										}
									}
									}
								}
							}

							// Carpet  tab data save				
							for(CarpetResponse carpetDetail:serverResponseRequest.getFlowerBedAreaTab()){
								List<GetCarpetAreaResponse> flowerTab = null;
								carpetDetail.setStatus("OK");




								//requestRoomTab = gson.toJson(carpetDetail);
								System.out.println("Request Syn Offline Carpet : " +requestRoomTab);
								if(carpetDetail!=null){
								for (GetCarpetAreaResponse carpetdata : carpetDetail.getValuesCarpetArea()) {
									flowerTab=new ArrayList<GetCarpetAreaResponse>();
									if(carpetdata!=null){
									carpetDetailid = carpetdata.getRoomDetailsIDs();
									flowerTab.add(carpetdata);
									carpetDetail.setValuesCarpetArea(flowerTab);
									requestRoomTab = gson.toJson(carpetDetail);
									if(carpetDetailid==null){
										carpetDetailid=String.valueOf(genNumber());
									}
									 if (requestRoomTab!=null&& carpetdata!=null &&carpetDetailid!=null  && carpetdata.getRequestId()!=null) {
										 SecondScreenActivity.mypcplData.insertFlowerbeddata(requestRoomTab, carpetdata.getRequestId(),carpetDetailid);


										}
									}
									}
								}
							}





}


						} else {
							// Toast.makeText(getApplicationContext(),
							// faultObject.getString("faultstring"),
							// Toast.LENGTH_SHORT).show();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d("LoginActivity", "JSON Result parse error");
						e.printStackTrace();
					}

				}
			}

		}

		public int genNumber() {
		    Random r = new Random( System.currentTimeMillis() );
		    return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
		}

}
