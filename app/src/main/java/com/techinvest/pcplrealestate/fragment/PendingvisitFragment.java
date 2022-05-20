package com.techinvest.pcplrealestate.fragment;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import static com.techinvest.pcpl.commonutil.AppDetails.getActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.SecondScreenActivity;
import com.techinvest.pcplrealestate.Services;
import com.techinvest.pcplrealestate.R.id;
import com.techinvest.pcplrealestate.R.layout;
import com.techinvest.pcplrealestate.R.string;
import com.techinvest.pcplrealestate.R.style;
import com.techinvest.pcplrealestate.fragment.CompletedVisitFragment.ClientAsyncTask;
import com.techinvest.pcpl.adapter.CustomArrayAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.OfflineSynStatus;
import com.techinvest.pcpl.model.PendingDataResponse;
import com.techinvest.pcpl.model.PendingVisit;
import com.techinvest.pcpl.model.RequestDetailsResponse;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;


public class PendingvisitFragment extends Fragment {
    private ProgressDialog loadingDialog;
    private ListView listView;
    GridView gride;
    CustomArrayAdapter adapter;
    ArrayList<PendingVisit> mClient = null;
    public ProgressDialog progressDialog;
    String user;
    String url = AppSetting.getapiURL();
    PendingVisit pendingVisit;
    String name;
    JSONArray jsonData;
    JSONObject jRealObject;
    SharedPreferences mPrefs;
    Activity activity;
    SwipeRefreshLayout swipeLayout;

    List<OfflineSynStatus> sysOfflineStatus = null;
    EditText editsearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.pendingvisits, container, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(id.swiperefresh);

        listView = (ListView) view.findViewById(id.xlstPendinglist);
        editsearch = (EditText) view.findViewById(R.id.searchs);

        mClient = new ArrayList<PendingVisit>();
        sysOfflineStatus = new ArrayList<OfflineSynStatus>();
        sysOfflineStatus = SecondScreenActivity.mypcplData.getSysOfflineStatus();
        mPrefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        adapter = new CustomArrayAdapter(getActivity(), mClient, sysOfflineStatus);

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ClientAsyncTask().execute();
            }
        });

        //  if(AppSetting.getUserId()!=null)
        //	url = "http://206.183.111.109/PCPLWebServices/Service1.asmx/SHOW_DATA_ON_ENGGUSERID?EngguserId=" +AppSetting.getUserId()+"&flag=IF";
        // http://206.183.111.109/PCPLWebServices/Service1.asmx/SHOW_DATA_ON_ENGGUSERID?EngguserId=1197d845-0b45-4c20-afcc-12ef2bdbcf56&flag=IF

        if (GetWebServiceData.isNetworkAvailable(getActivity())) {
            new ClientAsyncTask().execute();
        } else {

            String json = mPrefs.getString("PendingList", "");
            //  MyObject obj = gson.fromJson(json, MyObject.class);
            if (json != null) {
                PendingDataResponse serverResponserestaurant = new Gson()
                        .fromJson(json, PendingDataResponse.class);

                for (PendingVisit pendingvisit : serverResponserestaurant.getJj()) {
                    mClient.add(pendingvisit);
                }
                if (mClient != null && mClient.size() > 0) {
                    adapter = new CustomArrayAdapter(getActivity(), mClient, sysOfflineStatus);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            AppCommonDialog.showSimpleDialog(getActivity(), getResources()
                            .getString(string.app_name), getResources()
                            .getString(string.check_network),
                    getResources().getString(string.ok), "OK");
        }

        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
        });
        return view;
    }

    // WriteReviewTask
    public class ClientAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (GetWebServiceData.isNetworkAvailable(getActivity())) {
                    loadingDialog = AppCommonDialog.createProgressDialog(getActivity());
                } else {
                    AppCommonDialog.showSimpleDialog(getActivity(), getResources()
                            .getString(string.app_name), getResources()
                            .getString(string.check_network), getResources().getString(string.ok), "OK");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            List<NameValuePair> nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("EngguserId", AppSetting.getUserId()));
            nameValuePairs.add(new BasicNameValuePair("flag", "ALL"));

            return GetWebServiceData.getServerResponse(url + "/SHOW_DATA_ON_ENGGUSERID", nameValuePairs, data);///009 shashank

            // Write this code for multiple id send as commo  seperator
        /*		try {
            //response = ServerConnection.getServerResponse(data);
			response = GetWebServiceData.getWebServiceData(url);
			 // return response;
		     }catch (Exception e) {
				
			}*/
            //return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            }

            if (result != null) {
                Log.i("Request", result);
                //	if (loginResult) {
                Log.d("addResActivity", "Save Review Successful");
                PendingDataResponse serverResponserestaurant = new Gson()
                        .fromJson(result, PendingDataResponse.class);

                if (serverResponserestaurant != null && serverResponserestaurant.getJj() != null) {
                    Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    //String json = gson.toJson(requestgetData);
                    prefsEditor.putString("PendingList", result);
                    prefsEditor.commit();

                    mClient.clear();
                    for (PendingVisit pendingvisit : serverResponserestaurant.getJj()) {
                        mClient.add(pendingvisit);
                    }

                    if (mClient != null && mClient.size() > 0) {
                        if (getActivity() != null) {
                            adapter = new CustomArrayAdapter(getActivity(), mClient, sysOfflineStatus);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                    /*} else {

						AppCommonDialog.showSimpleDialog(getActivity(), getResources()
								.getString(R.string.app_name), "data not found",
								getResources().getString(R.string.ok), "OK");
					}*/
                swipeLayout.setRefreshing(false);
            }
            swipeLayout.setRefreshing(false);
        }

    } // Ended Readall Task

    @SuppressLint("WrongViewCast")
    protected void displayCustomerDetailDialog(int position) {

        final Dialog dialog = new Dialog(getActivity(), style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout.customer_popupdialog);
        TextView textTitle = (TextView) dialog.findViewById(id.xtxttitle);
        textTitle.setText("Customer Detail");
        // set the custom dialog components - text, image and button
        if (mClient != null && mClient.size() > 0) {
            ((EditText) dialog.findViewById(id.xedtCustomer)).setText(mClient.get(position).getApplicantName());
            ((EditText) dialog.findViewById(id.xedtMobileNo)).setText(mClient.get(position).getApplicantPhoneNo());
            ((EditText) dialog.findViewById(id.xedtAddress)).setText(mClient.get(position).getBuildingName());
        }

        Button btnproceed = (Button) dialog.findViewById(id.xbtnok);
        Button btnCancel = (Button) dialog.findViewById(id.xbtncancel);
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
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), Services.class);
                startActivity(intent);
            }

        });
        dialog.show();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;
    }
}
