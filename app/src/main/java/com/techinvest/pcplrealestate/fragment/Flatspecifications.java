package com.techinvest.pcplrealestate.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.techinvest.pcpl.adapter.LocationDetailAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.BuildingResponse;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.FlatSpecificationResponse;
import com.techinvest.pcpl.model.GetBuildingDetailsResponse;
import com.techinvest.pcpl.model.GetFlatSpecificationDetailsResponse;
import com.techinvest.pcpl.model.LocationDetailsResponse;
import com.techinvest.pcpl.model.Offlinedatamodel;
import com.techinvest.pcpl.model.RequestDropdownData;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.SecondScreenActivity;
import com.techinvest.pcplrealestate.fragment.Buildingdetails.BuildingDetailAsyncTask;
import com.techinvest.pcplrealestate.fragment.Buildingdetails.SetBuildingAsyncTask;

@SuppressLint("NewApi")
public class Flatspecifications extends Fragment implements AppConstants, OnClickListener {
    private View view;
    Activity activity;
    String url = AppSetting.getapiURL();
    private ProgressDialog loadingDialog;
    List<GetFlatSpecificationDetailsResponse> getFlatData;

    ListPopupWindow lstcompoundWall;
    ListPopupWindow lstelectrification;
    ListPopupWindow lstOpenSpacePavement;
    ListPopupWindow lstPlumbingType;
    ListPopupWindow lstRoofingType;


    List<String> compoundWallData;
    List<String> electrificationData;
    List<String> OpenSpacePavementData;
    List<String> PlumbingTypeData;
    List<String> RoofingTypeData;
    List<Offlinedatamodel> offlineData;
    List<SetOfflinedata> flatDataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.flatspecifications_fragment, container, false);

        getFlatData = new ArrayList<GetFlatSpecificationDetailsResponse>();
        compoundWallData = new ArrayList<String>();
        OpenSpacePavementData = new ArrayList<String>();
        PlumbingTypeData = new ArrayList<String>();
        RoofingTypeData = new ArrayList<String>();
        electrificationData = new ArrayList<String>();

        lstcompoundWall = new ListPopupWindow(getActivity());
        lstelectrification = new ListPopupWindow(getActivity());
        lstOpenSpacePavement = new ListPopupWindow(getActivity());
        lstPlumbingType = new ListPopupWindow(getActivity());
        lstRoofingType = new ListPopupWindow(getActivity());


        initview(view);
        offlineData = new ArrayList<Offlinedatamodel>();
        flatDataList = new ArrayList<SetOfflinedata>();
        if (offlineData != null) {
            offlineData = SecondScreenActivity.mypcplData.getFlatDetail(AppSetting.getRequestId());
        }

        if (GetWebServiceData.isNetworkAvailable(getActivity())) {
            new FlatsTabDetailAsyncTask().execute();

        } else {
            if (offlineData.size() > 0) {
                for (Offlinedatamodel datalocal : offlineData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestid().equals(AppSetting.getRequestId())) {
                        FlatSpecificationResponse serverResponseRequest = new Gson()
                                .fromJson(datalocal.getResponsejson(), FlatSpecificationResponse.class);
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


        lstcompoundWall.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //if(parent.getItemAtPosition(position).toString()!=null)
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautocompoundwall)).setText(selecteditem);
                lstcompoundWall.dismiss();

            }
        });


        lstelectrification.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //if(parent.getItemAtPosition(position).toString()!=null)
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoElectrification)).setText(selecteditem);
                lstelectrification.dismiss();

            }
        });


        lstOpenSpacePavement.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOpenspacepavement)).setText(selecteditem);
                lstOpenSpacePavement.dismiss();

            }
        });

        lstPlumbingType.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoPlubmingType)).setText(selecteditem);
                lstPlumbingType.dismiss();

            }
        });

        lstRoofingType.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoRoofingType)).setText(selecteditem);
                lstRoofingType.dismiss();

            }
        });

        return view;
    }


    private void updateandDisplayofflineData(String RequestID) {
        flatDataList = SecondScreenActivity.mypcplData.getFlatAllvalues(RequestID);
        if (flatDataList != null && flatDataList.size() > 0) {
            for (SetOfflinedata dataOffline : flatDataList) {
                if (dataOffline.getSaveuserdata() != null && dataOffline.getSaveuserdata().length() > 0) {
                    FlatSpecificationResponse serverResponseRequest = new Gson()
                            .fromJson(dataOffline.getSaveuserdata(), FlatSpecificationResponse.class);
                    if (getFlatData != null && getFlatData.size() > 0)
                        getFlatData.clear();
                    for (GetFlatSpecificationDetailsResponse requestdata : serverResponseRequest
                            .getValues()) {
                        getFlatData.add(requestdata);
                    }
                }
            }
            if (getFlatData != null && getFlatData.size() > 0) {
                for (GetFlatSpecificationDetailsResponse datalocal : getFlatData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestId().equals(AppSetting.getRequestId())) {
                        displayFlatSpecificationDetail(getFlatData);
                    }
                }
            }
        }

    }


    private void initview(View v) {
        // TODO Auto-generated method stub
        v.findViewById(R.id.ximgdropdownPlubmingType).setOnClickListener(this);

        v.findViewById(R.id.ximgdropdownOpenspacepavement).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdowncompoundwall).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownRoofingType).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownElectrification).setOnClickListener(this);
        v.findViewById(R.id.xbtnSave).setOnClickListener(this);

    }


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;

    }


    // Requestdetail Task
    public class FlatsTabDetailAsyncTask extends AsyncTask<String, Void, String> {

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
                    + "/GetFlatTabDetails", nameValuePairs, data);

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
                    FlatSpecificationResponse serverResponseRequest = new Gson()
                            .fromJson(result, FlatSpecificationResponse.class);

                    String loginResult = serverResponsere.getString("status");
                    if (loginResult.equals("OK")) {
                        Log.d("reuest detail", "get data");
                        if (result != null && AppSetting.getRequestId() != null) {
                            //	 SecondScreenActivity.mypcplData.insertFlatdata(result, AppSetting.getRequestId());

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


    public void displayFlatSpecificationDetail(List<GetFlatSpecificationDetailsResponse> getFlatData) {
        // TODO Auto-generated method stub
        if (getFlatData != null && getFlatData.size() > 0) {
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoPlubmingType)).setText(getFlatData.get(0).getPlumbingType());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautocompoundwall)).setText(getFlatData.get(0).getCompoundWall());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoElectrification)).setText(getFlatData.get(0).getElectrificationType());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoRoofingType)).setText(getFlatData.get(0).getRoofingType());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOpenspacepavement)).setText(getFlatData.get(0).getOpenSpacePavement());

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautonoOfGate)).setText(getFlatData.get(0).getNumberOfGates());
            ((EditText) view.findViewById(R.id.xedtnumberofcarparks)).setText(getFlatData.get(0).getNoOfcarPark());
            ((EditText) view.findViewById(R.id.xedtcarparkvalue)).setText(getFlatData.get(0).getCarParkValue());
            ((EditText) view.findViewById(R.id.xedtRemark)).setText(getFlatData.get(0).getRemarks1());


            if (getFlatData.get(0).getSwimmingPool() != null && getFlatData.get(0).getSwimmingPool().equals("Y")) {

                ((CheckBox) view.findViewById(R.id.checkSwimmingPool)).setChecked(true);
            }

            if (getFlatData.get(0).getGarden() != null && getFlatData.get(0).getGarden().equals("Y")) {

                ((CheckBox) view.findViewById(R.id.checkBoxGarden)).setChecked(true);
            }

            if (getFlatData.get(0).getGym() != null && getFlatData.get(0).getGym().equals("Y")) {

                ((CheckBox) view.findViewById(R.id.checkBoxGym)).setChecked(true);
            }

            if (getFlatData.get(0).getCarPark() != null && getFlatData.get(0).getCarPark().equals("Y")) {

                ((CheckBox) view.findViewById(R.id.checkBoxCarpark)).setChecked(true);
            }

            if (getFlatData.get(0).getClubHouse().equals("Y")) {

                ((CheckBox) view.findViewById(R.id.checkBoxClubhouse)).setChecked(true);
            }


        }
        try {
            setAutoFillData();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error :" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void setAutoFillData() {
        String[] tmp1 = new String[OpenSpacePavementData.size()];
        tmp1 = OpenSpacePavementData.toArray(tmp1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp1);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoPlubmingType)).setAdapter(adapter1);

        String[] tmp2 = new String[compoundWallData.size()];
        tmp2 = compoundWallData.toArray(tmp2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp2);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautocompoundwall)).setAdapter(adapter2);

        String[] tmp3 = new String[electrificationData.size()];
        tmp3 = electrificationData.toArray(tmp3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp3);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoElectrification)).setAdapter(adapter3);

        String[] tmp4 = new String[RoofingTypeData.size()];
        tmp4 = RoofingTypeData.toArray(tmp4);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp4);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoRoofingType)).setAdapter(adapter4);

        String[] tmp5 = new String[PlumbingTypeData.size()];
        tmp5 = PlumbingTypeData.toArray(tmp5);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp5);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOpenspacepavement)).setAdapter(adapter5);
    }


    public void displaydataOffOn(FlatSpecificationResponse serverResponseRequest) {
        if (serverResponseRequest != null && serverResponseRequest.getValues() != null
                && serverResponseRequest.getCompoundWall() != null && serverResponseRequest.getElectrification() != null && serverResponseRequest.getOpenSpacePavement() != null && serverResponseRequest.getPlumbingType() != null
                && serverResponseRequest.getRoofingType() != null) {

            for (GetFlatSpecificationDetailsResponse flatdata : serverResponseRequest.getValues()) {
                getFlatData.add(flatdata);
            }
            for (RequestDropdownData dropdata : serverResponseRequest
                    .getCompoundWall()) {
                compoundWallData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getElectrification()) {

                electrificationData.add(dropdata.getDatavalue());
            }
            for (RequestDropdownData dropdata : serverResponseRequest
                    .getOpenSpacePavement()) {

                OpenSpacePavementData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getPlumbingType()) {

                PlumbingTypeData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getRoofingType()) {

                RoofingTypeData.add(dropdata.getDatavalue());
            }


            if (getFlatData != null && getFlatData.size() > 0) {
                displayFlatSpecificationDetail(getFlatData);
            }


        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ximgdropdownPlubmingType:

                if (OpenSpacePavementData != null && OpenSpacePavementData.size() > 0)
                    lstPlumbingType.setAdapter(new LocationDetailAdapter(
                            getActivity(), OpenSpacePavementData));
                lstPlumbingType.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoPlubmingType)));
                lstPlumbingType.setModal(true);
                lstPlumbingType.show();

                break;


            case R.id.ximgdropdowncompoundwall:

                if (compoundWallData != null && compoundWallData.size() > 0)
                    lstcompoundWall.setAdapter(new LocationDetailAdapter(
                            getActivity(), compoundWallData));
                lstcompoundWall.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautocompoundwall)));
                lstcompoundWall.setModal(true);
                lstcompoundWall.show();

                break;


            case R.id.ximgdropdownRoofingType:

                if (RoofingTypeData != null && RoofingTypeData.size() > 0)
                    lstRoofingType.setAdapter(new LocationDetailAdapter(
                            getActivity(), RoofingTypeData));
                lstRoofingType.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoRoofingType)));
                lstRoofingType.setModal(true);
                lstRoofingType.show();

                break;


            case R.id.ximgdropdownElectrification:

                if (electrificationData != null && electrificationData.size() > 0)
                    lstelectrification.setAdapter(new LocationDetailAdapter(
                            getActivity(), electrificationData));
                lstelectrification.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoElectrification)));
                lstelectrification.setModal(true);
                lstelectrification.show();

                break;


            case R.id.ximgdropdownOpenspacepavement:

                if (PlumbingTypeData != null && PlumbingTypeData.size() > 0)
                    lstOpenSpacePavement.setAdapter(new LocationDetailAdapter(
                            getActivity(), PlumbingTypeData));
                lstOpenSpacePavement.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoOpenspacepavement)));
                lstOpenSpacePavement.setModal(true);
                lstOpenSpacePavement.show();

                break;

            case R.id.checkSwimmingPool:

                break;

            case R.id.checkBoxGarden:

                break;

            case R.id.checkBoxGym:

                break;

            case R.id.checkBoxClubhouse:

                break;
            case R.id.checkBoxCarpark:

                break;

            case R.id.xbtnSave:

                if (validateForm()) {
                    if (GetWebServiceData.isNetworkAvailable(getActivity())) {

                        new SetFlatSpecificationAsyncTask().execute();

                    } else {

                        //List<NameValuePair> nameValuePairs = null;
                        List<GetFlatSpecificationDetailsResponse> flatTab = null;
                        String swimmingPool;
                        String gardenChk;
                        String gymChk;
                        String clubHouseChk;
                        String carParkChk;
                        try {
                            //nameValuePairs = new ArrayList<NameValuePair>(1);
                            flatTab = new ArrayList<GetFlatSpecificationDetailsResponse>();
                        } catch (Exception e) {
                        }

                        if (((CheckBox) view.findViewById(R.id.checkSwimmingPool)).isChecked()) {
                            swimmingPool = "Y";
                        } else {
                            swimmingPool = "N";
                        }

                        if (((CheckBox) view.findViewById(R.id.checkBoxGarden)).isChecked()) {
                            gardenChk = "Y";
                        } else {
                            gardenChk = "N";
                        }


                        if (((CheckBox) view.findViewById(R.id.checkBoxGym)).isChecked()) {
                            gymChk = "Y";
                        } else {
                            gymChk = "N";
                        }


                        if (((CheckBox) view.findViewById(R.id.checkBoxClubhouse)).isChecked()) {
                            clubHouseChk = "Y";
                        } else {
                            clubHouseChk = "N";
                        }

                        if (((CheckBox) view.findViewById(R.id.checkBoxCarpark)).isChecked()) {
                            carParkChk = "Y";
                        } else {
                            carParkChk = "N";
                        }
                        //nameValuePairs=saveSetdataoffline(nameValuePairs);
                        flatTab.add(new GetFlatSpecificationDetailsResponse(AppSetting.getRequestId(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoRoofingType)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoPlubmingType)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoElectrification)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautocompoundwall)).getText().toString(),
                                ((AutoCompleteTextView) view.findViewById(R.id.xedtautonoOfGate)).getText().toString(), swimmingPool, gymChk, gardenChk, clubHouseChk, carParkChk, ((EditText) view.findViewById(R.id.xedtnumberofcarparks)).getText().toString(), ((EditText) view.findViewById(R.id.xedtcarparkvalue)).getText().toString(),
                                ((EditText) view.findViewById(R.id.xedtRemark)).getText().toString(), ((EditText) view.findViewById(R.id.xedtRemark)).getText().toString(), ((EditText) view.findViewById(R.id.xedtRemark)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOpenspacepavement)).getText().toString()));

                        if (flatTab != null && flatTab.size() > 0) {
                            displayOfflineSaveDataDialog(flatTab);
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


    private void displayOfflineSaveDataDialog(final List<GetFlatSpecificationDetailsResponse> flatTab) {
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
                FlatSpecificationResponse data = new FlatSpecificationResponse();
                data.setRemarks("yes");
                data.setStatus("sucessfully");
                data.setValues(flatTab);
                strRequest = gson.toJson(data);
                boolean bupdate = SecondScreenActivity.mypcplData.updateFlatdata(AppSetting.getRequestId(), strRequest, strRequest);
                if (bupdate) {
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
        FlatSpecificationResponse data = new FlatSpecificationResponse();
        data.setRemarks("yes");
        data.setStatus("sucessfully");
        data.setValues(flatTab);
        strRequest = gson.toJson(data);
        boolean bupdate = SecondScreenActivity.mypcplData.updateFlatdata(AppSetting.getRequestId(), strRequest, strRequest);
        if (bupdate) {
            Toast.makeText(getActivity(), "sucessfully Update offline data ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData(AppSetting.getRequestId());
            AppSetting.setUpdateOfflinedata(true);
        } else if (!bupdate) {
            SecondScreenActivity.mypcplData.insertFlatdata(strRequest, AppSetting.getRequestId());
            Toast.makeText(getActivity(), "sucessfully store data offline  ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData(AppSetting.getRequestId());
        }


    }


    private List<NameValuePair> saveSetdataoffline(
            List<NameValuePair> nameValuePairs) {
        if (AppSetting.getRequestId() != null) {

            nameValuePairs.add(new BasicNameValuePair("RequestId", AppSetting.getRequestId()));
            nameValuePairs.add(new BasicNameValuePair("RoofingType", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoRoofingType)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("PlumbingType", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoPlubmingType)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("ElectrificationType", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoElectrification)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("CompoundWall", ((AutoCompleteTextView) view.findViewById(R.id.xedtautocompoundwall)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("NumberOfGates", ((AutoCompleteTextView) view.findViewById(R.id.xedtautonoOfGate)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("OpenSpacePavement", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOpenspacepavement)).getText().toString()));

            if (((CheckBox) view.findViewById(R.id.checkSwimmingPool)).isChecked()) {
                nameValuePairs.add(new BasicNameValuePair("SwimmingPool", "Y"));
            } else {
                nameValuePairs.add(new BasicNameValuePair("SwimmingPool", "N"));
            }

            if (((CheckBox) view.findViewById(R.id.checkBoxGarden)).isChecked()) {
                nameValuePairs.add(new BasicNameValuePair("garden", "Y"));
            } else {
                nameValuePairs.add(new BasicNameValuePair("garden", "N"));
            }


            if (((CheckBox) view.findViewById(R.id.checkBoxGym)).isChecked()) {
                nameValuePairs.add(new BasicNameValuePair("Gym", "Y"));
            } else {
                nameValuePairs.add(new BasicNameValuePair("Gym", "N"));
            }


            if (((CheckBox) view.findViewById(R.id.checkBoxClubhouse)).isChecked()) {
                nameValuePairs.add(new BasicNameValuePair("ClubHouse", "Y"));
            } else {
                nameValuePairs.add(new BasicNameValuePair("ClubHouse", "N"));
            }

            if (((CheckBox) view.findViewById(R.id.checkBoxCarpark)).isChecked()) {
                nameValuePairs.add(new BasicNameValuePair("CarPark", "Y"));
            } else {
                nameValuePairs.add(new BasicNameValuePair("CarPark", "N"));
            }

            nameValuePairs.add(new BasicNameValuePair("NoOfcarPark", ((EditText) view.findViewById(R.id.xedtnumberofcarparks)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("CarParkValue", ((EditText) view.findViewById(R.id.xedtcarparkvalue)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Remarks1", ((EditText) view.findViewById(R.id.xedtRemark)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Remarks2", ((EditText) view.findViewById(R.id.xedtRemark)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Remarks3", ((EditText) view.findViewById(R.id.xedtRemark)).getText().toString()));


        }

        return nameValuePairs;
    }


    private boolean validateForm() {
        boolean valid = true;

        if (((EditText) view.findViewById(R.id.xedtautoRoofingType)).getText()
                .toString().length() <= 0) {
            // AppCommonDialog.setErrorMsg("Please Enter Name",
            // ((EditText)view.findViewById(R.id.xedtEnginnerName)), true);
            ((EditText) view.findViewById(R.id.xedtautoRoofingType))
                    .setError("Please Enter Roofing Type");
            valid = false;
        } else if (((EditText) view.findViewById(R.id.xedtautoPlubmingType))
                .getText().toString().trim().length() <= 0) {
            // AppCommonDialog.setErrorMsg("Please Enter Customer Name",
            // ((EditText)view.findViewById(R.id.xedtCustomerName)), true);
            ((EditText) view.findViewById(R.id.xedtautoPlubmingType))
                    .setError("Please Enter Plumbing Type");
            valid = false;

        } else if (((EditText) view.findViewById(R.id.xedtautoElectrification)).getText()
                .toString().length() <= 0) {
            AppCommonDialog.setErrorMsg("Please Enter Electrification",
                    ((EditText) view.findViewById(R.id.xedtautoElectrification)), true);
            valid = false;
        } else if (((TextView) view.findViewById(R.id.xedtautocompoundwall)).getText()
                .toString().trim().length() <= 0) {
            ((TextView) view.findViewById(R.id.xedtautocompoundwall))
                    .setError("Please Enter Compound Wall");
            valid = false;

        }


        return (valid);

    }


    // Requestdetail Task
    public class SetFlatSpecificationAsyncTask extends AsyncTask<String, Void, String> {

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
                    + "/SetFlatTabDetails", nameValuePairs, data);

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
}
	

