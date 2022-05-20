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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techinvest.pcpl.adapter.CarpetAreaAdapter;
import com.techinvest.pcpl.adapter.FlooerbeddetailsAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.CarpetResponse;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.GetCarpetAreaResponse;
import com.techinvest.pcpl.model.Offlinedatamodel;
import com.techinvest.pcpl.model.RoomdetailResponse;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;

public class Flowerbeddetails extends Activity implements OnClickListener {
	
	List<GetCarpetAreaResponse>floorAreaData;
	Activity activity;
	private ProgressDialog loadingDialog;
	String roomCalDetailID="";
	private boolean baddNewFlowerbed;
	String strLendth; 
	String strWidth;
	String strTotalArea;
	String url = AppSetting.getapiURL();
	 float totalArea;
	 List<Offlinedatamodel> offlineData;
	  List<SetOfflinedata> flowerbedDataList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_floordetail);
		
		/*Bundle data=getIntent().getExtras();
		if(data!=null){
			floorAreaData=(List<GetCarpetAreaResponse>) data.getSerializable("CARPET_FLOOER");
		}*/
		((TextView)findViewById(R.id.xtxtheaderTittle)).setText(getResources().getString(R.string.title_activity_flowerbeddetails));
        ((ImageButton)findViewById(R.id.backId)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        initview();
        offlineData=new ArrayList<Offlinedatamodel>();
        flowerbedDataList = new ArrayList<SetOfflinedata>();
		if(offlineData!=null){
			offlineData=SecondScreenActivity.mypcplData.getFlowerbedDetail(AppSetting.getRequestId());
			}
        
        if (GetWebServiceData.isNetworkAvailable(Flowerbeddetails.this)) {

			new CarpetAsyncTask().execute();

		} else {
			 floorAreaData = new ArrayList<GetCarpetAreaResponse>();
			 if(offlineData.size()>0){
				
				    for(Offlinedatamodel datalocal:offlineData){
					  if(AppSetting.getRequestId()!=null&& datalocal.getRequestid().equals(AppSetting.getRequestId())){
						  floorAreaData = new ArrayList<GetCarpetAreaResponse>();
						  CarpetResponse serverResponseRequest = new Gson()
							.fromJson(datalocal.getResponsejson(), CarpetResponse.class);
						    displaydataOffOn(serverResponseRequest);
					  }
				    }
			   }
			 updateandDisplayofflineData(AppSetting.getRequestId());
			

			AppCommonDialog.showSimpleDialog(Flowerbeddetails.this, getResources()
					.getString(R.string.app_name),
					getResources().getString(R.string.check_network),
					getResources().getString(R.string.ok), "OK");
		}

        
        
       /* if(floorAreaData!=null  && floorAreaData.size()>0){
			((ListView)findViewById(R.id.xlstFlowerbeds)).setAdapter(new FlooerbeddetailsAdapter(Flowerbeddetails.this,floorAreaData));
		}*/
        
        ((ListView)findViewById(R.id.xlstFlowerbeds)).setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				baddNewFlowerbed=false;
				roomCalDetailID=floorAreaData.get(position).getRoomDetailsIDs();
				if(roomCalDetailID==null){
					roomCalDetailID=String.valueOf(genNumber());
				}
				displayCustomerDetailDialog(position);
				
			}
		});
	}
	
	private void updateandDisplayofflineData(String RequestID) {
		 flowerbedDataList=SecondScreenActivity.mypcplData.getFlowerbedAllvalues(AppSetting.getRequestId());
		//flower data get
		 if(flowerbedDataList!=null&&flowerbedDataList.size()>0){
			 if(floorAreaData!=null&& floorAreaData.size()>0)
				 floorAreaData.clear();
			 for(SetOfflinedata dataOffline:flowerbedDataList){
				 if(dataOffline.getSaveuserdata()!=null&&dataOffline.getSaveuserdata().length()>0){
					 CarpetResponse serverResponseRequest = new Gson()
						.fromJson(dataOffline.getSaveuserdata(), CarpetResponse.class);
					
				   
					for (GetCarpetAreaResponse requestdata : serverResponseRequest
			            .getValuesCarpetArea()) {
						floorAreaData.add(requestdata);
					}
				 }
				
				
			 }	 
			 if(floorAreaData!=null&&floorAreaData.size()>0){
				 List<GetCarpetAreaResponse>localdisplayData=new ArrayList<GetCarpetAreaResponse>();
				 for(GetCarpetAreaResponse datalocal:floorAreaData){
					  if(AppSetting.getRequestId()!=null&& datalocal.getRequestId().equals(AppSetting.getRequestId())){
						  localdisplayData.add(datalocal);
					  }
				 }
				 if(localdisplayData!=null&&localdisplayData.size()>0){
				 ((ListView)findViewById(R.id.xlstFlowerbeds)).setAdapter(new FlooerbeddetailsAdapter(Flowerbeddetails.this,localdisplayData));
				    ((TextView)findViewById(R.id.xtxttotalamount)).setText(getTotalArea(localdisplayData));
				 }
				 }
		 } 
		
	}

	private void initview() {
		findViewById(R.id.xbtnSave).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xbtnSave:
			baddNewFlowerbed=true;
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
					loadingDialog = new ProgressDialog(Flowerbeddetails.this);
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
						+ "/GetFBDetails", nameValuePairs, data);

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
					
					floorAreaData = new ArrayList<GetCarpetAreaResponse>();
					
					try {
						JSONObject serverResponsere = new JSONObject(result);
						Log.d("addResActivity", "Save Review Successful");
						CarpetResponse serverResponseRequest = new Gson()
								.fromJson(result, CarpetResponse.class);

						String loginResult = serverResponsere.getString("status");
						if (loginResult.equals("OK")) {
							Log.d("reuest detail", "get data");
							
							if (result!=null&& AppSetting.getRequestId()!=null) {
								// SecondScreenActivity.mypcplData.insertFlowerbeddata(result, AppSetting.getRequestId(),roomCalDetailID);
								
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
	
	
	protected void displayCustomerDetailDialog(final int position) {
		
		final Dialog dialog = new Dialog(Flowerbeddetails.this);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.flowerbed_dialogbox);
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
					   totalArea= Float.valueOf(strLendth)*Float.valueOf(strWidth);
						 strTotalArea=String.valueOf(totalArea);
						 ((EditText)dialog.findViewById(R.id.xedtTotalarea)).setText(strTotalArea);
					 }else{
						 strTotalArea =((EditText)dialog.findViewById(R.id.xedtTotalarea)).getText().toString();
					 }
			   }
			  });
		if(floorAreaData!=null  && floorAreaData.size()>0){
			if(!baddNewFlowerbed){
				 List<GetCarpetAreaResponse>localdisplayData=new ArrayList<GetCarpetAreaResponse>();
				 for(GetCarpetAreaResponse datalocal:floorAreaData){
					  if(AppSetting.getRequestId()!=null&& datalocal.getRequestId().equals(AppSetting.getRequestId())){
						  localdisplayData.add(datalocal);
					  }
				 }
				 if(localdisplayData!=null&&localdisplayData.size()>0){
		((EditText)dialog.findViewById(R.id.xedtcarpetarea)).setText(localdisplayData.get(position).getNames());
		((EditText)dialog.findViewById(R.id.xedtlength)).setText(localdisplayData.get(position).getLengths());
		((EditText)dialog.findViewById(R.id.xedtwidth)).setText(localdisplayData.get(position).getBreadths());
		((EditText)dialog.findViewById(R.id.xedtTotalarea)).setText(localdisplayData.get(position).getArea());
		    roomCalDetailID=localdisplayData.get(position).getRoomDetailsIDs();
				 }
			}
		}
		
		strLendth=((EditText)dialog.findViewById(R.id.xedtlength)).getText().toString();
		strWidth=((EditText)dialog.findViewById(R.id.xedtwidth)).getText().toString();
		strTotalArea =((EditText)dialog.findViewById(R.id.xedtTotalarea)).getText().toString();
		 // roomCalDetailID=floorAreaData.get(position).getRoomDetailsIDs();
		 //strTotalArea=String.valueOf(totalArea);
		
		
		 if(strLendth!=null&&strLendth.length()>0 &&strWidth!=null&&strWidth.length()>0){
			 totalArea= Float.valueOf(strLendth)*Float.valueOf(strWidth);
			 strTotalArea=String.valueOf(totalArea);
			 ((EditText)dialog.findViewById(R.id.xedtTotalarea)).setText(strTotalArea);
		 }else{
			 strTotalArea =((EditText)dialog.findViewById(R.id.xedtTotalarea)).getText().toString();
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
				
				if (GetWebServiceData.isNetworkAvailable(Flowerbeddetails.this)) {
					if(validateForm(dialog)){
					new SetRoomDetailAsyncTask(Flowerbeddetails.this,((EditText)dialog.findViewById(R.id.xedtcarpetarea)).getText().toString(),
							strLendth,strWidth,strTotalArea).execute();
					}
				} else {
					
					
					List<GetCarpetAreaResponse> fbTab = null;
					
					fbTab=new ArrayList<GetCarpetAreaResponse>();
					if(!baddNewFlowerbed&&floorAreaData!=null&&floorAreaData.size()>0)
						  roomCalDetailID=floorAreaData.get(position).getRoomDetailsIDs();
					else{
						roomCalDetailID=String.valueOf(genNumber());
					}
					
					if(validateForm(dialog)){
					
					
						fbTab.add(new GetCarpetAreaResponse(AppSetting.getRequestId(),roomCalDetailID, strLendth, strWidth, strTotalArea, ((EditText)dialog.findViewById(R.id.xedtcarpetarea)).getText().toString(),"CarpetArea"));
					}
					if(fbTab!=null&&fbTab.size()>0){
						
					        displayOfflineSaveDataDialog(fbTab);
				     }
					
					AppCommonDialog.showSimpleDialog(Flowerbeddetails.this,
							getResources().getString(R.string.app_name),
							getResources().getString(R.string.check_network),
							getResources().getString(R.string.ok), "OK");
					
				}
				dialog.dismiss();
				
			
			}

		});

		dialog.show();

	}
	
	protected void displayOfflineSaveDataDialog(
			final List<GetCarpetAreaResponse> fbTab) {

		final Dialog dialog = new Dialog(Flowerbeddetails.this, R.style.DialogSlideAnim);
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
				String strRequest = "";
				boolean bupdate = false;
				CarpetResponse data=new CarpetResponse();
				  data.setRemarks("yes");
				  data.setStatus("sucessfully");
				  data.setValuesCarpetArea(fbTab);
				  strRequest = gson.toJson(data);
				  
					if(roomCalDetailID!=null&&!baddNewFlowerbed){
						bupdate= SecondScreenActivity.mypcplData.updateFlowerbeddata(AppSetting.getRequestId(), roomCalDetailID, strRequest);
							}else{
								if(roomCalDetailID==null){
									roomCalDetailID=String.valueOf(genNumber());
								}
						   SecondScreenActivity.mypcplData.insertFlowerbeddata(strRequest, AppSetting.getRequestId(),roomCalDetailID);
						   }

				
			
			if(bupdate){
				Toast.makeText(Flowerbeddetails.this,
						"sucessfully store data  ",
						Toast.LENGTH_SHORT).show();
				
			}
			updateandDisplayofflineData(AppSetting.getRequestId());
				dialog.dismiss();
			}

		});
		dialog.show();
		
	}

	public void displaydataOffOn(CarpetResponse serverResponseRequest) {
		if (serverResponseRequest != null&& serverResponseRequest.getValuesCarpetArea() != null) {
			if(floorAreaData!=null&&floorAreaData.size()>0){
				floorAreaData.clear();
			}

			for (GetCarpetAreaResponse requestdata : serverResponseRequest
					.getValuesCarpetArea()) {
				if(AppSetting.getRequestId().equals(requestdata.getRequestId())){
				   floorAreaData.add(requestdata);
				}
			}

			
			 if(floorAreaData!=null  && floorAreaData.size()>0){
					((ListView)findViewById(R.id.xlstFlowerbeds)).setAdapter(new FlooerbeddetailsAdapter(Flowerbeddetails.this,floorAreaData));
				}
			
			 ((TextView)findViewById(R.id.xtxttotalamount)).setText(getTotalArea(floorAreaData));

			

		}
		
	}

	public CharSequence getTotalArea(List<GetCarpetAreaResponse> floorAreaData2) {
		  String totalamount = null;
	      float totalarea=0;
	        for(GetCarpetAreaResponse data:floorAreaData2){
	        	totalarea=Float.valueOf(totalarea)+Float.valueOf(data.getArea());
	        	
	        }
	        totalamount=String.valueOf(totalarea);
			return totalamount;
	}

	protected boolean validateForm(Dialog dialog) {
		boolean valid = true;
		 strTotalArea =((EditText)dialog.findViewById(R.id.xedtTotalarea)).getText().toString();
		 if (strTotalArea.length() <= 0) {
			 AppCommonDialog.showSimpleDialog(Flowerbeddetails.this, getResources()
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
	
		public SetRoomDetailAsyncTask(Flowerbeddetails carpetdetails,
				String strCarpetArea, String strLendth, String strWidth, String strTotalArea)
		{
			  this.strCarpetArea=strCarpetArea;
		      //this.strLendth=strLendth;
		     // this.strWidth=strWidth;
		     // this.strTotalArea=strTotalArea;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				loadingDialog = new ProgressDialog(Flowerbeddetails.this);
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
				nameValuePairs = new ArrayList<NameValuePair>(22);
			}catch(Exception e){
				
			}
		
				if(baddNewFlowerbed){
					nameValuePairs.add(new BasicNameValuePair("RequestId",AppSetting.getRequestId()));	
				}else{
					nameValuePairs.add(new BasicNameValuePair("RequestId",AppSetting.getRequestId()));	
				}
			    nameValuePairs.add(new BasicNameValuePair("Names",strCarpetArea));
			    nameValuePairs.add(new BasicNameValuePair("Lengths",strLendth));
			    nameValuePairs.add(new BasicNameValuePair("Breadths",strWidth));
			    nameValuePairs.add(new BasicNameValuePair("Area",strTotalArea));
			    nameValuePairs.add(new BasicNameValuePair("AreaType","FlowerBed"));
			    nameValuePairs.add(new BasicNameValuePair("RoomCalDetailID",roomCalDetailID));
			
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
						
						//if(baddNewFlowerbed){
							  if (GetWebServiceData.isNetworkAvailable(Flowerbeddetails.this)) {

									new CarpetAsyncTask().execute();

								} else {

									AppCommonDialog.showSimpleDialog(Flowerbeddetails.this, getResources()
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
	
	public int genNumber() {
	    Random r = new Random( System.currentTimeMillis() );
	    return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
	}

}
