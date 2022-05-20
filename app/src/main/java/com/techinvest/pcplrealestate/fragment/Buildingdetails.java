package com.techinvest.pcplrealestate.fragment;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.techinvest.pcpl.adapter.LocationDetailAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.BuildingResponse;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.GetBuildingDetailsResponse;
import com.techinvest.pcpl.model.GetLocationResponse;
import com.techinvest.pcpl.model.LocationDetailsResponse;
import com.techinvest.pcpl.model.Offlinedatamodel;
import com.techinvest.pcpl.model.ProjectTabResponse;
import com.techinvest.pcpl.model.RequestDropdownData;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.SecondScreenActivity;
import com.techinvest.pcplrealestate.R.layout;
import com.techinvest.pcplrealestate.fragment.Locationdetails.LocationDetailAsyncTask;
import com.techinvest.pcplrealestate.fragment.Locationdetails.SetLocationAsyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.fragment.app.Fragment;

@SuppressLint("NewApi")
public class Buildingdetails extends Fragment implements AppConstants, OnClickListener {

    private View view;
    Activity activity;
    private ProgressDialog loadingDialog;
    String url = AppSetting.getapiURL();
    List<GetBuildingDetailsResponse> getbuildingData;

    ListPopupWindow lstcivilAmerica;
    ListPopupWindow lstEcq;
    ListPopupWindow lstFloors;
    ListPopupWindow lstIcq;
    ListPopupWindow lstInfrstructureQuality;
    ListPopupWindow lstRentalValue;
    ListPopupWindow lstWaterSupply;
    ListPopupWindow lstStructureType;
    ListPopupWindow lstNoofFlatFloor;
    ListPopupWindow lstLift;
    ListPopupWindow lstWing;
    ListPopupWindow lstBuildingmaintaince;
    List<String> structureTypeData;
    List<String> civilAmericaData;
    List<String> ecqData;
    List<String> floorsData;
    List<String> icqData;
    List<String> infrstructureQualityData;
    List<String> rentalValueData;
    List<String> waterSupplyData;
    List<String> noofFlatFloor;
    List<String> buildingmaintainData;
    List<String> noofWing;
    // SharedPreferences  mPrefs;
    List<Offlinedatamodel> offlineData;
    List<SetOfflinedata> buildingDataList;
    LinearLayout externalConstructionId, internalConstructionId, buildingMaintenanceId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Bundle bundle = this.getArguments();
//        if (bundle!= null) {
//            String temp = getArguments().getString("checkedValue");
//            if (temp.equalsIgnoreCase("Developed")){
//                externalConstructionId.setVisibility(View.VISIBLE);
//                internalConstructionId.setVisibility(View.VISIBLE);
//                buildingMaintenanceId.setVisibility(View.VISIBLE);
//            }
//            else {
//                externalConstructionId.setVisibility(View.GONE);
//                internalConstructionId.setVisibility(View.GONE);
//                buildingMaintenanceId.setVisibility(View.GONE);
//            }
//        }
//        else {
//            Toast.makeText(container.getContext(), "Bundle not Working", Toast.LENGTH_LONG).show();
//        }
        view = inflater.inflate(R.layout.buildingdetails_fragment, container, false);

        getbuildingData = new ArrayList<GetBuildingDetailsResponse>();
        structureTypeData = new ArrayList<String>();
        civilAmericaData = new ArrayList<String>();
        ecqData = new ArrayList<String>();
        floorsData = new ArrayList<String>();
        icqData = new ArrayList<String>();
        infrstructureQualityData = new ArrayList<String>();
        rentalValueData = new ArrayList<String>();
        waterSupplyData = new ArrayList<String>();
        buildingmaintainData = new ArrayList<String>();

        lstStructureType = new ListPopupWindow(getActivity());
        lstcivilAmerica = new ListPopupWindow(getActivity());
        lstEcq = new ListPopupWindow(getActivity());
        lstFloors = new ListPopupWindow(getActivity());
        lstIcq = new ListPopupWindow(getActivity());
        lstInfrstructureQuality = new ListPopupWindow(getActivity());
        lstRentalValue = new ListPopupWindow(getActivity());
        lstWaterSupply = new ListPopupWindow(getActivity());
        lstNoofFlatFloor = new ListPopupWindow(getActivity());
        lstLift = new ListPopupWindow(getActivity());
        lstWing = new ListPopupWindow(getActivity());
        lstBuildingmaintaince = new ListPopupWindow(getActivity());

        noofFlatFloor = new ArrayList<String>();
        noofWing = new ArrayList<String>();

        externalConstructionId = (LinearLayout) view.findViewById(R.id.externalConstructionId);
        internalConstructionId = (LinearLayout) view.findViewById(R.id.internalConstructionId);
        buildingMaintenanceId = (LinearLayout) view.findViewById(R.id.buildingMaintenanceId);

//        Bundle bundle = this.getArguments();
//        String temp = bundle.getString("checkedValue");



        for (int i = 0; i < 150; i++) {
            noofFlatFloor.add(String.valueOf(i));
        }
        for (int i = 0; i < 100; i++) {
            noofWing.add(String.valueOf(i));
        }

        initview(view);
        offlineData = new ArrayList<Offlinedatamodel>();
        buildingDataList = new ArrayList<SetOfflinedata>();

        if (offlineData != null) {
            offlineData = SecondScreenActivity.mypcplData.getBuildingDetail(AppSetting.getRequestId());
        }
        //mPrefs = activity.getPreferences(activity.MODE_PRIVATE);
        if (GetWebServiceData.isNetworkAvailable(getActivity())) {
            new BuildingDetailAsyncTask().execute();

        } else {

            if (offlineData.size() > 0) {
                for (Offlinedatamodel datalocal : offlineData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestid().equals(AppSetting.getRequestId())) {

                        BuildingResponse serverResponseRequest = new Gson()
                                .fromJson(datalocal.getResponsejson(), BuildingResponse.class);
                        displaydataOffOn(serverResponseRequest);
                    }
                }
            }

            updateandDisplayofflineData(AppSetting.getRequestId());

            /*AppCommonDialog.showSimpleDialog(getActivity(), getResources()
                            .getString(R.string.app_name),
                    getResources().getString(R.string.check_network),
                    getResources().getString(R.string.ok), "OK");*/
        }


        lstStructureType.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //if(parent.getItemAtPosition(position).toString()!=null)
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoStructuretype)).setText(selecteditem);
                lstStructureType.dismiss();

            }
        });


        lstcivilAmerica.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //if(parent.getItemAtPosition(position).toString()!=null)
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoCivicamenities)).setText(selecteditem);
                lstcivilAmerica.dismiss();

            }
        });


        lstEcq.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalconstructionquailty)).setText(selecteditem);
                lstEcq.dismiss();

            }
        });

        lstFloors.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).setText(selecteditem);
                lstFloors.dismiss();

            }
        });

        lstIcq.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalconstruction)).setText(selecteditem);
                lstIcq.dismiss();

            }
        });


        lstInfrstructureQuality.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoqualityofinfrastructure)).setText(selecteditem);
                lstInfrstructureQuality.dismiss();

            }
        });


        lstRentalValue.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautorentalvalue)).setText(selecteditem);
                lstRentalValue.dismiss();

            }
        });

        lstWaterSupply.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautowatersupply)).setText(selecteditem);
                lstWaterSupply.dismiss();

            }
        });


        lstNoofFlatFloor.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((TextView) view.findViewById(R.id.xedtautonoofflats)).setText(selecteditem);
                lstNoofFlatFloor.dismiss();

            }
        });


        lstLift.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((TextView) view.findViewById(R.id.xedtautonooflifts)).setText(selecteditem);
                lstLift.dismiss();

            }
        });


        lstWing.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((TextView) view.findViewById(R.id.xedtautonoofwings)).setText(selecteditem);
                lstWing.dismiss();

            }
        });

        lstBuildingmaintaince.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildingmaintenance)).setText(selecteditem);
                lstBuildingmaintaince.dismiss();

            }
        });

        return view;
    }

    private void updateandDisplayofflineData(String RequestID) {
        buildingDataList = SecondScreenActivity.mypcplData.getBuildingAllvalues(RequestID);
        if (buildingDataList != null && buildingDataList.size() > 0) {
            for (SetOfflinedata dataOffline : buildingDataList) {
                if (dataOffline.getSaveuserdata() != null && dataOffline.getSaveuserdata().length() > 0) {
                    BuildingResponse serverResponseRequest = new Gson()
                            .fromJson(dataOffline.getSaveuserdata(), BuildingResponse.class);
                    if (getbuildingData != null && getbuildingData.size() > 0)
                        getbuildingData.clear();
                    for (GetBuildingDetailsResponse requestdata : serverResponseRequest
                            .getValues()) {
                        getbuildingData.add(requestdata);
                    }
                }
            }
            if (getbuildingData != null && getbuildingData.size() > 0) {
                for (GetBuildingDetailsResponse datalocal : getbuildingData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestId().equals(AppSetting.getRequestId())) {
                        displayBuldingDetail(getbuildingData);
                    }
                }
            }
        }
    }


    private void initview(View v) {
        // TODO Auto-generated method stub

        String result = AppSetting.getKeyCheckedValue();
        if (result.equalsIgnoreCase("Developed")){
            externalConstructionId.setVisibility(View.VISIBLE);
            internalConstructionId.setVisibility(View.VISIBLE);
            buildingMaintenanceId.setVisibility(View.VISIBLE);
        }
        else {
            externalConstructionId.setVisibility(View.GONE);
            internalConstructionId.setVisibility(View.GONE);
            buildingMaintenanceId.setVisibility(View.GONE);
        }

        v.findViewById(R.id.ximgdropdownStructuretype).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautoCivicamenities).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautoexternalconstructionquailty).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownfloor).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdowninternalconstruction).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautoqualityofinfrastructure).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownrentalvalue).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownwatersupply).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownnoofflats).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownnoofwings).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownnooflifts).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownbuildingmaintenance).setOnClickListener(this);
        v.findViewById(R.id.xbtnSave).setOnClickListener(this);


    }


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;

    }


    // Requestdetail Task
    public class BuildingDetailAsyncTask extends AsyncTask<String, Void, String> {

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
                    + "/GetBuildingTabDetails", nameValuePairs, data);

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
                    BuildingResponse serverResponseRequest = new Gson()
                            .fromJson(result, BuildingResponse.class);

                    String loginResult = serverResponsere.getString("status");
                    if (loginResult.equals("OK")) {
                        /* Editor prefsEditor = mPrefs.edit();
					        Gson gson = new Gson();
					        //String json = gson.toJson(requestgetData);
					        prefsEditor.putString("BuildingData", result);
					        prefsEditor.commit();*/
                        if (result != null && AppSetting.getRequestId() != null) {
                            //	 SecondScreenActivity.mypcplData.insertBuildingdata(result, AppSetting.getRequestId());

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


    public void displayBuldingDetail(List<GetBuildingDetailsResponse> getbuildingData) {
        // TODO Auto-generated method stub
        if (getbuildingData != null && getbuildingData.size() > 0) {
            ((TextView) view.findViewById(R.id.xedtautonooflifts)).setText(getbuildingData.get(0).getNoOfLift());
            ((TextView) view.findViewById(R.id.xedtautonoofflats)).setText(getbuildingData.get(0).getNoOfFlatsFloor());
            ((TextView) view.findViewById(R.id.xedtautonoofwings)).setText(getbuildingData.get(0).getNoOfWings());

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoStructuretype)).setText(getbuildingData.get(0).getStructureType());//
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).setText(getbuildingData.get(0).getFloors());//
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalconstruction)).setText(getbuildingData.get(0).getInternalConstructionQuality());//
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildingmaintenance)).setText(getbuildingData.get(0).getBuildingMaintenance());//
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautowatersupply)).setText(getbuildingData.get(0).getWaterSupply());//
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalconstructionquailty)).setText(getbuildingData.get(0).getExternalConstructionQuality());//
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautorentalvalue)).setText(getbuildingData.get(0).getRentalValue());//
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoqualityofinfrastructure)).setText(getbuildingData.get(0).getQualityOfInfrastruncture());//
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoCivicamenities)).setText(getbuildingData.get(0).getCivicAmenities());//
        }

        try {
            setAutoFillData();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error :" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void setAutoFillData() {
        String[] tmp1 = new String[structureTypeData.size()];
        tmp1 = structureTypeData.toArray(tmp1);
//	Log.e("123",structureTypeData.get(1).toString());
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp1);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoStructuretype)).setAdapter(adapter1);

        String[] tmp2 = new String[floorsData.size()];
        tmp2 = floorsData.toArray(tmp2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp2);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).setAdapter(adapter2);

        String[] tmp3 = new String[icqData.size()];
        tmp3 = icqData.toArray(tmp3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp3);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalconstruction)).setAdapter(adapter3);

        String[] tmp4 = new String[buildingmaintainData.size()];
        tmp4 = buildingmaintainData.toArray(tmp4);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp4);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildingmaintenance)).setAdapter(adapter4);

        String[] tmp5 = new String[waterSupplyData.size()];
        tmp5 = waterSupplyData.toArray(tmp5);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp5);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautowatersupply)).setAdapter(adapter5);

        String[] tmp6 = new String[ecqData.size()];
        tmp6 = ecqData.toArray(tmp6);
        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp6);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalconstructionquailty)).setAdapter(adapter6);

        String[] tmp7 = new String[rentalValueData.size()];
        tmp7 = rentalValueData.toArray(tmp7);
        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp7);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautorentalvalue)).setAdapter(adapter7);

        String[] tmp8 = new String[infrstructureQualityData.size()];
        tmp8 = infrstructureQualityData.toArray(tmp8);
        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp8);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoqualityofinfrastructure)).setAdapter(adapter8);

        String[] tmp9 = new String[civilAmericaData.size()];
        tmp9 = civilAmericaData.toArray(tmp9);
        ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp9);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoCivicamenities)).setAdapter(adapter9);


    }


    public void displaydataOffOn(BuildingResponse serverResponseRequest) {
        if (serverResponseRequest != null && serverResponseRequest.getValues() != null
                && serverResponseRequest.getCivicAmenities() != null && serverResponseRequest.getECQ() != null && serverResponseRequest.getFloors() != null && serverResponseRequest.getICQ() != null
                && serverResponseRequest.getInfrstructureQuality() != null && serverResponseRequest.getRentalValue() != null && serverResponseRequest.getStructureType() != null) {

            for (GetBuildingDetailsResponse buindingdata : serverResponseRequest.getValues()) {
                getbuildingData.add(buindingdata);
            }
            for (RequestDropdownData dropdata : serverResponseRequest
                    .getCivicAmenities()) {
                civilAmericaData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getECQ()) {

                ecqData.add(dropdata.getDatavalue());
            }
            for (RequestDropdownData dropdata : serverResponseRequest
                    .getFloors()) {

                floorsData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getICQ()) {

                icqData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getInfrstructureQuality()) {

                infrstructureQualityData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getRentalValue()) {

                rentalValueData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getWaterSupply()) {
                waterSupplyData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getStructureType()) {
                structureTypeData.add(dropdata.getDatavalue());
            }
            for (RequestDropdownData dropdata : serverResponseRequest
                    .getBuildingMaintenance()) {
                buildingmaintainData.add(dropdata.getDatavalue());
            }

            if (getbuildingData != null && getbuildingData.size() > 0) {
                displayBuldingDetail(getbuildingData);
            }


        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ximgdropdownStructuretype:

                if (structureTypeData != null && structureTypeData.size() > 0)
                    lstStructureType.setAdapter(new LocationDetailAdapter(getActivity(), structureTypeData));
                lstStructureType.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoStructuretype)));
                lstStructureType.setModal(true);
                lstStructureType.show();

                break;


            case R.id.ximgdropdownautoCivicamenities:

                if (civilAmericaData != null && civilAmericaData.size() > 0)
                    lstcivilAmerica.setAdapter(new LocationDetailAdapter(
                            getActivity(), civilAmericaData));
                lstcivilAmerica.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoCivicamenities)));
                lstcivilAmerica.setModal(true);
                lstcivilAmerica.show();

                break;


            case R.id.ximgdropdownautoexternalconstructionquailty:

                if (ecqData != null && ecqData.size() > 0)
                    lstEcq.setAdapter(new LocationDetailAdapter(
                            getActivity(), ecqData));
                lstEcq.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalconstructionquailty)));
                lstEcq.setModal(true);
                lstEcq.show();

                break;


            case R.id.ximgdropdownfloor:

                if (floorsData != null && floorsData.size() > 0)
                    lstFloors.setAdapter(new LocationDetailAdapter(
                            getActivity(), floorsData));
                lstFloors.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)));
                lstFloors.setModal(true);
                lstFloors.show();

                break;


            case R.id.ximgdropdowninternalconstruction:

                if (icqData != null && icqData.size() > 0)
                    lstIcq.setAdapter(new LocationDetailAdapter(
                            getActivity(), icqData));
                lstIcq.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalconstruction)));
                lstIcq.setModal(true);
                lstIcq.show();

                break;

            case R.id.ximgdropdownautoqualityofinfrastructure:

                if (infrstructureQualityData != null && infrstructureQualityData.size() > 0)
                    lstInfrstructureQuality.setAdapter(new LocationDetailAdapter(
                            getActivity(), infrstructureQualityData));
                lstInfrstructureQuality.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoqualityofinfrastructure)));
                lstInfrstructureQuality.setModal(true);
                lstInfrstructureQuality.show();

                break;


            case R.id.ximgdropdownrentalvalue:

                if (rentalValueData != null && rentalValueData.size() > 0)
                    lstRentalValue.setAdapter(new LocationDetailAdapter(
                            getActivity(), rentalValueData));
                lstRentalValue.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautorentalvalue)));
                lstRentalValue.setModal(true);
                lstRentalValue.show();

                break;

            case R.id.ximgdropdownwatersupply:

                if (waterSupplyData != null && waterSupplyData.size() > 0)
                    lstWaterSupply.setAdapter(new LocationDetailAdapter(
                            getActivity(), waterSupplyData));
                lstWaterSupply.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautowatersupply)));
                lstWaterSupply.setModal(true);
                lstWaterSupply.show();

                break;


            case R.id.ximgdropdownnoofflats:

                if (noofFlatFloor != null && noofFlatFloor.size() > 0)
                    lstNoofFlatFloor.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofFlatFloor));
                lstNoofFlatFloor.setAnchorView(((TextView) view.findViewById(R.id.xedtautonoofflats)));
                lstNoofFlatFloor.setModal(true);
                lstNoofFlatFloor.show();

                break;

            case R.id.ximgdropdownnooflifts:

                if (noofWing != null && noofWing.size() > 0)
                    lstLift.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofWing));
                lstLift.setAnchorView(((TextView) view.findViewById(R.id.xedtautonooflifts)));
                lstLift.setModal(true);
                lstLift.show();

                break;

            case R.id.ximgdropdownnoofwings:

                if (noofWing != null && noofWing.size() > 0)
                    lstWing.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofWing));
                lstWing.setAnchorView(((TextView) view.findViewById(R.id.xedtautonoofwings)));
                lstWing.setModal(true);
                lstWing.show();

                break;

            case R.id.ximgdropdownbuildingmaintenance:
                if (buildingmaintainData != null && buildingmaintainData.size() > 0)
                    lstBuildingmaintaince.setAdapter(new LocationDetailAdapter(
                            getActivity(), buildingmaintainData));
                lstBuildingmaintaince.setAnchorView(((TextView) view.findViewById(R.id.xedtautobuildingmaintenance)));
                lstBuildingmaintaince.setModal(true);
                lstBuildingmaintaince.show();

                break;

            case R.id.xbtnSave:

                if (validateForm()) {
                    if (GetWebServiceData.isNetworkAvailable(getActivity())) {

                        new SetBuildingAsyncTask().execute();

                    } else {
                        List<GetBuildingDetailsResponse> buildingTab = null;
                        try {
                            buildingTab = new ArrayList<GetBuildingDetailsResponse>();
                        } catch (Exception e) {
                        }
                        buildingTab.add(new GetBuildingDetailsResponse(AppSetting.getRequestId(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoStructuretype)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).getText().toString(), ((TextView) view.findViewById(R.id.xedtautonoofflats)).getText().toString(), ((TextView) view.findViewById(R.id.xedtautonooflifts)).getText().toString(),
                                ((TextView) view.findViewById(R.id.xedtautonoofwings)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalconstruction)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalconstructionquailty)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildingmaintenance)).getText().toString(),
                                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoqualityofinfrastructure)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautowatersupply)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoCivicamenities)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautorentalvalue)).getText().toString()));

                        if (buildingTab != null && buildingTab.size() > 0) {
                            displayOfflineSaveDataDialog(buildingTab);
                        }

                        /*AppCommonDialog.showSimpleDialog(getActivity(),
                                getResources().getString(R.string.app_name),
                                getResources().getString(R.string.check_network),
                                getResources().getString(R.string.ok), "OK");*/
                    }
                }

                break;

            default:
                break;
        }

    }

    private void displayOfflineSaveDataDialog(final List<GetBuildingDetailsResponse> buildingTab) {
        /*final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dailog_box);
        TextView textTitle = (TextView) dialog.findViewById(R.id.xtxttitle);
        textTitle.setText("");
        // set the custom dialog components - text, image and button
        String strmessage = getResources().getString(R.string.savelocal_datamessage);
        ((TextView) dialog.findViewById(R.id.xtxtmessage)).setText(strmessage);


        Button buttonOk = (Button) dialog.findViewById(R.id.xbtnok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        buttonOk.setText(getResources().getString(R.string.ok).toUpperCase());
        btnCancel.setText(getResources().getString(R.string.cancel).toUpperCase());
        // btnsendemail.setText("OK");
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
                BuildingResponse data = new BuildingResponse();
                data.setRemarks("yes");
                data.setStatus("sucessfully");
                data.setValues(buildingTab);
                strRequest = gson.toJson(data);

                boolean bupdate = SecondScreenActivity.mypcplData.updateBuildingdata(AppSetting.getRequestId(), strRequest, strRequest);
                if (bupdate) {
                    Toast.makeText(getActivity(),
                            "sucessfully store data  ", Toast.LENGTH_SHORT).show();
                    updateandDisplayofflineData("");
                    AppSetting.setUpdateOfflinedata(true);
                }
                dialog.dismiss();
            }

        });
        dialog.show();*/

        Gson gson = new Gson();
        String strRequest = "";
        BuildingResponse data = new BuildingResponse();
        data.setRemarks("yes");
        data.setStatus("successfully");
        data.setValues(buildingTab);
        strRequest = gson.toJson(data);

        boolean bupdate = SecondScreenActivity.mypcplData.updateBuildingdata(AppSetting.getRequestId(), strRequest, strRequest);
        if (bupdate) {
            Toast.makeText(getActivity(), "successfully Update offline data ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData(AppSetting.getRequestId());
            AppSetting.setUpdateOfflinedata(true);
        } else if (!bupdate) {
            SecondScreenActivity.mypcplData.insertBuildingdata(strRequest, AppSetting.getRequestId());
            Toast.makeText(getActivity(), "successfully store data offline  ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData(AppSetting.getRequestId());
        }

    }


    private List<NameValuePair> saveSetdataoffline(
            List<NameValuePair> nameValuePairs) {
        if (AppSetting.getRequestId() != null) {
            if (AppSetting.getKeyCheckedValue().equalsIgnoreCase("Under Construction")){
                nameValuePairs.add(new BasicNameValuePair("RequestId", AppSetting.getRequestId()));
                nameValuePairs.add(new BasicNameValuePair("StructureType", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoStructuretype)).getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("Floors", ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("NoOfFlatsFloor", ((TextView) view.findViewById(R.id.xedtautonoofflats)).getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("NoOfLift", ((TextView) view.findViewById(R.id.xedtautonooflifts)).getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("NoOfWings", ((TextView) view.findViewById(R.id.xedtautonoofwings)).getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("InternalConstructionQuality", "NA"));
                nameValuePairs.add(new BasicNameValuePair("ExternalConstructionQuality", "NA"));
                nameValuePairs.add(new BasicNameValuePair("BuildingMaintenance", "NA"));
                nameValuePairs.add(new BasicNameValuePair("QualityOfInfrastruncture", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoqualityofinfrastructure)).getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("WaterSupply", ((AutoCompleteTextView) view.findViewById(R.id.xedtautowatersupply)).getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("CivicAmenities", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoCivicamenities)).getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("RentalValue", ((AutoCompleteTextView) view.findViewById(R.id.xedtautorentalvalue)).getText().toString()));
            }else {
                if (AppSetting.getKeyCheckedValue().equalsIgnoreCase("Developed")){
                    nameValuePairs.add(new BasicNameValuePair("RequestId", AppSetting.getRequestId()));
                    nameValuePairs.add(new BasicNameValuePair("StructureType", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoStructuretype)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Floors", ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("NoOfFlatsFloor", ((TextView) view.findViewById(R.id.xedtautonoofflats)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("NoOfLift", ((TextView) view.findViewById(R.id.xedtautonooflifts)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("NoOfWings", ((TextView) view.findViewById(R.id.xedtautonoofwings)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("InternalConstructionQuality", ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalconstruction)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("ExternalConstructionQuality", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalconstructionquailty)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("BuildingMaintenance", ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildingmaintenance)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("QualityOfInfrastruncture", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoqualityofinfrastructure)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("WaterSupply", ((AutoCompleteTextView) view.findViewById(R.id.xedtautowatersupply)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("CivicAmenities", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoCivicamenities)).getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("RentalValue", ((AutoCompleteTextView) view.findViewById(R.id.xedtautorentalvalue)).getText().toString()));

                }

            }

        }
        return nameValuePairs;
    }


    private boolean validateForm() {
        // TODO Auto-generated method stub
        boolean valid = true;

        if (AppSetting.getKeyCheckedValue().equalsIgnoreCase("Developed")){
            if (((EditText) view.findViewById(R.id.xedtautoStructuretype)).getText()
                    .toString().length() <= 0) {
                // AppCommonDialog.setErrorMsg("Please Enter Name",
                // ((EditText)view.findViewById(R.id.xedtEnginnerName)), true);
                ((EditText) view.findViewById(R.id.xedtautoStructuretype))
                        .setError("Please Enter Structure Type");
                valid = false;
            } else if (((EditText) view.findViewById(R.id.xedtautobuildingmaintenance))
                    .getText().toString().trim().length() <= 0) {
                // AppCommonDialog.setErrorMsg("Please Enter Customer Name",
                // ((EditText)view.findViewById(R.id.xedtCustomerName)), true);
                ((EditText) view.findViewById(R.id.xedtautobuildingmaintenance))
                        .setError("Please Enter Building Maintenance");
                valid = false;

            } else if (((EditText) view.findViewById(R.id.xedtautointernalconstruction)).getText()
                    .toString().length() <= 0) {
                AppCommonDialog.setErrorMsg("Please Enter Internal Construction",
                        ((EditText) view.findViewById(R.id.xedtautointernalconstruction)), true);
                valid = false;
            } else if (((TextView) view.findViewById(R.id.xedtautoexternalconstructionquailty)).getText()
                    .toString().trim().length() <= 0) {
                ((TextView) view.findViewById(R.id.xedtautoexternalconstructionquailty))
                        .setError("Please Enter External Construction");
                valid = false;

            } else if (((EditText) view.findViewById(R.id.xedtautofloor)).getText()
                    .toString().trim().length() <= 0) {
                ((EditText) view.findViewById(R.id.xedtautofloor))
                        .setError("Please Enter Floor");
                valid = false;

            }
        }
        else {
            if (AppSetting.getKeyCheckedValue().equalsIgnoreCase("Under Construction")){
                if (((EditText) view.findViewById(R.id.xedtautoStructuretype)).getText()
                        .toString().length() <= 0) {
                    // AppCommonDialog.setErrorMsg("Please Enter Name",
                    // ((EditText)view.findViewById(R.id.xedtEnginnerName)), true);
                    ((EditText) view.findViewById(R.id.xedtautoStructuretype))
                            .setError("Please Enter Structure Type");
                    valid = false;
                }
                else if (((EditText) view.findViewById(R.id.xedtautofloor)).getText()
                        .toString().trim().length() <= 0) {
                    ((EditText) view.findViewById(R.id.xedtautofloor))
                            .setError("Please Enter Floor");
                    valid = false;

                }
            }
        }

        return (valid);

    }


    // Requestdetail Task
    public class SetBuildingAsyncTask extends AsyncTask<String, Void, String> {

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
                    + "/SetBuildingTabDetails", nameValuePairs, data);

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
                        Log.d("request detail", "get data");
                        Toast.makeText(getActivity(), "successfully " + loginResult, Toast.LENGTH_SHORT).show();
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


}
