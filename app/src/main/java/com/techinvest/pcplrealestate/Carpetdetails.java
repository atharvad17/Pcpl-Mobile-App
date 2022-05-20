package com.techinvest.pcplrealestate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techinvest.pcpl.adapter.CarpetAreaAdapter;
import com.techinvest.pcpl.adapter.RoomDetailAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppDetails;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.CarpetResponse;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.GetCarpetAreaResponse;
import com.techinvest.pcpl.model.GetRoomdetailResponse;
import com.techinvest.pcpl.model.Offlinedatamodel;
import com.techinvest.pcpl.model.RequestDropdownData;
import com.techinvest.pcpl.model.RoomdetailResponse;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcplrealestate.Roomdetails.RoomdetailAsyncTask;
import com.techinvest.pcplrealestate.Roomdetails.SetRoomDetailAsyncTask;

public class  Carpetdetails extends Activity  implements OnClickListener{


	List<GetCarpetAreaResponse>carpetAreaData;
	//Activity activity;
	private ProgressDialog loadingDialog;
	String roomCalDetailID="";
	private boolean baddNewcarpetArea;
	String strLendth;
	String strWidth;
	String strTotalArea;
	String url = AppSetting.getapiURL();
	float totalArea;
	List<Offlinedatamodel> offlineData;
	List<SetOfflinedata> carpetDataList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carpetareanew);
		prepareActivity();
		
		/*Bundle data=getIntent().getExtras();
		if(data!=null){
			carpetAreaData=(List<GetCarpetAreaResponse>) data.getSerializable("CARPET_AREA");
		}*/
		initview();
		offlineData=new ArrayList<Offlinedatamodel>();
		carpetDataList = new ArrayList<SetOfflinedata>();
		if(offlineData!=null){
			offlineData=SecondScreenActivity.mypcplData.getCarpetDetail(AppSetting.getRequestId());
		}
		((TextView)findViewById(R.id.xtxtheaderTittle)).setText(getResources().getString(R.string.title_activity_carpetdetails));
		((ImageButton)findViewById(R.id.backId)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		if (GetWebServiceData.isNetworkAvailable(Carpetdetails.this)) {

			new CarpetAsyncTask().execute();

		} else {

			carpetAreaData = new ArrayList<GetCarpetAreaResponse>();
			if(offlineData.size()>0){
				for(Offlinedatamodel datalocal:offlineData){
					if(AppSetting.getRequestId()!=null&& datalocal.getRequestid().equals(AppSetting.getRequestId())){

						CarpetResponse serverResponseRequest = new Gson()
								.fromJson(datalocal.getResponsejson(), CarpetResponse.class);
						displaydataOffOn(serverResponseRequest);
					}
				}
			}
			// updateandDisplayofflineData();

			AppCommonDialog.showSimpleDialog(Carpetdetails.this, getResources()
							.getString(R.string.app_name),
					getResources().getString(R.string.check_network),
					getResources().getString(R.string.ok), "OK");
		}


      /*  if(carpetAreaData!=null  && carpetAreaData.size()>0){
			((ListView)findViewById(R.id.xlstCarpetArea)).setAdapter(new CarpetAreaAdapter(Carpetdetails.this,carpetAreaData));
		}*/

		((ListView)findViewById(R.id.xlstCarpetArea)).setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				baddNewcarpetArea=false;
				roomCalDetailID=carpetAreaData.get(position).getRoomDetailsIDs();
				if(roomCalDetailID==null){
					roomCalDetailID=String.valueOf(genNumber());
				}
				displayCustomerDetailDialog(position);


			}
		});


	}

	private void updateandDisplayofflineData(String RequestID) {
		//Carpet data get
		carpetDataList=SecondScreenActivity.mypcplData.getCarpetRequestIdvalues(AppSetting.getRequestId());
		if(carpetDataList!=null&&carpetDataList.size()>0){
			if(carpetAreaData!=null&& carpetAreaData.size()>0)
				carpetAreaData.clear();
			for(SetOfflinedata dataOffline:carpetDataList){
				if(dataOffline.getSaveuserdata()!=null&&dataOffline.getSaveuserdata().length()>0){
					CarpetResponse serverResponseRequest = new Gson()
							.fromJson(dataOffline.getSaveuserdata(), CarpetResponse.class);

					for (GetCarpetAreaResponse requestdata : serverResponseRequest
							.getValuesCarpetArea()) {
						if(AppSetting.getRequestId()!=null&& requestdata.getRequestId().equals(AppSetting.getRequestId())){
							carpetAreaData.add(requestdata);
						}
					}
				}
			}
			if(carpetAreaData!=null&&carpetAreaData.size()>0){
				List<GetCarpetAreaResponse>carpetdatadisplay=new ArrayList<GetCarpetAreaResponse>();
				for(GetCarpetAreaResponse datalocal:carpetAreaData){
					if(AppSetting.getRequestId()!=null&& datalocal.getRequestId().equals(AppSetting.getRequestId())){
						carpetdatadisplay.add(datalocal);
					}
				}
				if(carpetdatadisplay!=null&&carpetdatadisplay.size()>0){
					((ListView)findViewById(R.id.xlstCarpetArea)).setAdapter(new CarpetAreaAdapter(Carpetdetails.this,carpetdatadisplay));
					((TextView)findViewById(R.id.xtxttotalamount)).setText(getTotalArea(carpetdatadisplay));
				}
			}
		}

	}

	private void prepareActivity() {
		AppDetails.setContext(this);
		AppDetails.setActivity(this);

	}

	private void initview() {
		findViewById(R.id.xbtnSave).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.xbtnSave:
				baddNewcarpetArea=true;
				displayCustomerDetailDialog(0);
				break;

			default:
				break;
		}

	}



	// Requestdetail Task
	public class CarpetAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				loadingDialog = new ProgressDialog(Carpetdetails.this);
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
					+ "/GetCarpetDetails", nameValuePairs, data);

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

				carpetAreaData = new ArrayList<GetCarpetAreaResponse>();

				try {
					JSONObject serverResponsere = new JSONObject(result);
					Log.d("addResActivity", "Save Review Successful");
					CarpetResponse serverResponseRequest = new Gson()
							.fromJson(result, CarpetResponse.class);

					String loginResult = serverResponsere.getString("status");
					if (loginResult.equals("OK")) {
						Log.d("reuest detail", "get data");

						//if (result!=null&& AppSetting.getRequestId()!=null) {
						// SecondScreenActivity.mypcplData.insertCarpetdata(result, AppSetting.getRequestId(),roomCalDetailID);

						//	}
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


	protected void displayCustomerDetailDialog(final int position) {

		final Dialog dialog = new Dialog(Carpetdetails.this);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.carpetarea_dialog);
		//TextView textTitle = (TextView) dialog.findViewById(R.id.xtxttitle);


		((EditText)dialog.findViewById(R.id.xedtlength)).addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				strLendth= s.toString();
				if(strLendth!=null&&strLendth.length()>0 &&strWidth!=null&&strWidth.length()>0){
					totalArea= Float.valueOf(strLendth)*Float.valueOf(strWidth);
					strTotalArea=String.valueOf(totalArea);
					((EditText)dialog.findViewById(R.id.xedtTotalarea)).setText(strTotalArea);
				}else{
					strTotalArea =((EditText)dialog.findViewById(R.id.xedtTotalarea)).getText().toString();
				}
			}
		});

		((EditText)dialog.findViewById(R.id.xedtwidth)).addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				strWidth= s.toString();

				if(strLendth!=null&&strLendth.length()>0 &&strWidth!=null&&strWidth.length()>0){
					// totalArea= Integer.parseInt(strLendth)*Integer.parseInt(strWidth);
					totalArea= Float.valueOf(strLendth)*Float.valueOf(strWidth);
					strTotalArea=String.valueOf(totalArea);
					((EditText)dialog.findViewById(R.id.xedtTotalarea)).setText(strTotalArea);
				}else{
					strTotalArea =((EditText)dialog.findViewById(R.id.xedtTotalarea)).getText().toString();
				}
			}
		});


		if(carpetAreaData!=null  && carpetAreaData.size()>0){
			if(!baddNewcarpetArea){
				List<GetCarpetAreaResponse>carpetdatadisplay=new ArrayList<GetCarpetAreaResponse>();
				for(GetCarpetAreaResponse datalocal:carpetAreaData){
					if(AppSetting.getRequestId()!=null&& datalocal.getRequestId().equals(AppSetting.getRequestId())){
						carpetdatadisplay.add(datalocal);
					}
				}
				if(carpetdatadisplay!=null&&carpetdatadisplay.size()>0){
					((EditText)dialog.findViewById(R.id.xedtcarpetarea)).setText(carpetdatadisplay.get(position).getNames());
					((EditText)dialog.findViewById(R.id.xedtlength)).setText(carpetdatadisplay.get(position).getLengths());
					((EditText)dialog.findViewById(R.id.xedtwidth)).setText(carpetdatadisplay.get(position).getBreadths());
					((EditText)dialog.findViewById(R.id.xedtTotalarea)).setText(carpetdatadisplay.get(position).getArea());
					roomCalDetailID=carpetdatadisplay.get(position).getRoomDetailsIDs();
				}
			}
			strLendth=((EditText)dialog.findViewById(R.id.xedtlength)).getText().toString();
			strWidth=((EditText)dialog.findViewById(R.id.xedtwidth)).getText().toString();
			strTotalArea =((EditText)dialog.findViewById(R.id.xedtTotalarea)).getText().toString();
			// roomCalDetailID=carpetAreaData.get(position).getRoomDetailsIDs();
			//strTotalArea=String.valueOf(totalArea);


			if(strLendth!=null&&strLendth.length()>0 &&strWidth!=null&&strWidth.length()>0){
				//totalArea= Integer.parseInt(strLendth)*Integer.parseInt(strWidth);
				totalArea= Float.valueOf(strLendth)*Float.valueOf(strWidth);
				strTotalArea=String.valueOf(totalArea);
				((EditText)dialog.findViewById(R.id.xedtTotalarea)).setText(strTotalArea);
			}else{
				strTotalArea =((EditText)dialog.findViewById(R.id.xedtTotalarea)).getText().toString();
			}
			//((TextView)dialog.findViewById(R.id.xedtautowindowsdiscription)).setText(carpetAreaData.get(position).get);

		}
		Button btnsave = (Button) dialog
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
		btnsave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (GetWebServiceData.isNetworkAvailable(Carpetdetails.this)) {
					if(validateForm(dialog)){
						new SetRoomDetailAsyncTask(Carpetdetails.this,((EditText)dialog.findViewById(R.id.xedtcarpetarea)).getText().toString(),
								strLendth,strWidth,strTotalArea).execute();
					}


				} else {

					//List<NameValuePair> nameValuePairs = null;
					List<GetCarpetAreaResponse> carpetTab = null;
					try {
						//nameValuePairs = new ArrayList<NameValuePair>(1);
						carpetTab=new ArrayList<GetCarpetAreaResponse>();
						if(!baddNewcarpetArea&&carpetAreaData!=null&&carpetAreaData.size()>0){
							roomCalDetailID=carpetAreaData.get(position).getRoomDetailsIDs();
						}else{
							roomCalDetailID=String.valueOf(genNumber());
						}
					} catch (Exception e) {
					}
					if(validateForm(dialog)){
						// nameValuePairs=saveSetdataoffline(nameValuePairs,strTotalArea);

						carpetTab.add(new GetCarpetAreaResponse(AppSetting.getRequestId(),roomCalDetailID, strLendth, strWidth, strTotalArea, ((EditText)dialog.findViewById(R.id.xedtcarpetarea)).getText().toString(),"CarpetArea"));
					}
					if(carpetTab!=null&&carpetTab.size()>0){
						displayOfflineSaveDataDialog(carpetTab);
					}

					AppCommonDialog.showSimpleDialog(Carpetdetails.this,
							getResources().getString(R.string.app_name),
							getResources().getString(R.string.check_network),
							getResources().getString(R.string.ok), "OK");

				}
				dialog.dismiss();


			}

		});

		dialog.show();

	}

	protected void displayOfflineSaveDataDialog(final List<GetCarpetAreaResponse> carpetTab) {
		final Dialog dialog = new Dialog(Carpetdetails.this, R.style.DialogSlideAnim);
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
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Gson gson = new Gson();
				boolean bupdate = false;
				String strRequest = "";

				CarpetResponse data=new CarpetResponse();
				data.setRemarks("yes");
				data.setStatus("sucessfully");

				data.setValuesCarpetArea(carpetTab);
				strRequest = gson.toJson(data);
				if(!baddNewcarpetArea&&roomCalDetailID!=null){
					bupdate= SecondScreenActivity.mypcplData.updateCarpetdata(AppSetting.getRequestId(), roomCalDetailID, strRequest);
				}else{
					if(roomCalDetailID==null){
						int  randomid=genNumber();
						roomCalDetailID=String.valueOf(randomid);
					}
					SecondScreenActivity.mypcplData.insertCarpetdata(strRequest, AppSetting.getRequestId(),roomCalDetailID);
				}
				if(bupdate){
					Toast.makeText(Carpetdetails.this,
							"sucessfully store data  ",
							Toast.LENGTH_SHORT).show();

				}
				updateandDisplayofflineData(AppSetting.getRequestId());
				dialog.dismiss();
			}

		});
		dialog.show();


	}

	public int genNumber() {
		Random r = new Random( System.currentTimeMillis() );
		return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
	}

	public void displaydataOffOn(CarpetResponse serverResponseRequest) {
		if (serverResponseRequest != null&& serverResponseRequest.getValuesCarpetArea() != null) {
			/*if(carpetAreaData!=null&&carpetAreaData.size()>0){
				carpetAreaData.clear();
			}*/
			for (GetCarpetAreaResponse requestdata : serverResponseRequest
					.getValuesCarpetArea()) {
				if(AppSetting.getRequestId().equals(requestdata.getRequestId())){
					carpetAreaData.add(requestdata);
				}
			}


			if(carpetAreaData!=null  && carpetAreaData.size()>0){
				((ListView)findViewById(R.id.xlstCarpetArea)).setAdapter(new CarpetAreaAdapter(Carpetdetails.this,carpetAreaData));
			}

			((TextView)findViewById(R.id.xtxttotalamount)).setText(getTotalArea(carpetAreaData));



		}

	}

	public String getTotalArea(List<GetCarpetAreaResponse> carpetAreaData2) {
		String totalamount = null;
		float totalarea=0;
		for(GetCarpetAreaResponse data:carpetAreaData2){
			totalarea=Float.valueOf(totalarea)+Float.valueOf(data.getArea());

		}
		totalamount=String.valueOf(totalarea);
		return totalamount;
	}

	private boolean validateForm(Dialog dialog) {
		boolean valid = true;
		/* if (strLendth.length() <= 0) {
			 AppCommonDialog.showSimpleDialog(Carpetdetails.this, getResources()
						.getString(R.string.app_name), "Please Enter Length",
						getResources().getString(R.string.ok), "OK");
			valid = false;
		}
		 
		 else if (strWidth.length() <= 0) {
			 AppCommonDialog.showSimpleDialog(Carpetdetails.this, getResources()
						.getString(R.string.app_name), "Please Enter Width",
						getResources().getString(R.string.ok), "OK");
			valid = false;
		}*/
		strTotalArea =((EditText)dialog.findViewById(R.id.xedtTotalarea)).getText().toString();
		if ( strTotalArea.length() <= 0) {
			AppCommonDialog.showSimpleDialog(Carpetdetails.this, getResources()
							.getString(R.string.app_name), "Please Enter Total Area",
					getResources().getString(R.string.ok), "OK");
			valid = false;
		}

		return valid;
	}

	// Requestdetail Task
	public class SetRoomDetailAsyncTask extends AsyncTask<String, Void, String> {
		String strCarpetArea;
		//String strLendth; 
		//String strWidth;
		//String strTotalArea;

		public SetRoomDetailAsyncTask(Carpetdetails carpetdetails,
									  String strCarpetArea, String strLendth, String strWidth, String strTotalArea) {
			this.strCarpetArea=strCarpetArea;
			// strLendth=strLendth;
			// this.strWidth=strWidth;
			// this.strTotalArea=strTotalArea;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				loadingDialog = new ProgressDialog(Carpetdetails.this);
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

			try{
				nameValuePairs = new ArrayList<NameValuePair>(1);
			}catch(Exception e){

			}
			nameValuePairs=saveSetdataoffline(nameValuePairs,strCarpetArea);









			//Log.d("request", +);
			return GetWebServiceData.getServerResponse(url
					+ "/SetRoomArea", nameValuePairs, data);

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
			if (result != null ) {
				Log.i("Response", result);
				try {
					JSONObject serverResponsere = new JSONObject(result);

					CommonResponse serverResponseRequest = new Gson()
							.fromJson(result, CommonResponse.class);

					String loginResult = serverResponsere.getString("status");
					if (loginResult.equals("OK")) {
						Log.d("reuest detail", "get data");
						Toast.makeText(getApplicationContext(), "sucessfully "+loginResult, Toast.LENGTH_SHORT).show();
						/*if (serverResponseRequest != null&& serverResponseRequest.getStatus()!=null) {
							AppCommonDialog.showSimpleDialog(getActivity(),
									getResources().getString(R.string.app_name),"Data Update Sucessfully",
									getResources().getString(R.string.ok), "OK");
						}*/
						//if(baddNewcarpetArea){
						if (GetWebServiceData.isNetworkAvailable(Carpetdetails.this)) {
							new CarpetAsyncTask().execute();
						} else {
							AppCommonDialog.showSimpleDialog(Carpetdetails.this, getResources()
											.getString(R.string.app_name),
									getResources().getString(R.string.check_network),
									getResources().getString(R.string.ok), "OK");
						}
						//}

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
			List<NameValuePair> nameValuePairs, String strCarpetArea) {
		if(baddNewcarpetArea){

			nameValuePairs.add(new BasicNameValuePair("RequestId",AppSetting.getRequestId()));
		}else{
			nameValuePairs.add(new BasicNameValuePair("RequestId",AppSetting.getRequestId()));
		}

		nameValuePairs.add(new BasicNameValuePair("Names",strCarpetArea));
		nameValuePairs.add(new BasicNameValuePair("Lengths",strLendth));
		nameValuePairs.add(new BasicNameValuePair("Breadths",strWidth));
		nameValuePairs.add(new BasicNameValuePair("Area",strTotalArea));
		nameValuePairs.add(new BasicNameValuePair("AreaType","CarpetArea"));
		nameValuePairs.add(new BasicNameValuePair("RoomCalDetailID",roomCalDetailID));
		return nameValuePairs;
	}
}