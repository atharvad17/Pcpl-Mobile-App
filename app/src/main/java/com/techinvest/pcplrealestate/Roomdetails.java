package com.techinvest.pcplrealestate;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.techinvest.pcpl.adapter.LocationDetailAdapter;
import com.techinvest.pcpl.adapter.RoomDetailAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.FlatSpecificationResponse;
import com.techinvest.pcpl.model.GetCarpetAreaResponse;
import com.techinvest.pcpl.model.GetFlatSpecificationDetailsResponse;
import com.techinvest.pcpl.model.GetRoomdetailResponse;
import com.techinvest.pcpl.model.Offlinedatamodel;
import com.techinvest.pcpl.model.RequestDropdownData;
import com.techinvest.pcpl.model.RoomdetailResponse;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcplrealestate.fragment.Buildingdetails.SetBuildingAsyncTask;

@SuppressLint("NewApi")
public class Roomdetails extends Fragment implements OnClickListener {

    private View view;
    Activity activity;
    private TextView text;
    private ProgressDialog loadingDialog;
    List<GetRoomdetailResponse> roomdetailData;
    String url = AppSetting.getapiURL();
    //List<GetCarpetAreaResponse> carpetAreaData;
    //List<GetCarpetAreaResponse> floorAreaData;
    List<String> roomTypeData;
    List<String> flooringData;
    List<String> doorsTypeData;
    List<String> windowTypeData;
    List<String> wCTypeData;
    List<String> kitchenType;
    String Roomdetailid = "";
    ListPopupWindow lstroomType;
    ListPopupWindow lstkitchenType;
    ListPopupWindow lstdoorType;
    ListPopupWindow lstwindowsType;
    ListPopupWindow lstWcType;
    ListPopupWindow lstflooringType;
    AutoCompleteTextView edtautoRoom;
    AutoCompleteTextView edtautoflooring;
    AutoCompleteTextView edtautoDoor;
    AutoCompleteTextView edtautowindowsdiscription;
    AutoCompleteTextView edtautokitchenplatform;
    AutoCompleteTextView edtwctype;
    private boolean baddnewRoomdetail;
    List<Offlinedatamodel> offlineData;
    List<SetOfflinedata> roomDataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.room_detailnew, container, false);
        // roomdetails = (Button) view.findViewById(R.id.xbtnRoomdetail);
        // carpetdetails = (Button) view.findViewById(R.id.xbtnCarpetdetail);
        // flowerbeddetails = (Button)
        // view.findViewById(R.id.xbtnFlowerbeddetail);


        roomTypeData = new ArrayList<String>();
        flooringData = new ArrayList<String>();
        doorsTypeData = new ArrayList<String>();
        windowTypeData = new ArrayList<String>();
        kitchenType = new ArrayList<String>();
        wCTypeData = new ArrayList<String>();

        lstroomType = new ListPopupWindow(activity);
        lstkitchenType = new ListPopupWindow(activity);
        lstdoorType = new ListPopupWindow(activity);
        lstwindowsType = new ListPopupWindow(activity);
        lstWcType = new ListPopupWindow(activity);
        lstflooringType = new ListPopupWindow(activity);


        initview();
        offlineData = new ArrayList<Offlinedatamodel>();
        roomDataList = new ArrayList<SetOfflinedata>();
        if (offlineData != null) {
            offlineData = SecondScreenActivity.mypcplData.getRoomDetail(AppSetting.getRequestId());
        }


        if (GetWebServiceData.isNetworkAvailable(getActivity())) {

            new RoomdetailAsyncTask().execute();

        } else {
            roomdetailData = new ArrayList<GetRoomdetailResponse>();
            if (offlineData.size() > 0) {

                for (Offlinedatamodel datalocal : offlineData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestid().equals(AppSetting.getRequestId())) {
                        RoomdetailResponse serverResponseRequest = new Gson()
                                .fromJson(datalocal.getResponsejson(), RoomdetailResponse.class);
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

        view.findViewById(R.id.xbtnCarpetdetail).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //if (carpetAreaData != null && carpetAreaData.size() > 0) {
                        //	Bundle bundledata = new Bundle();
                        Intent intent = new Intent(getActivity(), Carpetdetails.class);
                        //bundledata.putSerializable("CARPET_AREA",(Serializable) carpetAreaData);
                        //intent.putExtras(bundledata);
                        startActivity(intent);
                        //	}

                    }
                });

        view.findViewById(R.id.xbtnFlowerbeddetail).setOnClickListener(
                new OnClickListener() {

                    public void onClick(View v) {

                        //Bundle bundledata = new Bundle();
                        Intent intent = new Intent(getActivity(), Flowerbeddetails.class);
                        //bundledata.putSerializable("CARPET_FLOOER",(Serializable) floorAreaData);
                        //intent.putExtras(bundledata);

                        startActivity(intent);

                    }
                });

        ((ListView) view.findViewById(R.id.xlstRoomdetail))
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        baddnewRoomdetail = false;
                        if (roomdetailData != null && roomdetailData.size() > 0)
                            Roomdetailid = roomdetailData.get(position).getRoomDetailsID();
                        displayCustomerDetailDialog(position);

                    }
                });


        return view;
    }

    private void updateandDisplayofflineData(String RequestID) {
        roomDataList = SecondScreenActivity.mypcplData.getRoomAllvalues(AppSetting.getRequestId());
        if (roomDataList != null && roomDataList.size() > 0) {
            if (roomdetailData != null && roomdetailData.size() > 0)
                roomdetailData.clear();
            for (SetOfflinedata dataOffline : roomDataList) {
                if (dataOffline.getSaveuserdata() != null && dataOffline.getSaveuserdata().length() > 0) {
                    RoomdetailResponse serverResponseRequest = new Gson()
                            .fromJson(dataOffline.getSaveuserdata(), RoomdetailResponse.class);


                    for (GetRoomdetailResponse requestdata : serverResponseRequest
                            .getValues()) {
                        roomdetailData.add(requestdata);
                    }
                }


            }

            if (roomdetailData != null && roomdetailData.size() > 0) {
                List<GetRoomdetailResponse> localroomdata = new ArrayList<GetRoomdetailResponse>();
                for (GetRoomdetailResponse datalocal : roomdetailData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestId().equals(AppSetting.getRequestId())) {

                        localroomdata.add(datalocal);
                    }
                }
                if (localroomdata != null && localroomdata.size() > 0)
                    ((ListView) view.findViewById(R.id.xlstRoomdetail)).setAdapter(new RoomDetailAdapter(
                            activity, localroomdata));
            }
        }

    }

    private void initview() {
        view.findViewById(R.id.xbtnadd).setOnClickListener(this);

    }

    public int genNumber() {
        Random r = new Random(System.currentTimeMillis());
        return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
    }

    // Requestdetail Task
    public class RoomdetailAsyncTask extends AsyncTask<String, Void, String> {

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

            return GetWebServiceData.getServerResponse(url
                    + "/GetRoomTabDetails", nameValuePairs, data);

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
                //Log.i("Request", result);
                roomdetailData = new ArrayList<GetRoomdetailResponse>();

                try {
                    JSONObject serverResponsere = new JSONObject(result);
                    Log.d("addResActivity", "Save Review Successful");
                    RoomdetailResponse serverResponseRequest = new Gson()
                            .fromJson(result, RoomdetailResponse.class);

                    String loginResult = serverResponsere.getString("status");
                    if (loginResult.equals("OK")) {
                        Log.d("reuest detail", "get data");

                        //if (result!=null&& AppSetting.getRequestId()!=null) {
                        // SecondScreenActivity.mypcplData.insertRoomdata(result, AppSetting.getRequestId(),Roomdetailid);

                        //}
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

    public void displaydataOffOn(RoomdetailResponse serverResponseRequest) {
        if (serverResponseRequest != null
                && serverResponseRequest.getValues() != null
                && serverResponseRequest.getDoors() != null) {
            for (GetRoomdetailResponse requestdata : serverResponseRequest
                    .getValues()) {
                if (AppSetting.getRequestId().equals(requestdata.getRequestId())) {
                    roomdetailData.add(requestdata);
                }
            }


            for (RequestDropdownData optiondata : serverResponseRequest
                    .getRoomType()) {
                roomTypeData.add(optiondata.getDatavalue());
            }
//            String temp;
//                // remove repetitive data from drop-down
//                for (int i=0; i<(serverResponseRequest.getRoomType().size())-1; i++){
//                    temp = serverResponseRequest.getRoomType().get(i).getDatavalue();
//                    for (int j=i+1; j<serverResponseRequest.getRoomType().size(); j++){
//                        if (serverResponseRequest.getRoomType().get(j).equals(temp)){
//                            roomTypeData.remove(serverResponseRequest.getRoomType().get(j));
//                        }
//                    }
//                }

            for (RequestDropdownData optiondata : serverResponseRequest
                    .getFlooring()) {
                flooringData.add(optiondata.getDatavalue());
            }

            for (RequestDropdownData optiondata : serverResponseRequest
                    .getDoors()) {
                windowTypeData.add(optiondata.getDatavalue());
            }

            for (RequestDropdownData optiondata : serverResponseRequest
                    .getWindowType()) {
                doorsTypeData.add(optiondata.getDatavalue());
            }
            for (RequestDropdownData optiondata : serverResponseRequest
                    .getKitchenType()) {
                kitchenType.add(optiondata.getDatavalue());
            }

            for (RequestDropdownData optiondata : serverResponseRequest
                    .getWCType()) {
                wCTypeData.add(optiondata.getDatavalue());
            }

            if (roomdetailData != null
                    && roomdetailData.size() > 0) {
                ((ListView) view
                        .findViewById(R.id.xlstRoomdetail))
                        .setAdapter(new RoomDetailAdapter(
                                activity, roomdetailData));
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xbtnadd:
                baddnewRoomdetail = true;
                displayCustomerDetailDialog(0);
                break;

            default:
                break;
        }

    }


    protected void displayCustomerDetailDialog(final int position) {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addroominformation_dialog);
        ImageView imgrooms = (ImageView) dialog
                .findViewById(R.id.ximgdropdownrooms);
        ImageView imgflooring = (ImageView) dialog
                .findViewById(R.id.ximgdropdownroomflooring);
        ImageView imgDoors = (ImageView) dialog
                .findViewById(R.id.ximgdropdowndoorsdiscription);
        ImageView imgwindows = (ImageView) dialog
                .findViewById(R.id.ximgdropdownwindowsdiscription);
        ImageView imgkitchenplatform = (ImageView) dialog
                .findViewById(R.id.ximgdropdownkitchenplatform);
        ImageView imgwctype = (ImageView) dialog
                .findViewById(R.id.ximgdropdownwctype);
        edtautoRoom = (AutoCompleteTextView) dialog.findViewById(R.id.xedtautorooms);
        edtautoRoom.setFocusable(true);
        edtautoflooring = (AutoCompleteTextView) dialog.findViewById(R.id.xedtautoroomflooring);
        edtautoflooring.setFocusable(true);
        edtautoDoor = (AutoCompleteTextView) dialog.findViewById(R.id.xedtautodoorsdiscription);
        edtautoDoor.setFocusable(true);
        edtautowindowsdiscription = (AutoCompleteTextView) dialog.findViewById(R.id.xedtautowindowsdiscription);
        edtautowindowsdiscription.setFocusable(true);
        edtautokitchenplatform = (AutoCompleteTextView) dialog.findViewById(R.id.xedtautokitchenplatform);
        edtautokitchenplatform.setFocusable(true);
        edtwctype = (AutoCompleteTextView) dialog.findViewById(R.id.xedtautowctype);
        edtwctype.setFocusable(true);


        if (roomdetailData != null && roomdetailData.size() > 0) {
            if (!baddnewRoomdetail) {
                List<GetRoomdetailResponse> localroomdata = new ArrayList<GetRoomdetailResponse>();
                for (GetRoomdetailResponse datalocal : roomdetailData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestId().equals(AppSetting.getRequestId())) {
                        localroomdata.add(datalocal);
                    }
                }
                if (localroomdata != null && localroomdata.size() > 0) {
                    ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautorooms))
                            .setText(localroomdata.get(position).getRoomType());
                    ((AutoCompleteTextView) dialog
                            .findViewById(R.id.xedtautoroomflooring))
                            .setText(localroomdata.get(position).getFlooringType());
                    ((AutoCompleteTextView) dialog
                            .findViewById(R.id.xedtautodoorsdiscription))
                            .setText(localroomdata.get(position).getDoorType());
                    ((AutoCompleteTextView) dialog
                            .findViewById(R.id.xedtautowindowsdiscription))
                            .setText(localroomdata.get(position).getWindowType());
                    ((AutoCompleteTextView) dialog
                            .findViewById(R.id.xedtautokitchenplatform))
                            .setText(localroomdata.get(position)
                                    .getKitchenPlatformType());
                    ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautowctype))
                            .setText(localroomdata.get(position).getWCType());
                    Roomdetailid = localroomdata.get(position).getRoomDetailsID();
                }
            }
        }
        Button btnsave = (Button) dialog.findViewById(R.id.xbtnok);
        Button btnCancel = (Button) dialog.findViewById(R.id.xbtncancel);
        // btnsendemail.setText("OK");


        if (edtautoRoom != null) {
            lstroomType.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    // if(parent.getItemAtPosition(position).toString()!=null)
                    String selecteditem = parent.getItemAtPosition(position)
                            .toString();
                    edtautoRoom.setText(selecteditem);
                    lstroomType.dismiss();

                }
            });
        }

        if (edtautoflooring != null) {
            lstflooringType.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    // if(parent.getItemAtPosition(position).toString()!=null)
                    String selecteditem = parent.getItemAtPosition(position)
                            .toString();
                    edtautoflooring.setText(selecteditem);
                    lstflooringType.dismiss();

                }
            });
        }

        if (edtautoDoor != null) {
            lstdoorType.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    // if(parent.getItemAtPosition(position).toString()!=null)
                    String selecteditem = parent.getItemAtPosition(position)
                            .toString();
                    edtautoDoor.setText(selecteditem);
                    lstdoorType.dismiss();

                }
            });
        }

        if (edtautowindowsdiscription != null) {
            lstwindowsType.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    // if(parent.getItemAtPosition(position).toString()!=null)
                    String selecteditem = parent.getItemAtPosition(position)
                            .toString();
                    edtautowindowsdiscription.setText(selecteditem);
                    lstwindowsType.dismiss();

                }
            });
        }

        if (edtautokitchenplatform != null) {
            lstkitchenType.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    // if(parent.getItemAtPosition(position).toString()!=null)
                    String selecteditem = parent.getItemAtPosition(position)
                            .toString();
                    edtautokitchenplatform.setText(selecteditem);
                    lstkitchenType.dismiss();

                }
            });
        }

        if (edtwctype != null) {
            lstWcType.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    // if(parent.getItemAtPosition(position).toString()!=null)
                    String selecteditem = parent.getItemAtPosition(position)
                            .toString();
                    edtwctype.setText(selecteditem);
                    lstWcType.dismiss();

                }
            });
        }

        imgrooms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomTypeData != null && roomTypeData.size() > 0)
                    lstroomType.setAdapter(new LocationDetailAdapter(
                            activity, roomTypeData));
                lstroomType.setAnchorView(edtautoRoom);
                lstroomType.setModal(true);
                lstroomType.show();

            }
        });

        imgflooring.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flooringData != null && flooringData.size() > 0)
                    lstflooringType.setAdapter(new LocationDetailAdapter(
                            activity, flooringData));
                lstflooringType.setAnchorView(edtautoflooring);
                lstflooringType.setModal(true);
                lstflooringType.show();

            }
        });


        imgDoors.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doorsTypeData != null && doorsTypeData.size() > 0)
                    lstdoorType.setAdapter(new LocationDetailAdapter(
                            activity, doorsTypeData));
                lstdoorType.setAnchorView(edtautoDoor);
                lstdoorType.setModal(true);
                lstdoorType.show();

            }
        });


        imgwindows.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (windowTypeData != null && windowTypeData.size() > 0)
                    lstwindowsType.setAdapter(new LocationDetailAdapter(
                            activity, windowTypeData));
                lstwindowsType.setAnchorView(edtautowindowsdiscription);
                lstwindowsType.setModal(true);
                lstwindowsType.show();

            }
        });

        imgkitchenplatform.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kitchenType != null && kitchenType.size() > 0)
                    lstkitchenType.setAdapter(new LocationDetailAdapter(
                            activity, kitchenType));
                lstkitchenType.setAnchorView(((AutoCompleteTextView) dialog
                        .findViewById(R.id.xedtautokitchenplatform)));
                lstkitchenType.setModal(true);
                lstkitchenType.show();

            }
        });

        imgwctype.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wCTypeData != null && wCTypeData.size() > 0)
                    lstWcType.setAdapter(new LocationDetailAdapter(
                            activity, wCTypeData));
                lstWcType.setAnchorView(edtwctype);
                lstWcType.setModal(true);
                lstWcType.show();

            }
        });

        try {
            setAutoFillData(dialog);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error :" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        // if button is clicked, close the custom dialog
        btnsave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GetWebServiceData.isNetworkAvailable(getActivity())) {
                    new SetRoomDetailAsyncTask(
                            activity,
                            ((AutoCompleteTextView) dialog
                                    .findViewById(R.id.xedtautorooms))
                                    .getText().toString(),
                            ((AutoCompleteTextView) dialog
                                    .findViewById(R.id.xedtautoroomflooring))
                                    .getText().toString(),
                            ((AutoCompleteTextView) dialog
                                    .findViewById(R.id.xedtautodoorsdiscription))
                                    .getText().toString(),
                            ((AutoCompleteTextView) dialog
                                    .findViewById(R.id.xedtautowindowsdiscription))
                                    .getText().toString(),
                            ((AutoCompleteTextView) dialog
                                    .findViewById(R.id.xedtautokitchenplatform))
                                    .getText().toString(),
                            ((AutoCompleteTextView) dialog
                                    .findViewById(R.id.xedtautowctype))
                                    .getText().toString()).execute();
                } else {

                    //List<NameValuePair> nameValuePairs = null;
                    List<GetRoomdetailResponse> roomTab = null;

                    try {
                        //nameValuePairs = new ArrayList<NameValuePair>(1);
                        roomTab = new ArrayList<GetRoomdetailResponse>();
                        if (!baddnewRoomdetail && roomdetailData != null && roomdetailData.size() > 0) {
                            Roomdetailid = roomdetailData.get(position).getRoomDetailsID();
                        } else {
                            Roomdetailid = String.valueOf(genNumber());
                        }
                    } catch (Exception e) {
                    }
					/*nameValuePairs=saveSetdataoffline(nameValuePairs,Roomdetailid,((AutoCompleteTextView) dialog
							.findViewById(R.id.xedtautorooms))
							.getText().toString(),
					((AutoCompleteTextView) dialog
							.findViewById(R.id.xedtautoroomflooring))
							.getText().toString(),
					((AutoCompleteTextView) dialog
							.findViewById(R.id.xedtautodoorsdiscription))
							.getText().toString(),
					((AutoCompleteTextView) dialog
							.findViewById(R.id.xedtautowindowsdiscription))
							.getText().toString(),
					((AutoCompleteTextView) dialog
							.findViewById(R.id.xedtautokitchenplatform))
							.getText().toString(),
					((AutoCompleteTextView) dialog
							.findViewById(R.id.xedtautowctype))
							.getText().toString());*/


                    roomTab.add(new GetRoomdetailResponse(AppSetting.getRequestId(), ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautorooms)).getText().toString(), ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautoroomflooring)).getText().toString(), ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautodoorsdiscription)).getText().toString(),
                            ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautowindowsdiscription)).getText().toString(), ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautokitchenplatform)).getText().toString(), ((AutoCompleteTextView) dialog
                            .findViewById(R.id.xedtautowctype))
                            .getText().toString(), Roomdetailid));

                    if (roomTab != null && roomTab.size() > 0) {
                        displayOfflineSaveDataDialog(roomTab);
                    }
                    AppCommonDialog.showSimpleDialog(getActivity(),
                            getResources().getString(R.string.app_name),
                            getResources().getString(R.string.check_network),
                            getResources().getString(R.string.ok), "OK");

                }
                dialog.dismiss();

            }

        });

        dialog.show();

    }

    public void setAutoFillData(Dialog dialog) {
        String[] tmp1 = new String[roomTypeData.size()];
        tmp1 = roomTypeData.toArray(tmp1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp1);
        ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautorooms)).setAdapter(adapter1);

        String[] tmp2 = new String[flooringData.size()];
        tmp2 = flooringData.toArray(tmp2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp2);
        ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautoroomflooring)).setAdapter(adapter2);

        String[] tmp3 = new String[doorsTypeData.size()];
        tmp3 = doorsTypeData.toArray(tmp3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp3);
        ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautodoorsdiscription)).setAdapter(adapter3);

        String[] tmp4 = new String[windowTypeData.size()];
        tmp4 = windowTypeData.toArray(tmp4);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp4);
        ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautowindowsdiscription)).setAdapter(adapter4);

        String[] tmp5 = new String[kitchenType.size()];
        tmp5 = kitchenType.toArray(tmp5);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp5);
        ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautokitchenplatform)).setAdapter(adapter5);

        String[] tmp6 = new String[wCTypeData.size()];
        tmp6 = wCTypeData.toArray(tmp6);
        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp6);
        ((AutoCompleteTextView) dialog.findViewById(R.id.xedtautowctype)).setAdapter(adapter6);
    }

    protected void displayOfflineSaveDataDialog(final List<GetRoomdetailResponse> roomTab) {
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
                boolean bupdate = false;
                Gson gson = new Gson();
                String strRequest = "";
                RoomdetailResponse data = new RoomdetailResponse();
                data.setRemarks("yes");
                data.setStatus("sucessfully");
                data.setValues(roomTab);
                strRequest = gson.toJson(data);
                if (!baddnewRoomdetail && Roomdetailid != null) {
                    bupdate = SecondScreenActivity.mypcplData.updateRoomdata(AppSetting.getRequestId(), Roomdetailid, strRequest);
                } else {
                    if (Roomdetailid == null) {
                        Roomdetailid = String.valueOf(genNumber());
                    }
                    SecondScreenActivity.mypcplData.insertRoomdata(strRequest, AppSetting.getRequestId(), Roomdetailid);
                }
                if (bupdate) {
                    Toast.makeText(getActivity(),
                            "sucessfully store data  ",
                            Toast.LENGTH_SHORT).show();

                }
                updateandDisplayofflineData(AppSetting.getRequestId());
                dialog.dismiss();
            }

        });
        dialog.show();*/

        boolean bupdate = false;
        Gson gson = new Gson();
        String strRequest = "";
        RoomdetailResponse data = new RoomdetailResponse();
        data.setRemarks("yes");
        data.setStatus("sucessfully");
        data.setValues(roomTab);
        strRequest = gson.toJson(data);
        if (!baddnewRoomdetail && Roomdetailid != null) {
            bupdate = SecondScreenActivity.mypcplData.updateRoomdata(AppSetting.getRequestId(), Roomdetailid, strRequest);
        } else {
            if (Roomdetailid == null) {
                Roomdetailid = String.valueOf(genNumber());
            }
            SecondScreenActivity.mypcplData.insertRoomdata(strRequest, AppSetting.getRequestId(), Roomdetailid);
        }
        if (bupdate) {
            Toast.makeText(getActivity(),
                    "sucessfully store data  ",
                    Toast.LENGTH_SHORT).show();
        }else if(!bupdate){
            SecondScreenActivity.mypcplData.insertRoomdata(strRequest, AppSetting.getRequestId(),Roomdetailid);
            Toast.makeText(getActivity(), "sucessfully store data offline  ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData(AppSetting.getRequestId());
        }
        updateandDisplayofflineData(AppSetting.getRequestId());


    }

    // Requestdetail Task
    public class SetRoomDetailAsyncTask extends AsyncTask<String, Void, String> {
        String stringRoomtype;
        String strRoomFloor;
        String strDoorType;
        String strWindowstype;
        String strKitchantype;
        String strWcType;

        public SetRoomDetailAsyncTask(Activity activity, String stringRoomtype,
                                      String strRoomFloor, String strDoorType, String strWindowstype,
                                      String strKitchantype, String strWcType) {

            this.stringRoomtype = stringRoomtype;
            this.strRoomFloor = strRoomFloor;
            this.strDoorType = strDoorType;
            this.strWindowstype = strWindowstype;
            this.strKitchantype = strKitchantype;
            this.strWcType = strWcType;
        }

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

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String data = "";
            List<NameValuePair> nameValuePairs = null;

            try {
                nameValuePairs = new ArrayList<NameValuePair>(22);
            } catch (Exception e) {

            }
            nameValuePairs = saveSetdataoffline(nameValuePairs, Roomdetailid, stringRoomtype, strRoomFloor, strDoorType, strWindowstype, strKitchantype, strWcType);


            // Log.d("request", +);
            return GetWebServiceData.getServerResponse(url
                    + "/SetRoomDetails", nameValuePairs, data);

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
                        if (GetWebServiceData.isNetworkAvailable(getActivity())) {

                            new RoomdetailAsyncTask().execute();

                        } else {

                            AppCommonDialog.showSimpleDialog(getActivity(), getResources()
                                            .getString(R.string.app_name),
                                    getResources().getString(R.string.check_network),
                                    getResources().getString(R.string.ok), "OK");
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

    public List<NameValuePair> saveSetdataoffline(
            List<NameValuePair> nameValuePairs, String roomdetailid2,
            String stringRoomtype, String strRoomFloor, String strDoorType,
            String strWindowstype, String strKitchantype, String strWcType) {
        if (baddnewRoomdetail) {
            nameValuePairs.add(new BasicNameValuePair("RoomDetailsID", ""));
        } else {
            nameValuePairs.add(new BasicNameValuePair("RoomDetailsID", Roomdetailid));
        }
        nameValuePairs.add(new BasicNameValuePair("RequestId", AppSetting.getRequestId()));
        nameValuePairs.add(new BasicNameValuePair("RoomType",
                stringRoomtype));
        nameValuePairs.add(new BasicNameValuePair("FlooringType",
                strRoomFloor));
        nameValuePairs.add(new BasicNameValuePair("DoorType",
                strDoorType));
        nameValuePairs.add(new BasicNameValuePair("WindowType",
                strWindowstype));
        nameValuePairs.add(new BasicNameValuePair(
                "KitchenPlatformType", strKitchantype));
        nameValuePairs.add(new BasicNameValuePair("WCType", strWcType));

        return nameValuePairs;
    }


}
