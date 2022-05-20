package com.techinvest.pcplrealestate.fragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.techinvest.pcpl.adapter.LocationDetailAdapter;
import com.techinvest.pcpl.adapter.RequestDetailAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.commonutil.GPSLocationReader;
import com.techinvest.pcpl.commonutil.GPSTracker;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.GetLocationResponse;
import com.techinvest.pcpl.model.LocationDetailsResponse;
import com.techinvest.pcpl.model.Offlinedatamodel;
import com.techinvest.pcpl.model.RequestDataDetail;
import com.techinvest.pcpl.model.RequestDetailsResponse;
import com.techinvest.pcpl.model.RequestDropdownData;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.parserhelper.DataBaseHelper;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcplrealestate.LoginActivity;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.SecondScreenActivity;
import com.techinvest.pcplrealestate.R.layout;
import com.techinvest.pcplrealestate.Requestdetails.RequestDetailAsyncTask;
import com.techinvest.pcplrealestate.Requestdetails.SetRequestDetailAsyncTask;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

@SuppressLint("NewApi")
public class Locationdetails extends Fragment implements AppConstants, OnClickListener, OnItemSelectedListener, OnItemClickListener {

    private View view;
    String url = AppSetting.getapiURL();
    Activity activity;
    private ProgressDialog loadingDialog;
    List<GetLocationResponse> getlocationData;

    ListPopupWindow lstpopupclass;
    ListPopupWindow lstproperytype;
    ListPopupWindow lstViewtype;
    ListPopupWindow lstsite;
    ListPopupWindow lstAutoavailable;
    ListPopupWindow lstBusStop;
    ListPopupWindow lstRailwaystation;

    ListPopupWindow lstBusdistance;
    ListPopupWindow lstRailwayDistance;
    ListPopupWindow lsthospitaldistance;

    List<String> properytypeData;
    List<String> locationclassData;
    List<String> viewtypeData;
    List<String> siteData;
    List<String> autoavailableData;
    List<String> busStopData;
    List<String> railwaystationData;
    List<String> distancerailwaystationData;
    List<String> distanceBusData;
    //List<String> distanceHosData;
    private GPSTracker gps;
    double latitude = 0.0;
    double longitude = 0.0;
    SharedPreferences mPrefs;
    //DataBaseHelper mypcplData;
    List<Offlinedatamodel> offlineData;
    List<SetOfflinedata> locationDataList;
    String[] locationArr;

    private Context context;
    //private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    //private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.demo_location, container, false);


        getlocationData = new ArrayList<GetLocationResponse>();
        properytypeData = new ArrayList<String>();
        locationclassData = new ArrayList<String>();
        viewtypeData = new ArrayList<String>();
        siteData = new ArrayList<String>();
        autoavailableData = new ArrayList<String>();
        busStopData = new ArrayList<String>();
        railwaystationData = new ArrayList<String>();
        lstpopupclass = new ListPopupWindow(getActivity());
        lstproperytype = new ListPopupWindow(getActivity());
        lstViewtype = new ListPopupWindow(getActivity());
        lstsite = new ListPopupWindow(getActivity());
        lstAutoavailable = new ListPopupWindow(getActivity());
        lstBusStop = new ListPopupWindow(getActivity());
        lstRailwaystation = new ListPopupWindow(getActivity());
        lstBusdistance = new ListPopupWindow(getActivity());
        lstRailwayDistance = new ListPopupWindow(getActivity());
        lsthospitaldistance = new ListPopupWindow(getActivity());
        distancerailwaystationData = new ArrayList<String>();
        distanceBusData = new ArrayList<String>();

        offlineData = new ArrayList<Offlinedatamodel>();
        locationDataList = new ArrayList<SetOfflinedata>();

        if (offlineData != null) {

            offlineData = SecondScreenActivity.mypcplData.getLocationdata(AppSetting.getRequestId());
            // offlineData = (ArrayList<Offlinedatamodel>) ObjectSerializer.deserialize(mPrefs.getString("LIST", ObjectSerializer.serialize(new ArrayList<Offlinedatamodel>())));

        }

        for (int i = 0; i < 50; i++) {
            distanceBusData.add(String.valueOf(i) + " " + "Mins");
        }
        for (int i = 0; i < 50; i++) {
            distancerailwaystationData.add(String.valueOf(i) + " " + "Kms");
        }

        initview(view);
        mPrefs = activity.getPreferences(activity.MODE_PRIVATE);
        getCurrentLocationdetail();

        if (GetWebServiceData.isNetworkAvailable(getActivity())) {
            new LocationDetailAsyncTask().execute();

        } else {

            if (offlineData.size() > 0) {
                for (Offlinedatamodel datalocal : offlineData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestid().equals(AppSetting.getRequestId())) {
                        LocationDetailsResponse serverResponseRequest = new Gson()
                                .fromJson(datalocal.getResponsejson(), LocationDetailsResponse.class);
                        displaydataOffOn(serverResponseRequest);
                    }
                }
                // updateand edit user data
                updateandDisplayofflineData(AppSetting.getRequestId());


               /* AppCommonDialog.showSimpleDialog(getActivity(), getResources()
                                .getString(R.string.app_name),
                        getResources().getString(R.string.check_network),
                        getResources().getString(R.string.ok), "OK");*/

            }
        }


        lstpopupclass.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //if(parent.getItemAtPosition(position).toString()!=null)
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoclass)).setText(selecteditem);
                lstpopupclass.dismiss();

            }
        });


        lstproperytype.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoType)).setText(selecteditem);
                lstproperytype.dismiss();

            }
        });

        lstViewtype.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoview)).setText(selecteditem);
                lstViewtype.dismiss();

            }
        });

        lstsite.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoSite)).setText(selecteditem);
                lstsite.dismiss();

            }
        });


        lstAutoavailable.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((TextView) view.findViewById(R.id.xedtautoavailable)).setText(selecteditem);
                lstAutoavailable.dismiss();

            }
        });


        lstBusStop.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestbusstop)).setText(selecteditem);
                lstBusStop.dismiss();

            }
        });

        lstRailwaystation.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).setText(selecteditem);
                lstRailwaystation.dismiss();

            }
        });

        lstRailwayDistance.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceRailwayKms)).setText(selecteditem);
                lstRailwayDistance.dismiss();

            }
        });


        lstBusdistance.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceBusMins)).setText(selecteditem);
                lstBusdistance.dismiss();

            }
        });


        lsthospitaldistance.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistancehosKms)).setText(selecteditem);
                lsthospitaldistance.dismiss();

            }
        });

        if (railwaystationData != null && railwaystationData.size() > 0) {

            locationArr = new String[railwaystationData.size()];
            locationArr = railwaystationData.toArray(locationArr);

        }

        if (locationArr != null && locationArr.length > 0) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.select_dialog_item, locationArr);
            //Used to specify minimum number of
            //characters the user has to type in order to display the drop down hint.
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).setThreshold(1);
            //Setting adapter
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).setAdapter(arrayAdapter);

        }


        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).setOnItemSelectedListener(this);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).setOnItemClickListener(this);

        return view;
    }


    private void updateandDisplayofflineData(String RequestID) {
        locationDataList = SecondScreenActivity.mypcplData.getLocationAllvalues(RequestID);
        if (locationDataList != null && locationDataList.size() > 0) {
            for (SetOfflinedata dataOffline : locationDataList) {
                if (dataOffline.getSaveuserdata() != null && dataOffline.getSaveuserdata().length() > 0) {
                    LocationDetailsResponse serverResponseRequest = new Gson()
                            .fromJson(dataOffline.getSaveuserdata(), LocationDetailsResponse.class);

                    if (getlocationData != null && getlocationData.size() > 0)
                        getlocationData.clear();
                    for (GetLocationResponse requestdata : serverResponseRequest
                            .getValues()) {
                        getlocationData.add(requestdata);
                    }
                }
            }

            if (getlocationData != null && getlocationData.size() > 0) {
                for (GetLocationResponse datalocal : getlocationData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestId().equals(AppSetting.getRequestId())) {
                        displayLocationDetail(getlocationData);
                    }
                }
            }
        }

    }


    private void getCurrentLocationdetail() {
        // create class object
        gps = new GPSTracker(getActivity());

        // check if GPS enabled
        if (gps.canGetLocation()) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // AppSetting.setUserLatitude(String.valueOf(latitude));
            // AppSetting.setUserLongitude(String.valueOf(longitude));


            // String currentCity = new GPSLocationReader().getAddress(latitude, longitude);
            // ((Button) view).setText(currentCity);
            // Toast.makeText(getActivity(), "Your Location is - " + currentCity, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }


    private void initview(View v) {
        // TODO Auto-generated method stub
        v.findViewById(R.id.ximgdropdownType).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownSite).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownclass).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownview).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautoavailable).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownNearestbusstop).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownNearestrailwaystation).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdowndistanceKms).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdowndistanceMins).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdowndistancehosKms).setOnClickListener(this);
        v.findViewById(R.id.xbtnsave).setOnClickListener(this);
        v.findViewById(R.id.xedtlongitude).setOnClickListener(this);
        v.findViewById(R.id.xedtLatitude).setOnClickListener(this);

    }


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;

    }


    // Requestdetail Task
    public class LocationDetailAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                loadingDialog = new ProgressDialog(getActivity());
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
            List<NameValuePair> nameValuePairs = null;

            if (AppSetting.getRequestId() != null) {
                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("requestID",
                        AppSetting.getRequestId()));
            }
            //Log.d("LoginActivity", "Attempting Signup");
            return GetWebServiceData.getServerResponse(url
                    + "/GetLocationTabDetails", nameValuePairs, data);

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
                Log.i("Request", result);
                try {
                    JSONObject serverResponsere = new JSONObject(result);
                    Log.d("addResActivity", "Save Review Successful");
                    LocationDetailsResponse serverResponseRequest = new Gson()
                            .fromJson(result, LocationDetailsResponse.class);

                    String loginResult = serverResponsere.getString("status");
                    if (loginResult.equals("OK")) {
							/* Editor prefsEditor = mPrefs.edit();
						        Gson gson = new Gson();
						        //String json = gson.toJson(requestgetData);
						        prefsEditor.putString("LocationData", result);
						        prefsEditor.commit();*/
                        if (result != null && AppSetting.getRequestId() != null) {
                            //	 SecondScreenActivity.mypcplData.insertLocationdata(result, AppSetting.getRequestId());

                        }


                        displaydataOffOn(serverResponseRequest);


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


    public void displayLocationDetail(List<GetLocationResponse> getlocationData) {
        // TODO Auto-generated method stub
        if (getlocationData != null && getlocationData.size() > 0) {
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoType)).setText(getlocationData.get(0).getLCTYPE());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoclass)).setText(getlocationData.get(0).getCLASS());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoSite)).setText(getlocationData.get(0).getSITENAME());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoview)).setText(getlocationData.get(0).getVIEWNAME());

            ((EditText) view.findViewById(R.id.xedtNorthName)).setText(getlocationData.get(0).getNorth());
            ((EditText) view.findViewById(R.id.xedtEastName)).setText(getlocationData.get(0).getEast());
            ((EditText) view.findViewById(R.id.xedtsouth)).setText(getlocationData.get(0).getSouth());
            ((EditText) view.findViewById(R.id.xedtwest)).setText(getlocationData.get(0).getWest());
///////////////////////////////////////////////////////////////////Change/////////////////////////////////////
            if (getlocationData.get(0).getLattitude() != null || getlocationData.get(0).getLongitude() != null) {
                ((TextView) view.findViewById(R.id.xedtLatitude)).setText(getlocationData.get(0).getLattitude());
                ((TextView) view.findViewById(R.id.xedtlongitude)).setText(getlocationData.get(0).getLongitude());
                AppSetting.setUserLatitude(getlocationData.get(0).getLattitude());
                AppSetting.setUserLongitude(getlocationData.get(0).getLongitude());
                Log.e("123456", "" + getlocationData.get(0).getLattitude() + "\n" + getlocationData.get(0).getLattitude());
            } else {
                if (String.valueOf(latitude) != null && String.valueOf(longitude) != null) {
                    ((TextView) view.findViewById(R.id.xedtLatitude)).setText(String.valueOf(latitude));
                    ((TextView) view.findViewById(R.id.xedtlongitude)).setText(String.valueOf(longitude));
                }
            }
///////////////////////////////////////////////////////////////////Change/////////////////////////////////////

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).setText(getlocationData.get(0).getNearRailWayStation());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceRailwayKms)).setText(getlocationData.get(0).getDistancesRailway());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestbusstop)).setText(getlocationData.get(0).getNearBusStop());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceBusMins)).setText(getlocationData.get(0).getDistancesBus());
            ((EditText) view.findViewById(R.id.xedtNearesthospital)).setText(getlocationData.get(0).getHospital());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistancehosKms)).setText(getlocationData.get(0).getDistancehosp());
            ((TextView) view.findViewById(R.id.xedtautoavailable)).setText(getlocationData.get(0).getAutoAvailability());
        }

        try {
            setAutoFillData();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error :" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void setAutoFillData() {
        String[] tmp1 = new String[properytypeData.size()];
        tmp1 = properytypeData.toArray(tmp1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp1);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoType)).setAdapter(adapter1);

        String[] tmp2 = new String[locationclassData.size()];
        tmp2 = locationclassData.toArray(tmp2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp2);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoclass)).setAdapter(adapter2);

        String[] tmp3 = new String[siteData.size()];
        tmp3 = siteData.toArray(tmp3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp3);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoSite)).setAdapter(adapter3);

        String[] tmp4 = new String[viewtypeData.size()];
        tmp4 = viewtypeData.toArray(tmp4);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp4);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoview)).setAdapter(adapter4);

        String[] tmp5 = new String[railwaystationData.size()];
        tmp5 = railwaystationData.toArray(tmp5);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp5);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).setAdapter(adapter5);

        String[] tmp6 = new String[distancerailwaystationData.size()];
        tmp6 = distancerailwaystationData.toArray(tmp6);
        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp6);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceRailwayKms)).setAdapter(adapter6);

        String[] tmp7 = new String[busStopData.size()];
        tmp7 = busStopData.toArray(tmp7);
        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp7);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestbusstop)).setAdapter(adapter7);

        String[] tmp8 = new String[distanceBusData.size()];
        tmp8 = distanceBusData.toArray(tmp8);
        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp8);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceBusMins)).setAdapter(adapter8);

        String[] tmp9 = new String[distancerailwaystationData.size()];
        tmp9 = distancerailwaystationData.toArray(tmp9);
        ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp9);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistancehosKms)).setAdapter(adapter9);


    }


    public void displaydataOffOn(LocationDetailsResponse serverResponseRequest) {
        if (serverResponseRequest != null && serverResponseRequest.getValues() != null
                && serverResponseRequest.getLocationClass() != null && serverResponseRequest.getPropertyClass() != null && serverResponseRequest.getSiteClass() != null && serverResponseRequest.getViewType() != null
                && serverResponseRequest.getBusStop() != null && serverResponseRequest.getAutoAvailability() != null && serverResponseRequest.getRailwayStation() != null) {

            for (GetLocationResponse locationdata : serverResponseRequest.getValues()) {
                getlocationData.add(locationdata);
            }
            for (RequestDropdownData dropdata : serverResponseRequest
                    .getPropertyClass()) {
                locationclassData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getLocationClass()) {
//if loop for residential house
                if (dropdata.getDatavalue().equalsIgnoreCase("Residential Flat")){

                }
                else {
                    properytypeData.add(dropdata.getDatavalue());
                }

            }
            for (RequestDropdownData dropdata : serverResponseRequest
                    .getSiteClass()) {

                siteData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getViewType()) {

                viewtypeData.add(dropdata.getDatavalue());
            }
            viewtypeData.add("Lake Facing");
            viewtypeData.add("River Facing");
            viewtypeData.add("Temple Facing");

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getAutoAvailability()) {

                autoavailableData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getBusStop()) {

                busStopData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getRailwayStation()) {
                railwaystationData.add(dropdata.getDatavalue());
            }

            if (getlocationData != null && getlocationData.size() > 0) {
                //getCurrentLocationdetail();
                displayLocationDetail(getlocationData);
            }


        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ximgdropdownType:

                if (properytypeData != null && properytypeData.size() > 0)
                    lstproperytype.setAdapter(new LocationDetailAdapter(getActivity(), properytypeData));
                lstproperytype.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoType)));
                lstproperytype.setModal(true);
                lstproperytype.show();

                break;


            case R.id.ximgdropdownSite:

                if (siteData != null && siteData.size() > 0)
                    lstsite.setAdapter(new LocationDetailAdapter(
                            getActivity(), siteData));
                lstsite.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoSite)));
                lstsite.setModal(true);
                lstsite.show();

                break;


            case R.id.ximgdropdownclass:

                if (locationclassData != null && locationclassData.size() > 0)
                    lstpopupclass.setAdapter(new LocationDetailAdapter(
                            getActivity(), locationclassData));
                lstpopupclass.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoclass)));
                lstpopupclass.setModal(true);
                lstpopupclass.show();

                break;


            case R.id.ximgdropdownview:

                if (viewtypeData != null && viewtypeData.size() > 0)
                    lstViewtype.setAdapter(new LocationDetailAdapter(
                            getActivity(), viewtypeData));
                lstViewtype.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoview)));
                lstViewtype.setModal(true);
                lstViewtype.show();

                break;

            case R.id.ximgdropdownNearestbusstop:

                if (busStopData != null && busStopData.size() > 0)
                    lstBusStop.setAdapter(new LocationDetailAdapter(
                            getActivity(), busStopData));
                lstBusStop.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestbusstop)));
                lstBusStop.setModal(true);
                lstBusStop.show();

                break;


            case R.id.ximgdropdownautoavailable:

                if (autoavailableData != null && autoavailableData.size() > 0)
                    lstAutoavailable.setAdapter(new LocationDetailAdapter(
                            getActivity(), autoavailableData));
                lstAutoavailable.setAnchorView(((TextView) view.findViewById(R.id.xedtautoavailable)));
                lstAutoavailable.setModal(true);
                lstAutoavailable.show();

                break;

            case R.id.ximgdropdownNearestrailwaystation:

                if (railwaystationData != null && railwaystationData.size() > 0)
                    lstRailwaystation.setAdapter(new LocationDetailAdapter(
                            getActivity(), railwaystationData));
                lstRailwaystation.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)));
                lstRailwaystation.setModal(true);
                lstRailwaystation.show();

                break;


            case R.id.ximgdropdowndistanceMins:

                if (distanceBusData != null && distanceBusData.size() > 0)
                    lstBusdistance.setAdapter(new LocationDetailAdapter(
                            getActivity(), distanceBusData));
                lstBusdistance.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceBusMins)));
                lstBusdistance.setModal(true);
                lstBusdistance.show();

                break;


            case R.id.ximgdropdowndistanceKms:

                if (distancerailwaystationData != null && distancerailwaystationData.size() > 0)
                    lstRailwayDistance.setAdapter(new LocationDetailAdapter(
                            getActivity(), distancerailwaystationData));
                lstRailwayDistance.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceRailwayKms)));
                lstRailwayDistance.setModal(true);
                lstRailwayDistance.show();

                break;

            case R.id.ximgdropdowndistancehosKms:

                if (distancerailwaystationData != null && distancerailwaystationData.size() > 0)
                    lsthospitaldistance.setAdapter(new LocationDetailAdapter(
                            getActivity(), distancerailwaystationData));
                lsthospitaldistance.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautodistancehosKms)));
                lsthospitaldistance.setModal(true);
                lsthospitaldistance.show();

                break;


            case R.id.xbtnsave:

                if (validateForm()) {
                    if (GetWebServiceData.isNetworkAvailable(getActivity())) {
                        new SetLocationAsyncTask().execute();
                    } else {
                        List<GetLocationResponse> locationTab = null;
                        try {
                            locationTab = new ArrayList<GetLocationResponse>();
                        } catch (Exception e) {
                        }
                        locationTab.add(new GetLocationResponse(AppSetting.getRequestId(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoType)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoclass)).getText().toString(),
                                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoSite)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoview)).getText().toString(), ((EditText) view.findViewById(R.id.xedtNorthName)).getText().toString(), ((EditText) view.findViewById(R.id.xedtsouth)).getText().toString(),
                                ((EditText) view.findViewById(R.id.xedtwest)).getText().toString(), ((EditText) view.findViewById(R.id.xedtEastName)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceRailwayKms)).getText().toString(),
                                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestbusstop)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceBusMins)).getText().toString(), ((TextView) view.findViewById(R.id.xedtautoavailable)).getText().toString(), ((TextView) view.findViewById(R.id.xedtLatitude)).getText().toString(),
                                ((TextView) view.findViewById(R.id.xedtlongitude)).getText().toString(), ((EditText) view.findViewById(R.id.xedtNearesthospital)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistancehosKms)).getText().toString()));

                        if (locationTab != null && locationTab.size() > 0) {
                            displayOfflineSaveDataDialog(locationTab);
                        }
			/*AppCommonDialog.showSimpleDialog(getActivity(),
					getResources().getString(R.string.app_name),
					getResources().getString(R.string.check_network),
					getResources().getString(R.string.ok), "OK");*/
                    }
                }

                break;

            case R.id.xedtlongitude:

                if (String.valueOf(longitude) != null) {
                    ((TextView) view.findViewById(R.id.xedtlongitude)).setText(String.valueOf(longitude));
                    AppSetting.setUserLongitude(String.valueOf(longitude));
                } else {
                    getCurrentLocationdetail();
                    ((TextView) view.findViewById(R.id.xedtlongitude)).setText(String.valueOf(longitude));
                    AppSetting.setUserLongitude(String.valueOf(longitude));

                }

                break;

            case R.id.xedtLatitude:
                if (String.valueOf(latitude) != null) {
                    ((TextView) view.findViewById(R.id.xedtLatitude)).setText(String.valueOf(latitude));
                    AppSetting.setUserLatitude(String.valueOf(latitude));

                } else {
                    getCurrentLocationdetail();
                    ((TextView) view.findViewById(R.id.xedtLatitude)).setText(String.valueOf(latitude));
                    AppSetting.setUserLatitude(String.valueOf(latitude));

                }

                break;

            default:
                break;
        }

    }

    private void displayOfflineSaveDataDialog(final List<GetLocationResponse> locationTab) {
		/*final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dailog_box);
		TextView textTitle = (TextView) dialog.findViewById(R.id.xtxttitle);
		textTitle.setText("");
		// set the custom dialog components - text, image and button
		String strmessage=getResources().getString(R.string.savelocal_datamessage);
		((TextView)dialog.findViewById(R.id.xtxtmessage)).setText(strmessage);
		

		Button buttonOk = (Button) dialog.findViewById(R.id.xbtnok);
		Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
		  buttonOk.setText(getResources().getString(R.string.ok).toUpperCase());
		  btnCancel.setText(getResources().getString(R.string.cancel).toUpperCase());
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		// if button is clicked, close the custom dialog
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Gson gson = new Gson();
				String strRequest = "";
				LocationDetailsResponse data=new LocationDetailsResponse();
				  data.setRemarks("yes");
				  data.setStatus("sucessfully");
				  data.setValues(locationTab);
				
				strRequest = gson.toJson(data);
			boolean bupdate= SecondScreenActivity.mypcplData.updateLocationdata(AppSetting.getRequestId(), strRequest, strRequest);
			if(bupdate){
				Toast.makeText(getActivity(),
						"sucessfully store data  ",
						Toast.LENGTH_SHORT).show();
				updateandDisplayofflineData("");
				AppSetting.setUpdateOfflinedata(true);
			}
				dialog.dismiss();
			}

		});
		dialog.show();*/

        Gson gson = new Gson();
        String strRequest = "";
        LocationDetailsResponse data = new LocationDetailsResponse();
        data.setRemarks("yes");
        data.setStatus("successfully");
        data.setValues(locationTab);
        strRequest = gson.toJson(data);
        boolean bupdate = SecondScreenActivity.mypcplData.updateLocationdata(AppSetting.getRequestId(), strRequest, strRequest);
        if (bupdate) {
            Toast.makeText(getActivity(), "successfully Update offline data ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData(AppSetting.getRequestId());
            AppSetting.setUpdateOfflinedata(true);
        } else if (!bupdate) {
            SecondScreenActivity.mypcplData.insertdata(strRequest, AppSetting.getRequestId());
            Toast.makeText(getActivity(), "successfully store data offline  ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData(AppSetting.getRequestId());
        }

    }


    private List<NameValuePair> saveSetdataoffline(
            List<NameValuePair> nameValuePairs) {
        if (AppSetting.getRequestId() != null) {

            nameValuePairs.add(new BasicNameValuePair("RequestId", AppSetting.getRequestId()));
            nameValuePairs.add(new BasicNameValuePair("LCTYPE", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoType)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("CLASS", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoclass)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("SITENAME", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoSite)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("VIEWNAME", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoview)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("North", ((EditText) view.findViewById(R.id.xedtNorthName)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("South", ((EditText) view.findViewById(R.id.xedtsouth)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("West", ((EditText) view.findViewById(R.id.xedtwest)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("East", ((EditText) view.findViewById(R.id.xedtEastName)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("NearRailWayStation", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("DistancesRailway", ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceRailwayKms)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("NearBusStop", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestbusstop)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("DistancesBus", ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistanceBusMins)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("AutoAvailability", ((TextView) view.findViewById(R.id.xedtautoavailable)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Lattitude", ((TextView) view.findViewById(R.id.xedtLatitude)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Longitude", ((TextView) view.findViewById(R.id.xedtlongitude)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Hospital", ((EditText) view.findViewById(R.id.xedtNearesthospital)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Distancehosp", ((AutoCompleteTextView) view.findViewById(R.id.xedtautodistancehosKms)).getText().toString()));
        }
        return nameValuePairs;
    }


    private boolean validateForm() {
        boolean valid = true;

        if (((EditText) view.findViewById(R.id.xedtautoType)).getText()
                .toString().length() <= 0) {
            // AppCommonDialog.setErrorMsg("Please Enter Name",
            // ((EditText)view.findViewById(R.id.xedtEnginnerName)), true);
            ((EditText) view.findViewById(R.id.xedtautoType))
                    .setError("Please Enter Location Type");
            valid = false;
        } else if (((EditText) view.findViewById(R.id.xedtautoclass))
                .getText().toString().trim().length() <= 0) {
            // AppCommonDialog.setErrorMsg("Please Enter Customer Name",
            // ((EditText)view.findViewById(R.id.xedtCustomerName)), true);
            ((EditText) view.findViewById(R.id.xedtautoclass))
                    .setError("Please Enter Locality or Class");
            valid = false;

        } else if (((EditText) view.findViewById(R.id.xedtautoSite)).getText()
                .toString().length() <= 0) {
            AppCommonDialog.setErrorMsg("Please Enter Site Name",
                    ((EditText) view.findViewById(R.id.xedtautoSite)), true);
            valid = false;
        } else if (((TextView) view.findViewById(R.id.xedtautoview)).getText()
                .toString().trim().length() <= 0) {
            ((TextView) view.findViewById(R.id.xedtautoview))
                    .setError("Please Enter View Name");
            valid = false;

        }


        return (valid);
    }


    // Requestdetail Task
    public class SetLocationAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                loadingDialog = new ProgressDialog(getActivity());
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
            List<NameValuePair> nameValuePairs = null;

            try {
                nameValuePairs = new ArrayList<NameValuePair>(22);
            } catch (Exception e) {

            }

            nameValuePairs = saveSetdataoffline(nameValuePairs);

            //Log.d("request", +);
            return GetWebServiceData.getServerResponse(url
                    + "/SetLocationTabDetails", nameValuePairs, data);

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

                    CommonResponse serverResponseRequest = new Gson()
                            .fromJson(result, CommonResponse.class);

                    String loginResult = serverResponsere.getString("status");
                    if (loginResult.equals("OK")) {
                        Log.d("reuest detail", "get data");
                        Toast.makeText(getActivity(), "sucessfully " + loginResult, Toast.LENGTH_SHORT).show();
								/*if (serverResponseRequest != null&& serverResponseRequest.getStatus()!=null) {
									AppCommonDialog.showSimpleDialog(getActivity(),
											getResources().getString(R.string.app_name),"Data Update Sucessfully",
											getResources().getString(R.string.ok), "OK");
								}*/
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {

        if (locationArr != null && locationArr.length > 0) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.select_dialog_item, locationArr);
            //Used to specify minimum number of
            //characters the user has to type in order to display the drop down hint.
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).setThreshold(1);
            //Setting adapter
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNearestrailwaystation)).setAdapter(arrayAdapter);

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

}