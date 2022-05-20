package com.techinvest.pcplrealestate.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.SecondScreenActivity;
import com.techinvest.pcplrealestate.R.id;
import com.techinvest.pcplrealestate.R.layout;
import com.techinvest.pcplrealestate.R.string;
import com.techinvest.pcpl.adapter.CustomArrayAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.OfflineSynStatus;
import com.techinvest.pcpl.model.PendingDataResponse;
import com.techinvest.pcpl.model.PendingVisit;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;

public class CompletedVisitFragment extends Fragment
{
	ListView listView;
	GridView gride;
	CustomArrayAdapter adapter;
	ArrayList<PendingVisit> mClient;


	String url = AppSetting.getapiURL();
	private ProgressDialog loadingDialog;
	SharedPreferences  mPrefs;
	Activity activity;
	List<OfflineSynStatus> sysOfflineStatus;

	// public static final String PREFS_NAME = "MyApp_Settings";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.completedvisits,container, false);

		listView = (ListView)view.findViewById(R.id.listView1);
		mClient = new ArrayList<PendingVisit>();
		sysOfflineStatus=new ArrayList<OfflineSynStatus>();
		if(sysOfflineStatus!=null)
			sysOfflineStatus=SecondScreenActivity.mypcplData.getSysOfflineStatus();
		mPrefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);


		//if(AppSetting.getUserId()!=null)
		//     url = "http://206.183.111.109/PCPLWebServices/Service1.asmx/SHOW_DATA_ON_ENGGUSERID?EngguserId=" +AppSetting.getUserId()+"&flag=CF";

		if (GetWebServiceData.isNetworkAvailable(getActivity())) {

			new ClientAsyncTask().execute();

		} else {

			String json = mPrefs.getString("CompleteList", "");
			//  MyObject obj = gson.fromJson(json, MyObject.class);
			if(json!=null){
				PendingDataResponse serverResponserestaurant = new Gson()
						.fromJson(json, PendingDataResponse.class);

				for(PendingVisit pendingvisit:serverResponserestaurant.getJj()){
					mClient.add(pendingvisit);
				}
				if(mClient!=null&&mClient.size()>0){

					adapter = new CustomArrayAdapter(getActivity(), mClient,sysOfflineStatus);
					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();


				}
			}

			AppCommonDialog.showSimpleDialog(getActivity(), getResources()
							.getString(R.string.app_name), getResources()
							.getString(R.string.check_network),
					getResources().getString(R.string.ok), "OK");
		}


		return view;
	}





	// WriteReviewTask
	public class ClientAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// strRes = activity.getResources();
			try {
				if (GetWebServiceData.isNetworkAvailable(getActivity())) {

					loadingDialog = AppCommonDialog
							.createProgressDialog(getActivity());

				} else {

					AppCommonDialog.showSimpleDialog(getActivity(), getResources()
									.getString(R.string.app_name), getResources()
									.getString(R.string.check_network),
							getResources().getString(R.string.ok), "OK");
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			/*String response = null;	
				try {
			//response = ServerConnection.getServerResponse(data);
			response = GetWebServiceData.getWebServiceData(url);
			 // return response;
		     }catch (Exception e) {
				
			}
				return response;*/

			String data = "";
			List<NameValuePair> nameValuePairs = null;


			nameValuePairs = new ArrayList<NameValuePair>(2);
			if(AppSetting.getUserId()!=null)
				nameValuePairs.add(new BasicNameValuePair("EngguserId",AppSetting.getUserId()));
			nameValuePairs.add(new BasicNameValuePair("flag","CF"));

			//Log.d("LoginActivity", "Attempting Signup");
			return GetWebServiceData.getServerResponse(url
					+ "/SHOW_DATA_ON_ENGGUSERID", nameValuePairs, data);

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
					JSONObject addresObject = new JSONObject(result);
					//	Boolean loginResult = addresObject.getBoolean("status");

					//	if (loginResult) {
					Log.d("addResActivity", "Save Review Successful");
					PendingDataResponse serverResponserestaurant = new Gson()
							.fromJson(result, PendingDataResponse.class);

					if (serverResponserestaurant != null && serverResponserestaurant.getJj()!=null) {

						Editor prefsEditor = mPrefs.edit();
						Gson gson = new Gson();
						//String json = gson.toJson(requestgetData);
						prefsEditor.putString("CompleteList", result);
						prefsEditor.commit();
						for(PendingVisit pendingvisit:serverResponserestaurant.getJj()){
							mClient.add(pendingvisit);
						}
						if(mClient!=null&&mClient.size()>0){
							adapter = new CustomArrayAdapter(getActivity(), mClient,sysOfflineStatus);
							listView.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}

					}
					/*} else {
			
						AppCommonDialog.showSimpleDialog(getActivity(), getResources()
								.getString(R.string.app_name), "data not found",
								getResources().getString(R.string.ok), "OK");
					}*/

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("LoginActivity", "JSON Result parse error");
					AppCommonDialog.showSimpleDialog(getActivity(), getResources()
									.getString(R.string.app_name), getResources()
									.getString(R.string.jsonerror_message),
							getResources().getString(R.string.ok), "OK");
					e.printStackTrace();

				}

			}
		}

	} // Ended  Task
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*public class ClientAsyncTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected void onPreExecute() {

			progressDialog = new ProgressDialog(getActivity());
			
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			progressDialog.setMessage("Loading application View, please wait...");
			
			progressDialog.setCancelable(false);
			
			progressDialog.setIndeterminate(false);
			
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				
				synchronized (this)
				{
					getWebApiData(url);
					
				}
				return true;
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{  
			super.onPostExecute(result);
	 		progressDialog.dismiss();

			if (result == true) {
				CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), R.layout.single_item, mClient);
				listView.setAdapter(adapter);
			} 
			else
			{
				Toast.makeText(getActivity(), "Data is null ",Toast.LENGTH_LONG).show();

			}
		}
	}

	private void getWebApiData(String dataUrl)
	{

		try {

			String apidata = GetWebServiceData.getWebServiceData(dataUrl);

			
			if (apidata != null) {

				JSONObject JResponse = new JSONObject(apidata);

				//Log.i("complite visits", "getting json object data" + JResponse);

				JSONArray jsonData = JResponse.getJSONArray("jj");
				Log.e("complite visits", "jj:" + jsonData);

				for (int j = 0; j < jsonData.length(); j++) {
					PendingVisit pendingVisit = new PendingVisit();
					JSONObject jRealObject = jsonData.getJSONObject(j);
					
					pendingVisit.setClient(jRealObject.getString("ClientName"));
					pendingVisit.setCustomer(jRealObject.getString("ApplicantName"));
					pendingVisit.setContact(jRealObject.getString("AreaOrWard"));
					pendingVisit.setAera(jRealObject.getString("BranchName"));

					mClient.add(pendingVisit);
					Log.e("Pending Visits", "ClientName:" + pendingVisit.getClient());
					Log.e("Pending Visits", "ApplicantName:" + pendingVisit.getCustomer());
					Log.e("Pending Visits", "AreaOrWard:" + pendingVisit.getAera());
					Log.e("Pending Visits", "BranchName:" + pendingVisit.getContact());
				}

			}
			else 
			{
				Toast.makeText(getActivity(), "No record found", Toast.LENGTH_LONG).show();
			}
		}

		catch (Exception e)
		{
			e.printStackTrace();
			
		}
	}*/

}
