package com.techinvest.pcpl.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.SetOfflineRequest;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.parserhelper.DataBaseHelper;
import com.techinvest.pcplrealestate.LoginActivity;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.SecondScreenActivity;
import com.techinvest.pcplrealestate.Services;
import com.techinvest.pcplrealestate.ThirdScreenActivity;
import com.techinvest.pcplrealestate.SecondScreenActivity.GetOfflineSyncDataAsyncTask;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.BuildingResponse;
import com.techinvest.pcpl.model.CarpetResponse;
import com.techinvest.pcpl.model.CommonRequestData;
import com.techinvest.pcpl.model.FlatSpecificationResponse;
import com.techinvest.pcpl.model.GetBuildingDetailsResponse;
import com.techinvest.pcpl.model.GetCarpetAreaResponse;
import com.techinvest.pcpl.model.GetFlatSpecificationDetailsResponse;
import com.techinvest.pcpl.model.GetLocationResponse;
import com.techinvest.pcpl.model.GetProjectDetailsResponse;
import com.techinvest.pcpl.model.GetRoomdetailResponse;
import com.techinvest.pcpl.model.LocationDetailsResponse;
import com.techinvest.pcpl.model.OfflineSynStatus;
import com.techinvest.pcpl.model.OfflineSyncResponse;
import com.techinvest.pcpl.model.PendingVisit;
import com.techinvest.pcpl.model.ProjectTabResponse;
import com.techinvest.pcpl.model.RequestDataDetail;
import com.techinvest.pcpl.model.RequestDetailsResponse;
import com.techinvest.pcpl.model.RoomdetailResponse;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;

import static com.techinvest.pcplrealestate.SecondScreenActivity.SetDetails;
import static com.techinvest.pcplrealestate.SecondScreenActivity.buildingDataList;
import static com.techinvest.pcplrealestate.SecondScreenActivity.buildingTab;
import static com.techinvest.pcplrealestate.SecondScreenActivity.carpetDataList;
import static com.techinvest.pcplrealestate.SecondScreenActivity.flatDataList;
import static com.techinvest.pcplrealestate.SecondScreenActivity.flatTab;
import static com.techinvest.pcplrealestate.SecondScreenActivity.flowerbedDataList;
import static com.techinvest.pcplrealestate.SecondScreenActivity.locationDataList;
import static com.techinvest.pcplrealestate.SecondScreenActivity.locationTab;
import static com.techinvest.pcplrealestate.SecondScreenActivity.projectDataList;
import static com.techinvest.pcplrealestate.SecondScreenActivity.projectTab;
import static com.techinvest.pcplrealestate.SecondScreenActivity.requestDataList;
import static com.techinvest.pcplrealestate.SecondScreenActivity.requestTab;
import static com.techinvest.pcplrealestate.SecondScreenActivity.roomArea;
import static com.techinvest.pcplrealestate.SecondScreenActivity.roomAreaFB;
import static com.techinvest.pcplrealestate.SecondScreenActivity.roomDataList;
import static com.techinvest.pcplrealestate.SecondScreenActivity.roomDetail;

public class CustomArrayAdapter extends BaseAdapter {
    private ArrayList<PendingVisit> mClient;
    public ArrayList<PendingVisit> arraylist;
    private final Context context;
    LayoutInflater inflater;
    private ProgressDialog loadingDialog;
    public static DataBaseHelper mypcplData;
    List<OfflineSynStatus> sysOfflineStatus;
    /*List<SetOfflinedata> requestDataList;
    List<SetOfflinedata> projectDataList;
    List<SetOfflinedata> locationDataList;
    List<SetOfflinedata> buildingDataList;
    List<SetOfflinedata> flatDataList;
    List<SetOfflinedata> roomDataList;
    List<SetOfflinedata> carpetDataList;
    List<SetOfflinedata> flowerbedDataList;
*/
    /*public static List<SetOfflineRequest> SetDetails;
    List<RequestDataDetail> requestTab;
	List<GetBuildingDetailsResponse> buildingTab;
	List<GetLocationResponse> locationTab;
	List<GetFlatSpecificationDetailsResponse> flatTab;
	List<GetProjectDetailsResponse> projectTab;
	List<GetRoomdetailResponse> roomDetail;
	List<GetCarpetAreaResponse> roomArea;
	List<GetCarpetAreaResponse> roomAreaFB;
	List<OfflineSynStatus> sysOnlineStatus;
	*/
    Dialog updateOffline;

    String url = AppSetting.getapiURL();
    //SetOfflineDataAsyncTask setoff;
//	SetOfflinedata setofflineid;
    //String ReqID = setofflineid.getRequestid();
    //SharedPreferences mPrefs;


    public CustomArrayAdapter(Context context, ArrayList<PendingVisit> mClient, List<OfflineSynStatus> sysOfflineStatus) {
        //this.mypcplData = new DataBaseHelper(context)

        this.context = context;
        this.mClient = mClient;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.sysOfflineStatus = sysOfflineStatus;

        this.arraylist = new ArrayList<PendingVisit>();
        this.arraylist.addAll(mClient);


        requestDataList = new ArrayList<SetOfflinedata>();
        locationDataList = new ArrayList<SetOfflinedata>();
        projectDataList = new ArrayList<SetOfflinedata>();
        buildingDataList = new ArrayList<SetOfflinedata>();
        flatDataList = new ArrayList<SetOfflinedata>();
        roomDataList = new ArrayList<SetOfflinedata>();
        carpetDataList = new ArrayList<SetOfflinedata>();
        flowerbedDataList = new ArrayList<SetOfflinedata>();
        SetDetails = new ArrayList<SetOfflineRequest>();

        requestTab = new ArrayList<RequestDataDetail>();
        buildingTab = new ArrayList<GetBuildingDetailsResponse>();
        locationTab = new ArrayList<GetLocationResponse>();
        flatTab = new ArrayList<GetFlatSpecificationDetailsResponse>();
        projectTab = new ArrayList<GetProjectDetailsResponse>();
        roomDetail = new ArrayList<GetRoomdetailResponse>();
        roomArea = new ArrayList<GetCarpetAreaResponse>();
        roomAreaFB = new ArrayList<GetCarpetAreaResponse>();


    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mClient.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pendingvisits, null);
            holder = new ViewHolder();

            holder.clientname = (TextView) convertView.findViewById(R.id.xtxtClientName);
            holder.customer = (TextView) convertView.findViewById(R.id.xtxtCustomerName);
            holder.contact = (TextView) convertView.findViewById(R.id.xtxtContact);
            holder.area = (TextView) convertView.findViewById(R.id.xtxtArea);
            holder.txtbranchName = (TextView) convertView.findViewById(R.id.xtxtBranch);
            holder.btnSyncOffline = (Button) convertView.findViewById(R.id.xbtnSynoffline);
            holder.btnUpdateOnline = (Button) convertView.findViewById(R.id.xbtnUpdateOnline);
            holder.btnReleaseReq = (Button) convertView.findViewById(R.id.xbtnReleaseReq);

            holder.txtCaseNo = (TextView) convertView.findViewById(R.id.xtxtCaseNo);
            //holder.img = (ImageView) convertView.findViewById(R.id.ximgeditCustomer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.clientname.setText(mClient.get(position).getClientName());
        holder.customer.setText(mClient.get(position).getApplicantName());
        holder.contact.setText(mClient.get(position).getApplicantPhoneNo());
        holder.area.setText(mClient.get(position).getAreaOrWard());
        holder.txtbranchName.setText(mClient.get(position).getBranchName());
        holder.txtCaseNo.setText(mClient.get(position).getCaseNo());

        changeButtonState(holder.btnSyncOffline, position);
        changeButtonState1(holder.btnUpdateOnline, position);
        //btnUpdateOnline


        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //pos=position;
                displayCustomerDetailDialog(position);
            }
        });
        holder.btnReleaseReq.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        context);
                alertDialog.setTitle("Release");
                alertDialog.setMessage("Confirm To Release This Request ?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        if (mClient.get(position).getRequestID() != null) {
                            AppSetting.setRequestId(mClient.get(position).getRequestID());
                            Log.e("12345", "" + position);
                            //0009
                            //arraylist.remove(position);
                            PendingVisit tempItem = null;
                            boolean isItemExisted = false;
                            for (PendingVisit item : arraylist) {
                                if (item.getRequestID().equals(mClient.get(position).getRequestID())) {
                                    tempItem = item;
                                    isItemExisted = true;
                                    break;
                                }
                            }
                            if (isItemExisted) {
                                arraylist.remove(tempItem);
                            }
                            mClient.remove(position);

                            notifyDataSetChanged();
                            new ReleaseReqBackgroundTask().execute();


                        }
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onNegativeButtonClicked(alertDialog);
                    }
                });
                // Showing Alert Message
                alertDialog.show();

            }
        });
        holder.btnSyncOffline.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Bundle data=new Bundle();
                /*Intent intent = new Intent(context, ThirdScreenActivity.class);
                data.putSerializable("CUSTOMER", mClient);
				data.putInt("POS", position);
				intent.putExtras(data);
				context.startActivity(intent);
				
				displayCustomerDetailDialog(position);*/
                if (mClient.get(position).getRequestID() != null)
                    AppSetting.setRequestId(mClient.get(position).getRequestID());

                if (GetWebServiceData.isNetworkAvailable(context)) {
                    if (AppSetting.getRequestId() != null)
                        new GetOfflineSyncDataAsyncTask(position).execute();
                } else {
                    AppCommonDialog.showSimpleDialog((Activity) context, context.getResources()
                                    .getString(R.string.app_name),context.getResources()
                                    .getString(R.string.check_network),context.getResources().getString(R.string.ok), "OK");
                }


            }
        });

        holder.btnUpdateOnline.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //	 if(context instanceof SecondScreenActivity) {
                //		 ((SecondScreenActivity)context).SetOfflineDataAsyncTask.execute();
                //Bundle data=new Bundle();
                /*Intent intent = new Intent(context, ThirdScreenActivity.class);
				data.putSerializable("CUSTOMER", mClient);
				data.putInt("POS", position);
				intent.putExtras(data);
				context.startActivity(intent);

				displayCustomerDetailDialog(position);*/
                syncCaseServer(position);

            }
        });
        return convertView;
    }

    public void syncCaseServer(int position){
        if (mClient.get(position).getRequestID() != null)
            AppSetting.setRequestId(mClient.get(position).getRequestID());
        if (context instanceof SecondScreenActivity) {
            ((SecondScreenActivity) context).updateOnlineData(AppSetting.getRequestId());
        }
        //updateOnlineData(AppSetting.getRequestId());
        Gson gson = new Gson();
        if (GetWebServiceData.isNetworkAvailable(context)) {
            if (SetDetails != null && SetDetails.size() > 0) {
                new SetOfflineDataAsyncTask().execute();//009
            }
        } else {
            AppCommonDialog.showSimpleDialog((Activity) context, context.getResources()
                    .getString(R.string.app_name), context.getResources()
                    .getString(R.string.check_network), context.getResources().getString(R.string.ok), "OK");
        }
    }

    private void onNegativeButtonClicked(AlertDialog.Builder alertDialog) {
        // alertDialog.dismiss();
    }


    private void changeButtonState(Button btnSyncOffline, int position) {
        // TODO Auto-generated method stub
        if (sysOfflineStatus != null && sysOfflineStatus.size() > 0) {
            for (OfflineSynStatus data : sysOfflineStatus) {
                if (data.getRequestId().equals(mClient.get(position).getRequestID())) {
                    if (data.getSynOfflineStatus().equals("1")) {
                        btnSyncOffline.setBackgroundColor(context.getResources().getColor(R.color.ColorPrimary));
                    } else {
                        btnSyncOffline.setBackgroundColor(context.getResources().getColor(R.color.gray));

                    }
                }
            }

        }


    }

    private void changeButtonState1(Button btnUpdateOnline, int position) {
        // TODO Auto-generated method stub
        if (sysOfflineStatus != null && sysOfflineStatus.size() > 0) {
            for (OfflineSynStatus data : sysOfflineStatus) {
                if (data.getRequestId().equals(mClient.get(position).getRequestID())) {
                    if (data.getSynOfflineStatus().equals("1")) {
                        btnUpdateOnline.setBackgroundColor(context.getResources().getColor(R.color.ColorPrimary));
                    } else {
                        btnUpdateOnline.setBackgroundColor(context.getResources().getColor(R.color.gray));

                    }
                }
            }

        }


    }


    protected void displayCustomerDetailDialog(final int position) {

        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_popupdialog);
        TextView textTitle = (TextView) dialog.findViewById(R.id.xtxttitle);
        textTitle.setText("Customer Detail");
        // set the custom dialog components - text, image and button
        ((TextView) dialog.findViewById(R.id.xedtCustomer)).setText(mClient.get(position).getApplicantName());
        ((TextView) dialog.findViewById(R.id.xedtMobileNo)).setText(mClient.get(position).getApplicantPhoneNo());
        ((TextView) dialog.findViewById(R.id.xedtAddress)).setText(mClient.get(position).getBuildingName());

        ((TextView) dialog.findViewById(R.id.xedtMobileNo)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mClient.get(position).getApplicantPhoneNo()));
                context.startActivity(intent);

            }
        });


        Button btnproceed = (Button) dialog
                .findViewById(R.id.xbtnok);
        Button btnCancel = (Button) dialog.findViewById(R.id.xbtncancel);
        // btnsendemail.setText("OK");
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        // if button is clicked, close the custom dialog
        btnproceed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GetWebServiceData.isNetworkAvailable(context))
                {
                    //		 if(AppSetting.getUpdateOfflinedata()){
                    //		 Toast.makeText(context, "Please update  data to server first", 1).show();

                    //	 }else{
                    if (mClient.get(position).getRequestID() != null) {
                        AppSetting.setRequestId(mClient.get(position).getRequestID());
                        syncCaseServer(position);
                        Toast.makeText(context,"Sync Completed",Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    Intent intent = new Intent(context, Services.class);
                    context.startActivity(intent);
                }
                else {
                    if (mClient.get(position).getRequestID() != null)
                        AppSetting.setRequestId(mClient.get(position).getRequestID());
                    System.out.println("Request id  : " + AppSetting.getRequestId());
                    dialog.dismiss();
                    Intent intent = new Intent(context, Services.class);
                    context.startActivity(intent);
                }


            }

        });

        dialog.show();

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
            if (locationDataList != null && locationDataList.size() > 0) {
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
            if (projectDataList != null && projectDataList.size() > 0) {
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
            if (buildingDataList != null && buildingDataList.size() > 0) {
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
                loadingDialog = new ProgressDialog(context);
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
            nameValuePairs.add(new BasicNameValuePair("AllRequestData", strsave));
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

            Log.e("Request data display offline second", "REQUEST_DATA Second= " + strsave);
            //SERVER_URL=obj.getServerURL();
            System.out.println("Request before : " + nameValuePairs.toString());
            return GetWebServiceData.getServerResponse(url
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

                        //AppCommonDialog.showSimpleDialog(context, getResources().getString(R.string.app_name),
                        //		"sucessfully "+loginResult,
                        //		getResources().getString(R.string.ok), "OK");
                        //	boolean bdelete = mypcplData.DeleteAllRecord();
                        //if (bdelete) {
                        //	Toast.makeText(context,
                        //			"sucessfully update and clear local data ",
                        //			Toast.LENGTH_SHORT).show();
                        if (SetDetails != null && SetDetails.size() > 0) {
                            SetDetails.clear();

                        }
                        AppSetting.setUpdateOfflinedata(false);
                        Toast.makeText(context, "sucessfully updated", Toast.LENGTH_SHORT).show();
                    }

						/*
						 * if (serverResponseRequest != null&&
						 * serverResponseRequest.getStatus()!=null) {
						 * AppCommonDialog.showSimpleDialog(getActivity(),
						 * getResources
						 * ().getString(R.string.app_name),"Data Update Sucessfully"
						 * , getResources().getString(R.string.ok), "OK"); }
						 */
                    //} else {
                    // Toast.makeText(getApplicationContext(),
                    // faultObject.getString("faultstring"),
                    // Toast.LENGTH_SHORT).show();
                    //	}

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.d("LoginActivity", "JSON Result parse error");
                    e.printStackTrace();
                }

            }
        }
    }

    static class ViewHolder {
        public TextView clientname;
        public TextView customer;
        public TextView contact;
        public TextView area;
        public TextView txtbranchName;
        public TextView txtCaseNo;
        public Button btnSyncOffline;
        public Button btnUpdateOnline;
        public Button btnReleaseReq;

    }


    // Requestdetail Task
    public class GetOfflineSyncDataAsyncTask extends AsyncTask<String, Void, String>
    {
        int pos = 0;

        public GetOfflineSyncDataAsyncTask(int position) {
            // TODO Auto-generated constructor stub
            this.pos = position;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                loadingDialog = new ProgressDialog(context);
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
            } catch (Exception e) { }
            nameValuePairs.add(new BasicNameValuePair("EngineerID", AppSetting.getUserId()));
            nameValuePairs.add(new BasicNameValuePair("RequestID", AppSetting.getRequestId()));


            System.out.println("Request before : " + nameValuePairs.toString());
            return GetWebServiceData.getServerResponse(url + "/syncOfflineFromOnlineR", nameValuePairs, data);
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
                    OfflineSyncResponse serverResponseRequest = new Gson().fromJson(result, OfflineSyncResponse.class);
                    String loginResult = serverResponsere.getString("status");
                    if (loginResult.equals("OK"))
                    {
                        if (context instanceof SecondScreenActivity) {
                            ((SecondScreenActivity) context).mypcplData.DeleteRecord(AppSetting.getRequestId());
                        }

                        //Toast.makeText(activity, "sucessfully " + loginResult,Toast.LENGTH_SHORT).show();

                        //SecondScreenActivity.mypcplData.DeleteAllRecord();
                        SynOfflineupdateDialog(context, context.getResources()
                                        .getString(R.string.app_name), "sucessfully",
                                context.getResources().getString(R.string.ok),
                                pos, "OK");
							/*updateOffline=AppCommonDialog.showSimpleDialog(context, context.getResources()
									.getString(R.string.app_name),
									 "sucessfully "+loginResult,
									 context.getResources().getString(R.string.ok), "OK");*/
                        //sysOfflineStatus.add(new OfflineSynStatus(mClient.get(pos).getRequestID(),"1"));

                        //if (bdelete) {
                        //Toast.makeText(activity,"sucessfully clear local data ",Toast.LENGTH_SHORT).show();
                        //	}

                        if (serverResponseRequest != null) {
                            String requestTabRequest = null;
                            String requestLocationTab = null;
                            String requestProjectTab = null;
                            String requestBuildingTab = null;
                            String requestFlatTab = null;
                            String requestRoomTab = null;
                            String roomDetailid = null;
                            String carpetDetailid = null;
                            String flowerDetailid = null;

                            Gson gson = new Gson();

                            // Request data save
                            for (RequestDetailsResponse requestdata : serverResponseRequest.getRequestData()) {
                                requestdata.setStatus("OK");

                                requestTabRequest = gson.toJson(requestdata);
                                System.out.println("Request Syn Offline RequestTab : " + requestTabRequest);
                                for (RequestDataDetail requestdatadetail : requestdata.getValues()) {

                                    if (requestTabRequest != null && requestdatadetail != null && requestdatadetail.getRequestId() != null) {
                                        SecondScreenActivity.mypcplData.insertdata(requestTabRequest, requestdatadetail.getRequestId());

                                    }
                                }

                            }
                            // location data save
                            for (LocationDetailsResponse locationDetail : serverResponseRequest.getLocationData()) {
                                locationDetail.setStatus("OK");

                                requestLocationTab = gson.toJson(locationDetail);
                                System.out.println("Request Syn Offline location : " + requestLocationTab);
                                for (GetLocationResponse locationdata : locationDetail.getValues()) {
                                    if (requestLocationTab != null && locationdata != null && locationdata.getRequestId() != null) {
                                        SecondScreenActivity.mypcplData.insertLocationdata(requestLocationTab, locationdata.getRequestId());

                                    }
                                }
                            }


                            // Project tab data save
                            for (ProjectTabResponse projectDetail : serverResponseRequest.getProjectData()) {
                                projectDetail.setStatus("OK");

                                requestProjectTab = gson.toJson(projectDetail);
                                System.out.println("Request Syn Offline Project : " + requestLocationTab);
                                for (GetProjectDetailsResponse projectdata : projectDetail.getValues()) {
                                    if (requestProjectTab != null && projectdata != null && projectdata.getRequestId() != null) {
                                        SecondScreenActivity.mypcplData.insertProjectdata(requestProjectTab, projectdata.getRequestId());

                                    }
                                }
                            }

                            // Building  tab data save
                            for (BuildingResponse buildingDetail : serverResponseRequest.getBuildingData()) {
                                buildingDetail.setStatus("OK");

                                requestBuildingTab = gson.toJson(buildingDetail);
                                System.out.println("Request Syn Offline building : " + requestBuildingTab);
                                for (GetBuildingDetailsResponse buindingdata : buildingDetail.getValues()) {
                                    if (requestBuildingTab != null && buindingdata != null && buindingdata.getRequestId() != null) {
                                        SecondScreenActivity.mypcplData.insertBuildingdata(requestBuildingTab, buindingdata.getRequestId());

                                    }
                                }
                            }

                            // Flat  tab data save
                            for (FlatSpecificationResponse flatDetail : serverResponseRequest.getFlatData()) {
                                flatDetail.setStatus("OK");

                                requestFlatTab = gson.toJson(flatDetail);
                                System.out.println("Request Syn Offline flat : " + requestFlatTab);
                                for (GetFlatSpecificationDetailsResponse flatdata : flatDetail.getValues()) {
                                    if (requestFlatTab != null && flatdata != null && flatdata.getRequestId() != null) {
                                        SecondScreenActivity.mypcplData.insertFlatdata(requestFlatTab, flatdata.getRequestId());

                                    }
                                }
                            }

                            // Room  tab data save
                            for (RoomdetailResponse roomDetail : serverResponseRequest.getRoomData()) {
                                List<GetRoomdetailResponse> roomTab = null;
                                roomDetail.setStatus("OK");


                                System.out.println("Request Syn Offline Room : " + requestBuildingTab);
                                for (GetRoomdetailResponse roomdata : roomDetail.getValues()) {
                                    roomTab = new ArrayList<GetRoomdetailResponse>();
                                    roomTab.add(roomdata);
                                    roomDetail.setValues(roomTab);
                                    requestRoomTab = gson.toJson(roomDetail);
                                    roomDetailid = roomdata.getRoomDetailsID();
                                    if (roomDetailid == null) {
                                        roomDetailid = String.valueOf(genNumber());
                                    }
                                    if (requestRoomTab != null && roomdata != null && roomDetailid != null && roomdata.getRequestId() != null) {
                                        SecondScreenActivity.mypcplData.insertRoomdata(requestRoomTab, roomdata.getRequestId(), roomDetailid);


                                    }
                                }
                            }


                            // Carpet  tab data save
                            for (CarpetResponse carpetDetail : serverResponseRequest.getCarpetAreaTab()) {
                                List<GetCarpetAreaResponse> carpetTab = null;
                                carpetDetail.setStatus("OK");

                                if (carpetDetail != null) {
                                    for (GetCarpetAreaResponse carpetdata : carpetDetail.getValuesCarpetArea()) {
                                        carpetTab = new ArrayList<GetCarpetAreaResponse>();
                                        if (carpetdata != null) {
                                            carpetTab.add(carpetdata);
                                            carpetDetail.setValuesCarpetArea(carpetTab);
                                            requestRoomTab = gson.toJson(carpetDetail);

                                            System.out.println("Request Syn Offline Carpet : " + requestRoomTab);
                                            carpetDetailid = carpetdata.getRoomDetailsIDs();

                                            if (carpetDetailid == null) {
                                                carpetDetailid = String.valueOf(genNumber());
                                            }
                                            if (requestRoomTab != null && carpetdata != null && carpetDetailid != null && carpetdata.getRequestId() != null) {
                                                SecondScreenActivity.mypcplData.insertCarpetdata(requestRoomTab, carpetdata.getRequestId(), carpetDetailid);


                                            }
                                        }
                                    }
                                }
                            }

                            // Carpet  tab data save
                            for (CarpetResponse carpetDetail : serverResponseRequest.getFlowerBedAreaTab()) {
                                List<GetCarpetAreaResponse> flowerTab = null;
                                carpetDetail.setStatus("OK");


                                //requestRoomTab = gson.toJson(carpetDetail);
                                System.out.println("Request Syn Offline Carpet : " + requestRoomTab);
                                if (carpetDetail != null) {
                                    for (GetCarpetAreaResponse carpetdata : carpetDetail.getValuesCarpetArea()) {
                                        flowerTab = new ArrayList<GetCarpetAreaResponse>();
                                        if (carpetdata != null) {
                                            carpetDetailid = carpetdata.getRoomDetailsIDs();
                                            flowerTab.add(carpetdata);
                                            carpetDetail.setValuesCarpetArea(flowerTab);
                                            requestRoomTab = gson.toJson(carpetDetail);
                                            if (carpetDetailid == null) {
                                                carpetDetailid = String.valueOf(genNumber());
                                            }
                                            if (requestRoomTab != null && carpetdata != null && carpetDetailid != null && carpetdata.getRequestId() != null) {
                                                SecondScreenActivity.mypcplData.insertFlowerbeddata(requestRoomTab, carpetdata.getRequestId(), carpetDetailid);


                                            }
                                        }
                                    }
                                }
                            }
                            /////////
                            // Layout tab data save (Images)
                            /*for (CarpetResponse carpetDetail : serverResponseRequest.getFlowerBedAreaTab()) {
                                List<GetCarpetAreaResponse> flowerTab = null;
                                carpetDetail.setStatus("OK");


                                //requestRoomTab = gson.toJson(carpetDetail);
                                System.out.println("Request Syn Offline Carpet : " + requestRoomTab);
                                if (carpetDetail != null) {
                                    for (GetCarpetAreaResponse carpetdata : carpetDetail.getValuesCarpetArea()) {
                                        flowerTab = new ArrayList<GetCarpetAreaResponse>();
                                        if (carpetdata != null) {
                                            carpetDetailid = carpetdata.getRoomDetailsIDs();
                                            flowerTab.add(carpetdata);
                                            carpetDetail.setValuesCarpetArea(flowerTab);
                                            requestRoomTab = gson.toJson(carpetDetail);
                                            if (carpetDetailid == null) {
                                                carpetDetailid = String.valueOf(genNumber());
                                            }
                                            if (requestRoomTab != null && carpetdata != null && carpetDetailid != null && carpetdata.getRequestId() != null) {
                                                SecondScreenActivity.mypcplData.insertFlowerbeddata(requestRoomTab, carpetdata.getRequestId(), carpetDetailid);


                                            }
                                        }
                                    }
                                }
                            }*/


                        }


                    } else {
                        // Toast.makeText(getApplicationContext(),
                        // faultObject.getString("faultstring"),
                        // Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.d("LoginActivity "+CustomArrayAdapter.class.getName(), "JSON Result parse error"+e.getMessage());
                    e.printStackTrace();
                }

            }
        }
    }

    public void SynOfflineupdateDialog(Context context, String alerttitle,
                                       String messagetxt, String btnOk, final int pos, String btnvalue) {
        if (updateOffline != null)
            updateOffline.dismiss();
        updateOffline = AppCommonDialog.showSimpleDialog((Activity) context, alerttitle, messagetxt, btnOk, btnvalue);
        updateOffline.show();


        //Button btnLogout = (Button) alertLogin.findViewById(R.id.btn_cancel);
        Button btnupdate = (Button) updateOffline.findViewById(R.id.btn_confirm_ok);


        btnupdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SecondScreenActivity.mypcplData.insertSysOfflineStatus(mClient.get(pos).getRequestID(), "1");

                if (sysOfflineStatus != null) {
                    sysOfflineStatus = SecondScreenActivity.mypcplData.getSysOfflineStatus();
                } else {
                    sysOfflineStatus = new ArrayList<OfflineSynStatus>();
                    sysOfflineStatus = SecondScreenActivity.mypcplData.getSysOfflineStatus();

                }
                notifyDataSetChanged();
                updateOffline.dismiss();


            }
        });

    }

    private class ReleaseReqBackgroundTask extends AsyncTask<String, Void, String> {
        final ProgressDialog pDialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String data = "";
            List<NameValuePair> nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("RequestID", AppSetting.getRequestId()));
            nameValuePairs.add(new BasicNameValuePair("UserID", AppSetting.getUserId()));
            Log.d("123456", data.toString());
            return GetWebServiceData.getServerResponse(url + "/GetReleaseRequest", nameValuePairs, data);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            } catch (IllegalArgumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if (result != null && !result.equals("")) {
                Log.d("123456", result);
               /* try
                {
                    *//*UnsignedReq obj = new Gson().fromJson(result, UnsignedReq.class);
                    String loginResult = obj.getJj().get(0).getApplicantName();
                    List<Jj> jjList=new ArrayList<Jj>();
                    for(int i=0;i<obj.getJj().size();i++)
                    {    jjList.add(obj.getJj().get(i));    }
                    adapter=new UnAssignedReqListViewAdapter(visitsActivity.this, jjList);
                    list.setAdapter(adapter);*//*
                    //Log.d(TAG, loginResult);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.d("LoginActivity", "JSON Result parse error");
                    e.printStackTrace();
                }*/
                notifyDataSetChanged();

                Toast.makeText(context, "Request has been released Susscessfully !!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        mClient.clear();
        if (text.length() == 0) {
            mClient.addAll(arraylist);
        } else {
            for (PendingVisit wp : arraylist) {
                if (wp.getApplicantName().toLowerCase(Locale.getDefault()).contains(text) ||
                        wp.getAreaOrWard().toLowerCase(Locale.getDefault()).contains(text) ||
                        wp.getClientName().toLowerCase(Locale.getDefault()).contains(text)) {
                    mClient.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public int genNumber() {
        Random r = new Random(System.currentTimeMillis());
        return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
    }
}
