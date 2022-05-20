package com.techinvest.pcplrealestate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techinvest.pcpl.adapter.RequestDetailAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.commonutil.ObjectSerializer;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.OfflineResponse;
import com.techinvest.pcpl.model.Offlinedatamodel;
import com.techinvest.pcpl.model.RequestDataDetail;
import com.techinvest.pcpl.model.RequestDetailsResponse;
import com.techinvest.pcpl.model.RequestDropdownData;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.parserhelper.DataBaseHelper;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;

@SuppressLint("NewApi")
public class Requestdetails extends Fragment implements AppConstants, OnClickListener {
    private View view;
    Activity activity;
    private TextView text;
    private ProgressDialog loadingDialog;
    String url = AppSetting.getapiURL();
    // private ArrayList<String> searchArrayList;
    AutoCompleteTextView autoCompleteArea;
    List<RequestDataDetail> requestgetData;
    List<RequestDropdownData> requestoptionData;
    ListPopupWindow myPopupArea;
    ListPopupWindow lstpincode;
    ListPopupWindow lstPropery;
    ListPopupWindow lstoccupied;
    ListPopupWindow lstoccupSubtype;

    List<String> areaData;
    List<String> pincodeData;
    List<String> proprtyData;
    List<String> occupiedData;
    List<String> occupsubtypeData;
    SharedPreferences mPrefs;
    //DataBaseHelper mypcplData;
    List<Offlinedatamodel> offlineData;
    List<SetOfflinedata> requestDataList;

    // String reqid="05C1BB83-CB6D-4D4F-B6ED-F96AA0D557E9";

    AutoCompleteTextView autoCompletearea, autoCompletepincode,
            autoCompleteoccupysubtype;

    public Requestdetails() {
    }

    /*
     * public static Requestdetails newInstance(FragmentManager fragmentManager)
     * { Requestdetails fragment = new Requestdetails(); return fragment; }
     */

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.requestdetail_fragment, container,
                false);


        requestgetData = new ArrayList<RequestDataDetail>();
        requestoptionData = new ArrayList<RequestDropdownData>();
        areaData = new ArrayList<String>();
        pincodeData = new ArrayList<String>();
        proprtyData = new ArrayList<String>();
        occupiedData = new ArrayList<String>();
        occupsubtypeData = new ArrayList<String>();

        lstpincode = new ListPopupWindow(getActivity());
        myPopupArea = new ListPopupWindow(getActivity());
        lstPropery = new ListPopupWindow(getActivity());
        lstoccupied = new ListPopupWindow(getActivity());
        lstoccupSubtype = new ListPopupWindow(getActivity());
        initview(view);
        mPrefs = activity.getPreferences(activity.MODE_PRIVATE);
        offlineData = new ArrayList<Offlinedatamodel>();
        // for use update user data retrive
        requestDataList = new ArrayList<SetOfflinedata>();

        if (offlineData != null) {

            offlineData = SecondScreenActivity.mypcplData.getRequestDetail(AppSetting.getRequestId());
            // offlineData = (ArrayList<Offlinedatamodel>) ObjectSerializer.deserialize(mPrefs.getString("LIST", ObjectSerializer.serialize(new ArrayList<Offlinedatamodel>())));

        } else {

        }

        if (GetWebServiceData.isNetworkAvailable(getActivity())) {

            new RequestDetailAsyncTask().execute();

        } else {
		/*	//Offlinedatamodel obj = null;
			if(offlineData!=null){
				 try {
					 offlineData = (ArrayList<Offlinedatamodel>) ObjectSerializer.deserialize(mPrefs.getString("LIST", ObjectSerializer.serialize(new ArrayList<Offlinedatamodel>())));
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
				
			}*/

            if (offlineData.size() > 0) {
                for (Offlinedatamodel datalocal : offlineData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestid().equals(AppSetting.getRequestId())) {
                        RequestDetailsResponse serverResponseRequest = new Gson()
                                .fromJson(datalocal.getResponsejson(), RequestDetailsResponse.class);
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

        // AutoCompleteAdapter adapter = new AutoCompleteAdapter(getActivity(),
        // R.layout.item_autocomplete, R.id.xtxtselectName, areaData);

        // autoCompleteTextView = (AutoCompleteTextView)
        // view.findViewById(R.id.xedtautoArea);
        // autoCompleteTextView.setAdapter(adapter);

        view.findViewById(R.id.ximgdropdownArea).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (areaData != null && areaData.size() > 0)
                            myPopupArea.setAdapter(new RequestDetailAdapter(
                                    getActivity(), areaData));
                        myPopupArea.setAnchorView(((AutoCompleteTextView) view
                                .findViewById(R.id.xedtautoArea)));
                        // listPopupWindow.setWidth(300);
                        // listPopupWindow.setHeight(400);
                        myPopupArea.setModal(true);
                        myPopupArea.show();

                    }
                });

        view.findViewById(R.id.ximgdropdownPincode).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pincodeData != null && pincodeData.size() > 0)
                            lstpincode.setAdapter(new RequestDetailAdapter(
                                    getActivity(), pincodeData));
                        lstpincode.setAnchorView(((AutoCompleteTextView) view
                                .findViewById(R.id.xedtautoPincode)));
                        lstpincode.setModal(true);
                        lstpincode.show();

                    }
                });

        view.findViewById(R.id.ximgdropdownproperty).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (proprtyData != null && proprtyData.size() > 0)
                            lstPropery.setAdapter(new RequestDetailAdapter(
                                    getActivity(), proprtyData));
                        lstPropery.setAnchorView(((TextView) view
                                .findViewById(R.id.xedtautoProperty)));
                        lstPropery.setModal(true);
                        lstPropery.show();

                    }
                });

        view.findViewById(R.id.ximgdropdownOccupation).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (occupiedData != null && occupiedData.size() > 0)
                            lstoccupied.setAdapter(new RequestDetailAdapter(
                                    getActivity(), occupiedData));
                        lstoccupied.setAnchorView(((TextView) view
                                .findViewById(R.id.xedtautoOccupation)));
                        lstoccupied.setModal(true);
                        lstoccupied.show();

                    }
                });

        myPopupArea.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();

                autoCompletearea.setText(selecteditem);
                myPopupArea.dismiss();

            }
        });

        lstpincode.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                autoCompletepincode.setText(selecteditem);

                lstpincode.dismiss();

            }
        });

        lstPropery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((TextView) view.findViewById(R.id.xedtautoProperty))
                        .setText(selecteditem);

                lstPropery.dismiss();

            }
        });

        lstoccupied.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((TextView) view.findViewById(R.id.xedtautoOccupation))
                        .setText(selecteditem);

                lstoccupied.dismiss();

            }
        });

        lstoccupSubtype.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((TextView) view.findViewById(R.id.xedtautoOccupiedsubtype))
                        .setText(selecteditem);

                lstoccupSubtype.dismiss();

            }
        });

        return view;
    }

    private void updateandDisplayofflineData(String RequestID) {
        requestDataList = SecondScreenActivity.mypcplData.getRequestAllvalues(RequestID);
        if (requestDataList != null && requestDataList.size() > 0) {
            for (SetOfflinedata dataOffline : requestDataList) {
                if (dataOffline.getSaveuserdata() != null && dataOffline.getSaveuserdata().length() > 0) {
                    RequestDetailsResponse serverResponseRequest = new Gson()
                            .fromJson(dataOffline.getSaveuserdata(), RequestDetailsResponse.class);
                    if (requestgetData != null && requestgetData.size() > 0)
                        requestgetData.clear();
                    for (RequestDataDetail requestdata : serverResponseRequest.getValues()) {
                        requestgetData.add(requestdata);
                    }
                }


            }
            if (requestgetData != null && requestgetData.size() > 0) {
                for (RequestDataDetail datalocal : requestgetData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestId().equals(AppSetting.getRequestId())) {
                        displayRequestDetail(requestgetData);
                    }
                }
            }
        }

    }

    private void initview(View view) {
        autoCompletearea = (AutoCompleteTextView) view
                .findViewById(R.id.xedtautoArea);
        autoCompletepincode = (AutoCompleteTextView) view
                .findViewById(R.id.xedtautoPincode);

        view.findViewById(R.id.xbtnSubmit).setOnClickListener(this);
        view.findViewById(R.id.ximgdropdownOccupiedsub)
                .setOnClickListener(this);

    }

    private boolean validateForm() {
        boolean valid = true;

        if (((EditText) view.findViewById(R.id.xedtEnginnerName)).getText()
                .toString().length() <= 0) {
            // AppCommonDialog.setErrorMsg("Please Enter Name",
            // ((EditText)view.findViewById(R.id.xedtEnginnerName)), true);
            ((EditText) view.findViewById(R.id.xedtEnginnerName))
                    .setError("Please Enter Name");
            valid = false;
        } else if (((EditText) view.findViewById(R.id.xedtCustomerName))
                .getText().toString().trim().length() <= 0) {
            // AppCommonDialog.setErrorMsg("Please Enter Customer Name",
            // ((EditText)view.findViewById(R.id.xedtCustomerName)), true);
            ((EditText) view.findViewById(R.id.xedtCustomerName))
                    .setError("Please Enter Customer Name");
            valid = false;

        } else if (((EditText) view.findViewById(R.id.xedtOwnerName)).getText()
                .toString().length() <= 0) {
            AppCommonDialog.setErrorMsg("Please Enter Owner name",
                    ((EditText) view.findViewById(R.id.xedtOwnerName)), true);
            valid = false;
        } else if (((TextView) view.findViewById(R.id.xedtBankname)).getText()
                .toString().trim().length() <= 0) {
            ((TextView) view.findViewById(R.id.xedtBankname))
                    .setError("Please Enter Bank Name");
            valid = false;

        } else if (((EditText) view.findViewById(R.id.xedtBoardName)).getText()
                .toString().trim().length() <= 0) {
            ((EditText) view.findViewById(R.id.xedtBoardName))
                    .setError("Please Enter Board Name");
            valid = false;

        } else if (((EditText) view.findViewById(R.id.xedtContactName)).getText()
                .toString().trim().length() <= 0) {
            ((EditText) view.findViewById(R.id.xedtContactName))
                    .setError("Please Enter Contact Name");
            valid = false;

        } else if (((EditText) view.findViewById(R.id.xedtaddressFlatno))
                .getText().toString().trim().length() <= 0) {
            ((EditText) view.findViewById(R.id.xedtaddressFlatno))
                    .setError("Please Enter Flat no and Detail");
            valid = false;

        } else if (((EditText) view.findViewById(R.id.xedtAddressLandMark))
                .getText().toString().trim().length() <= 0) {
            ((EditText) view.findViewById(R.id.xedtAddressLandMark))
                    .setError("Please Enter Landmark address");
            valid = false;

        } else if (((EditText) view.findViewById(R.id.xedtaddressRoad)).getText()
                .toString().trim().length() <= 0) {
            ((EditText) view.findViewById(R.id.xedtaddressRoad))
                    .setError("Please Enter Address");
            valid = false;

        } else if (((EditText) view.findViewById(R.id.xedtLocationArea))
                .getText().toString().trim().length() <= 0) {
            ((EditText) view.findViewById(R.id.xedtLocationArea))
                    .setError("Please Enter Location and Area");
            valid = false;

        } else if (((EditText) view.findViewById(R.id.xedtPersonmet)).getText()
                .toString().trim().length() <= 0) {
            ((EditText) view.findViewById(R.id.xedtPersonmet))
                    .setError("Please Enter PersonMet Name");
            valid = false;

        } else if (((AutoCompleteTextView) view
                .findViewById(R.id.xedtautoPincode)).getText().toString()
                .trim().length() <= 0) {
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoPincode))
                    .setError("please select pincode ");
            valid = false;

        } else if (((AutoCompleteTextView) view.findViewById(R.id.xedtautoArea))
                .getText().toString().trim().length() <= 0) {
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoArea))
                    .setError("Please Select Area");
            valid = false;

        }

        /*
         * else if
         * (!((EditText)view.findViewById(R.id.xedtOwnerName)).getText().
         * toString
         * ().trim().equals(getResources().getString(R.string.ownername)) &&
         * ((EditText
         * )view.findViewById(R.id.xedtEnginnerName)).getText().toString
         * ().length()>0) {
         * AppCommonDialog.setErrorMsg("Please Enter Owner name",
         * ((EditText)view.findViewById(R.id.xedtOwnerName)), true); valid =
         * false; }
         */

        return (valid);
    }

    // Requestdetail Task
    public class RequestDetailAsyncTask extends AsyncTask<String, Void, String> {

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

            } catch (Exception e) {
            }

            if (AppSetting.getRequestId() != null) {
                nameValuePairs = new ArrayList<NameValuePair>(1);
                // nameValuePairs.add(new BasicNameValuePair("post_data",
                // data));
                nameValuePairs.add(new BasicNameValuePair("requestID",
                        AppSetting.getRequestId()));
            }

            Log.d("LoginActivity", "Attempting Signup");
            return GetWebServiceData.getServerResponse(url
                    + "/GetRequestTabDetails", nameValuePairs, data);

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
            if (result != null && !result.equals("")) {
                Log.i("Request", result);
                try {
                    JSONObject serverResponsere = new JSONObject(result);
                    Log.d("addResActivity", "Save Review Successful");
                    RequestDetailsResponse serverResponseRequest = new Gson()
                            .fromJson(result, RequestDetailsResponse.class);

                    String loginResult = serverResponsere.getString("status");
                    if (loginResult.equals("OK")) {
                        Log.d("reuest detail", "get data");


                        if (result != null && AppSetting.getRequestId() != null) {
                            //SecondScreenActivity.mypcplData.insertdata(result, AppSetting.getRequestId());

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

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;

    }

    public void displaydataOffOn(RequestDetailsResponse serverResponseRequest) {
        if (serverResponseRequest != null
                && serverResponseRequest.getValues() != null
                && serverResponseRequest.getOptions() != null) {
            for (RequestDataDetail requestdata : serverResponseRequest
                    .getValues()) {
                requestgetData.add(requestdata);
            }

            for (RequestDropdownData optiondata : serverResponseRequest
                    .getOptions()) {
                requestoptionData.add(optiondata);
            }

            if (requestgetData != null
                    && requestgetData.size() > 0
                    && requestoptionData != null
                    && requestoptionData.size() > 0)
                displayRequestDetail(requestgetData);

            for (RequestDropdownData dropdata : serverResponseRequest
                    .getOptions()) {
                if (RequestDetailArea.equals(dropdata
                        .getDataType())) {
                    areaData.add(dropdata.getDataValue());
                }
                if (RequestDetailPincode.equals(dropdata
                        .getDataType())) {
                    pincodeData.add(dropdata.getDataValue());
                }
                if (RequestDetailProperty.equals(dropdata
                        .getDataType())) {
                    proprtyData.add(dropdata.getDataValue());
                }
                if (RequestDetailOccupiedSubType.equals(dropdata
                        .getDataType())) {
                    occupiedData.add(dropdata.getDataValue());
                }

                if (RequestDetailOccupied
                        .equals(dropdata.getDataType())) {
                    occupsubtypeData.add(dropdata
                            .getDataValue());
                }

            }
			/*if(offlineData!=null&&offlineData.size()>0){
			for(Offlinedatamodel datalocal:offlineData){
			if(!datalocal.getRequestid().equals(requestgetData.get(0).getRequestId())){
				offlineData.add(new Offlinedatamodel(requestgetData.get(0).getRequestId(),result));	
			}
			}
			}else{
				if(offlineData!=null)
				offlineData.add(new Offlinedatamodel(requestgetData.get(0).getRequestId(),result));
			}
			
			 Editor prefsEditor = mPrefs.edit();
		      //  Gson gson = new Gson();
		       // String json = gson.toJson(offlineData);
		       // prefsEditor.putString("MyObject", json);
		          try {
		        	  prefsEditor.putString("LIST", ObjectSerializer.serialize(offlineData));
                } catch (IOException e) {
               e.printStackTrace();
              }
		        prefsEditor.commit();
			*/


        }

    }

    public void displayRequestDetail(List<RequestDataDetail> requestgetData
    ) {
        // TODO Auto-generated method stub

        ((EditText) view.findViewById(R.id.xedtEnginnerName))
                .setText(requestgetData.get(0).getEngineerName());
        ((EditText) view.findViewById(R.id.xedtCustomerName))
                .setText(requestgetData.get(0).getCustomerName());
        ((EditText) view.findViewById(R.id.xedtOwnerName))
                .setText(requestgetData.get(0).getOwnerName());
        ((TextView) view.findViewById(R.id.xedtBankname))
                .setText(requestgetData.get(0).getBankName());
        ((EditText) view.findViewById(R.id.xedtContactName))
                .setText(requestgetData.get(0).getContactName());
        ((EditText) view.findViewById(R.id.xedtBoardName))
                .setText(requestgetData.get(0).getNameOnBoard());
        ((EditText) view.findViewById(R.id.xedtBuildingName))
                .setText(requestgetData.get(0).getBuildingName());
        ((EditText) view.findViewById(R.id.xedtaddressFlatno))
                .setText(requestgetData.get(0).getAddress1());
        ((EditText) view.findViewById(R.id.xedtAddressLandMark))
                .setText(requestgetData.get(0).getAddress2());
        ((EditText) view.findViewById(R.id.xedtaddressRoad))
                .setText(requestgetData.get(0).getAddress3());
        ((EditText) view.findViewById(R.id.xedtLocationArea))
                .setText(requestgetData.get(0).getLocation());
        ((EditText) view.findViewById(R.id.xedtPersonmet))
                .setText(requestgetData.get(0).getPersonMet());
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoPincode))
                .setText(requestgetData.get(0).getPincode());
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoArea))
                .setText(requestgetData.get(0).getArea());
        ((TextView) view.findViewById(R.id.xedtautoProperty))
                .setText(requestgetData.get(0).getPeopertyType());
        ((TextView) view.findViewById(R.id.xedtautoOccupation))
                .setText(requestgetData.get(0).getOccupant());
        ((TextView) view.findViewById(R.id.xedtautoOccupiedsubtype))
                .setText(requestgetData.get(0).getOccupied());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xbtnSubmit:
                if (validateForm())
                {
                    if (GetWebServiceData.isNetworkAvailable(getActivity())) {
                        new SetRequestDetailAsyncTask().execute();
                    } else {
                        //List<NameValuePair> nameValuePairs = null;
                        List<RequestDataDetail> RequestTab = null;
                        try {
                            RequestTab = new ArrayList<RequestDataDetail>();
                        } catch (Exception e) {
                        }

                        //nameValuePairs=saveSetdataoffline(nameValuePairs);
                        RequestTab.add(new RequestDataDetail(AppSetting.getRequestId(), ((EditText) view.findViewById(R.id.xedtEnginnerName))
                                .getText().toString(), ((TextView) view.findViewById(R.id.xedtBankname))
                                .getText().toString(), ((EditText) view.findViewById(R.id.xedtCustomerName))
                                .getText().toString(), ((EditText) view.findViewById(R.id.xedtBoardName))
                                .getText().toString(), ((EditText) view.findViewById(R.id.xedtOwnerName))
                                .getText().toString(), ((EditText) view.findViewById(R.id.xedtContactName))
                                .getText().toString(), ((EditText) view.findViewById(R.id.xedtaddressFlatno))
                                .getText().toString(), ((EditText) view.findViewById(R.id.xedtBuildingName))
                                .getText().toString(), ((EditText) view.findViewById(R.id.xedtAddressLandMark))
                                .getText().toString(), ((EditText) view.findViewById(R.id.xedtaddressRoad))
                                .getText().toString(), ((EditText) view.findViewById(R.id.xedtLocationArea))
                                .getText().toString(), ((AutoCompleteTextView) view
                                .findViewById(R.id.xedtautoArea)).getText().toString(), ((AutoCompleteTextView) view
                                .findViewById(R.id.xedtautoPincode)).getText()
                                .toString(), ((EditText) view.findViewById(R.id.xedtPersonmet))
                                .getText().toString(), ((TextView) view.findViewById(R.id.xedtautoProperty))
                                .getText().toString(), ((TextView) view
                                .findViewById(R.id.xedtautoOccupiedsubtype))
                                .getText().toString(), ((TextView) view.findViewById(R.id.xedtautoOccupation))
                                .getText().toString()));

                        if (RequestTab != null && RequestTab.size() > 0) {
                            saveOfflineDataDialog(RequestTab);
                        }

					/*AppCommonDialog.showSimpleDialog(getActivity(),
							getResources().getString(R.string.app_name),
							getResources().getString(R.string.check_network),
							getResources().getString(R.string.ok), "OK");*/

                    }
                }
                break;

            case R.id.ximgdropdownOccupiedsub:
                if (occupsubtypeData != null && occupsubtypeData.size() > 0)
                    lstoccupSubtype.setAdapter(new RequestDetailAdapter(
                            getActivity(), occupsubtypeData));
                lstoccupSubtype.setAnchorView(((TextView) view
                        .findViewById(R.id.xedtautoOccupiedsubtype)));

                lstoccupSubtype.setModal(true);
                lstoccupSubtype.show();

                break;
            default:
                break;
        }

    }

    // Requestdetail Task
    public class SetRequestDetailAsyncTask extends
            AsyncTask<String, Void, String> {

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
                nameValuePairs = new ArrayList<NameValuePair>(1);
            } catch (Exception e) {
            }
            nameValuePairs = saveSetdataoffline(nameValuePairs);

            System.out.println("Request before : " + nameValuePairs.toString());
            return GetWebServiceData.getServerResponse(url
                    + "/SetRequestTabDetails", nameValuePairs, data);

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
                        Toast.makeText(getActivity(),
                                "sucessfully " + loginResult,
                                Toast.LENGTH_SHORT).show();
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

    public List<NameValuePair> saveSetdataoffline(List<NameValuePair> nameValuePairs) {
        if (AppSetting.getRequestId() != null) {

            nameValuePairs.add(new BasicNameValuePair("RequestId",
                    AppSetting.getRequestId()));
            nameValuePairs.add(new BasicNameValuePair("EngineerName",
                    ((EditText) view.findViewById(R.id.xedtEnginnerName))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("BankName",
                    ((TextView) view.findViewById(R.id.xedtBankname))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("CustomerName",
                    ((EditText) view.findViewById(R.id.xedtCustomerName))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("NameOnBoard",
                    ((EditText) view.findViewById(R.id.xedtBoardName))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("OwnerName",
                    ((EditText) view.findViewById(R.id.xedtOwnerName))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("ContactName",
                    ((EditText) view.findViewById(R.id.xedtContactName))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Address1",
                    ((EditText) view.findViewById(R.id.xedtaddressFlatno))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Address2", ((EditText) view.findViewById(R.id.xedtAddressLandMark))
                    .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Address3",
                    ((EditText) view.findViewById(R.id.xedtaddressRoad))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Location",
                    ((EditText) view.findViewById(R.id.xedtLocationArea))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("BuildingName", ((EditText) view.findViewById(R.id.xedtBuildingName))
                    .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Area",
                    ((AutoCompleteTextView) view
                            .findViewById(R.id.xedtautoArea)).getText()
                            .toString()));
            nameValuePairs.add(new BasicNameValuePair("Pincode",
                    ((AutoCompleteTextView) view
                            .findViewById(R.id.xedtautoPincode)).getText()
                            .toString()));
            nameValuePairs.add(new BasicNameValuePair("PersonMet",
                    ((EditText) view.findViewById(R.id.xedtPersonmet))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("PeopertyType",
                    ((TextView) view.findViewById(R.id.xedtautoProperty))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Occupied",
                    ((TextView) view
                            .findViewById(R.id.xedtautoOccupiedsubtype))
                            .getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Occupant",
                    ((TextView) view.findViewById(R.id.xedtautoOccupation))
                            .getText().toString()));


        }

        return nameValuePairs;
    }

    protected void saveOfflineDataDialog(final List<RequestDataDetail> requestTab) {

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
		// btnsendemail.setText("OK");
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		// if button is clicked, close the custom dialog
		buttonOk.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Gson gson = new Gson();
				String strRequest = "";
				RequestDetailsResponse data=new RequestDetailsResponse();
				  data.setRemarks("yes");
				  data.setStatus("sucessfully");
				  data.setValues(requestTab);
				strRequest = gson.toJson(data);
				
			boolean bupdate= SecondScreenActivity.mypcplData.updateRequestdata(AppSetting.getRequestId(), strRequest, strRequest);
			if(bupdate){
				Toast.makeText(getActivity(),"sucessfully store data  ",Toast.LENGTH_SHORT).show();
				updateandDisplayofflineData("");
				AppSetting.setUpdateOfflinedata(true);
			}
				dialog.dismiss();
			}

		});
		dialog.show();*/

        Gson gson = new Gson();
        String strRequest = "";
        RequestDetailsResponse data = new RequestDetailsResponse();
        data.setRemarks("yes");
        data.setStatus("sucessfully");
        data.setValues(requestTab);
        strRequest = gson.toJson(data);
        boolean bupdate = SecondScreenActivity.mypcplData.updateRequestdata(AppSetting.getRequestId(), strRequest, strRequest);
        if (bupdate) {
            Toast.makeText(getActivity(), "sucessfully Update offline data ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData("");
            AppSetting.setUpdateOfflinedata(true);
        } else if (!bupdate) {
            SecondScreenActivity.mypcplData.insertdata(strRequest, AppSetting.getRequestId());
            Toast.makeText(getActivity(), "sucessfully store data offline  ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData(AppSetting.getRequestId());
        }
    }
}
