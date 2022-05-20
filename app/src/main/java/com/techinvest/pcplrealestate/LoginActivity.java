package com.techinvest.pcplrealestate;

import static com.techinvest.pcplrealestate.PcplApplication.TAG;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
//import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techinvest.pcpl.adapter.ImageDetailAdapter;
import com.techinvest.pcpl.adapter.UnAssignedReqListViewAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppDetails;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.DisplayImagesResponse;
import com.techinvest.pcpl.model.ImageDetailResponse;
import com.techinvest.pcpl.model.Jj;
import com.techinvest.pcpl.model.Location1;
import com.techinvest.pcpl.model.LocationDataResponse;
import com.techinvest.pcpl.model.LoginResponse;
import com.techinvest.pcpl.model.UnsignedReq;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;

public class LoginActivity extends Activity implements AdapterView.OnItemSelectedListener {
	//Toolbar toolbar;
	Button Signin;
	EditText uname, password;
	 RadioGroup radioCity;
	 RadioButton Rd_punClick;
	 RadioButton RD_nashikClick;
	String City;
	private String emailText;
	private String passwordText;
	public ProgressDialog progressDialog;
	private String url;
	public String userId;
	private String email;
	public Spinner spinner;
	List<Location1> locationDataList=new ArrayList<Location1>();

	AppSetting apset = new AppSetting();
	public static String SERVER_URL = "";


	public void onRdPune(View view) {
	}

	public void onRdMumbai(View view) {
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String text = parent.getItemAtPosition(position).toString();
		if (text.equalsIgnoreCase("Select Location")){

		}
		else{
			Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	// /
	private class EmployeeLoginBackgroundTaskMumbai extends AsyncTask<String, Void, String>
	{
		@Override

		protected void onPreExecute() {
			progressDialog = AppCommonDialog.createProgressDialog(LoginActivity.this);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated meth
			//  od stub
			String data = "";

			List<NameValuePair> nameValuePairs = null;
			nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("name",
					emailText));
			nameValuePairs.add(new BasicNameValuePair("pass",
					passwordText));

			return GetWebServiceData.getServerResponse( SERVER_URL + "/GetLoginData", nameValuePairs, data);
//}
		}

		@Override
		protected void onPostExecute(String result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {

				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (result != null && !result.equals(""))
			{
				Log.i("Request", result);
				try {
					JSONObject serverResponsere = new JSONObject(result);
					Log.d("addResActivity", "Save Review Successful");
					LoginResponse serverResponseRequest = new Gson()
							.fromJson(result, LoginResponse.class);

					String loginResult = serverResponsere.getString("status");
					if (loginResult.equals("OK"))
					{
						Log.d("request detail", "get data");

						if (serverResponseRequest != null && serverResponseRequest.getUSERID()!=null) {
							AppSetting.setUserId(serverResponseRequest.getUSERID());
							AppSetting.setRequestId(serverResponseRequest.getUSERID());
							AppSetting.setUserLoginEmail(serverResponseRequest.getName());
							AppSetting.setapiURL(SERVER_URL);
							AppSetting.setRemeberMe(false);
							if (((CheckBox) findViewById(R.id.checkRemeberMe)).isChecked()) {
								AppSetting.setRemeberMe(true);
							}
							Intent intent = new Intent(LoginActivity.this,SecondScreenActivity.class);
							startActivity(intent);
						}

					}
					else
					{

						if (serverResponseRequest != null && serverResponseRequest.getRemarks()!=null) {

							/*Toast.makeText(getApplicationContext(),
									"Serv : "+serverResponseRequest.getRemarks(),
									Toast.LENGTH_SHORT).show();*/
							if(!isFinishing())
							AppCommonDialog.showSimpleDialog(LoginActivity.this,
									getResources().getString(R.string.app_name),
									serverResponseRequest.getRemarks(),
									getResources().getString(R.string.ok), "OK");

								EmployeeLoginBackgroundTaskMumbai tmp = EmployeeLoginBackgroundTaskMumbai.this;
								tmp.cancel(true);

						}else{
							Toast.makeText(getApplicationContext(),
									"some thing went wrong",
									Toast.LENGTH_SHORT).show();
							//loginMethod("http://120.63.240.30:8085/PCPLWebServices/Service1.asmx");//Origin Url
						}


					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("LoginActivity", "JSON Result parse error");
					e.printStackTrace();
				}

			}
		}
	}

	private class EmployeeLoginBackgroundTaskPune extends AsyncTask<String, Void, String>
    {
		@Override
		protected void onPreExecute() {
			progressDialog = AppCommonDialog.createProgressDialog(LoginActivity.this);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String data = "";

			List<NameValuePair> nameValuePairs = null;
			nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("name",
					emailText));
			nameValuePairs.add(new BasicNameValuePair("pass",
					passwordText));


			//return GetWebServiceData.getServerResponse(AppConstants.SERVER_URLPUN
			//		+ "/GetLoginData", nameValuePairs, data);

//}
			//		else if(City == "Mumbai") {
			return GetWebServiceData.getServerResponse(SERVER_URL
					+ "/GetLoginData", nameValuePairs, data);
//}
		}


		//@Override
		/*protected  String doInBackground1(String... params) {
			String data = "";
			List<NameValuePair> nameValuePairs = null;
			nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("name",
					emailText));
			nameValuePairs.add(new BasicNameValuePair("pass",
					passwordText));

	//Log.d("LoginActivity", "Attempting Signup");

	return GetWebServiceData.getServerResponse(AppConstants.SERVER_URLPUN
			+ "/GetLoginData", nameValuePairs, data);




		}*/

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {

				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (result != null && !result.equals(""))
			{
				Log.i("Request", result);
				try
				{
					JSONObject serverResponsere = new JSONObject(result);
					Log.d("addResActivity", "Save Review Successful");
					LoginResponse serverResponseRequest = new Gson().fromJson(result, LoginResponse.class);
					String loginResult = serverResponsere.getString("status");
					if (loginResult.equals("OK"))
					{
						Log.d("request detail", "get data");

						if (serverResponseRequest != null&& serverResponseRequest.getUSERID()!=null) {
							AppSetting.setUserId(serverResponseRequest.getUSERID());
							AppSetting.setRequestId(serverResponseRequest.getUSERID());
							AppSetting.setUserLoginEmail(serverResponseRequest.getName());
							AppSetting.setapiURL(SERVER_URL);
							AppSetting.setRemeberMe(false);
							if (((CheckBox) findViewById(R.id.checkRemeberMe)).isChecked()) {
								AppSetting.setRemeberMe(true);
							}
							Intent intent = new Intent(LoginActivity.this,SecondScreenActivity.class);
							startActivity(intent);
						}

					}
					else
					{
						if (serverResponseRequest != null && serverResponseRequest.getRemarks()!=null) {

							/*Toast.makeText(getApplicationContext(),
									serverResponseRequest.getRemarks(),
									Toast.LENGTH_SHORT).show();*/
							AppCommonDialog.showSimpleDialog(LoginActivity.this,
									getResources().getString(R.string.app_name),
									serverResponseRequest.getRemarks(),
									getResources().getString(R.string.ok), "OK");

							EmployeeLoginBackgroundTaskPune tmp = EmployeeLoginBackgroundTaskPune.this;
							tmp.cancel(true);

						}else{
							Toast.makeText(getApplicationContext(),
									"Something went wrong",
									Toast.LENGTH_SHORT).show();
						}

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("LoginActivity", "JSON Result parse error");
					e.printStackTrace();
				}

			}
		}
	}

	public class GetLocationData extends AsyncTask<String, Void, String> implements AdapterView.OnItemSelectedListener {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				progressDialog = AppCommonDialog.createProgressDialog(LoginActivity.this);
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
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("",""));
			return GetWebServiceData.getServerResponse(url	+ "/GetLocationData", nameValuePairs, data);

		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			try {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (result != null && !result.equals(""))
			{
				try
				{
					JSONObject serverResponsere = new JSONObject(result);
					String loginResult = serverResponsere.getString("status");

					if (loginResult.equalsIgnoreCase("OK")){
						locationDataList = new Gson().fromJson(result, new TypeToken<List<Location1>>(){}.getType());
						String[] locationList = new String[locationDataList.size()];
						locationList[0] = "Select Location";
						for(int i=0;i<locationDataList.size();i++)
						{
							locationList[i+1] = (locationDataList.get(i).getName());
						}
						spinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, locationList));
					}
					else{
						Toast.makeText(getApplicationContext(), "Location Not Found", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("LoginActivity", "JSON Result parse error");
					e.printStackTrace();
				}
				spinner.setOnItemSelectedListener(this);
			}
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			if (parent.getItemAtPosition(position).equals("Select Location")) {

			} else {
				String result = parent.getItemAtPosition(position).toString();
				if (result.equalsIgnoreCase("MUMBAI")){
					AppSetting.setLocationId(locationDataList.get(position).getLocationId());
					//"C290B0BD-49B4-465E-A973-2BA59FB9EA85"
					AppSetting.setLocationName("MUMBAI");
				}
				else if (result.equalsIgnoreCase("PUNE")){
					AppSetting.setLocationId(locationDataList.get(position).getLocationId());
					//"145D8B5E-E58A-418E-B3EB-3594AC0EB506"
					AppSetting.setLocationName("PUNE");
				}
				else{
					AppSetting.setLocationId(locationDataList.get(position).getLocationId());
					//"DA600BA0-C9A8-462F-B550-7488761E64FC"
					AppSetting.setLocationName("Nashik");
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}





	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		prepareActivity();
	//	toolbar = (Toolbar) findViewById(R.id.tool_bar);
	//	setSupportActionBar(toolbar);
		//setSu(toolbar);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		spinner = findViewById(R.id.spinner);
		uname = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
//        Rd_punClick = (RadioButton) findViewById(R.id.radioButton);
//        RD_nashikClick = (RadioButton) findViewById(R.id.radioButton3);
		Signin = (Button) findViewById(R.id.button1);
		//new GetLocationData().execute();
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.locationValues, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		Signin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(isConnectedToServer("http://13.235.139.58:8080/Service1.asmx",3000)){
					SERVER_URL = "http://13.235.139.58:8080/Service1.asmx";
					Toast.makeText(LoginActivity.this, "Server connected", Toast.LENGTH_SHORT).show();
					loginMethod(SERVER_URL);
				}else if(isConnectedToServer("http://120.63.240.30:8085/PCPLWebServices/Service1.asmx",115)){
					SERVER_URL = "http://120.63.240.30:8085/PCPLWebServices/Service1.asmx";
					loginMethod(SERVER_URL);
				}else {
					Toast.makeText(LoginActivity.this,"Server Not Responding",Toast.LENGTH_LONG).show();
				}

				//SERVER_URL = "http://206.183.111.109/PCPLWebServices/Service1.asmx";
				/*if(isConnectedToServer("http://206.183.111.109",8080)){
					SERVER_URL = "http://206.183.111.109/PCPLWebServices/Service1.asmx";
					loginMethod(SERVER_URL);
				}else {
					Toast.makeText(LoginActivity.this,"Server Not Responding",Toast.LENGTH_LONG).show();
				}*/
				}

		});

		((CheckBox) findViewById(R.id.checkRemeberMe)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) findViewById(R.id.checkRemeberMe)).isChecked()) {
                    ((CheckBox) findViewById(R.id.checkRemeberMe)).setSelected(true);
                }
			}
		});
		getPermissions();

	}
	public void getPermissions()
	{
		if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			{
				requestPermissions(new String[]{
						android.Manifest.permission.ACCESS_COARSE_LOCATION,
						android.Manifest.permission.ACCESS_FINE_LOCATION,
						android.Manifest.permission.READ_EXTERNAL_STORAGE,
						android.Manifest.permission.CAMERA,
						android.Manifest.permission.CALL_PHONE} ,1);
			}
			return;
		}
	}


	private void loginMethod(String url)
	{
		emailText = uname.getText().toString();
		passwordText = password.getText().toString().trim();
		System.out.println("checking username="+emailText);
		try {
			if (validateForm())
			{
				 if (GetWebServiceData.isNetworkAvailable(LoginActivity.this))
				 {
					 if(AppSetting.getLocationName().equalsIgnoreCase("PUNE"))
					 {
						 //set Pune Api
						 SERVER_URL = "http://13.235.139.58:8080/Service1.asmx";
//"http://111.125.232.137:8085/PCPLWebServices/Service1.asmx";

						 AppSetting.setapiURL(SERVER_URL);
                                    //"http://206.183.111.109/PCPLWebServices/Service1.asmx";

						 //obj.setServerURL("http://206.183.111.109/PCPLWebServices/Service1.asmx");
						 //new EmployeeLoginBackgroundTaskPune().execute();
						 new EmployeeLoginBackgroundTaskPune().execute();
					 }
					 else if (AppSetting.getLocationName().equalsIgnoreCase("Nashik")){
					 	//set Nashik Api
						 SERVER_URL = "http://13.235.139.58:8080/Service1.asmx";
//"http://111.125.232.137:8085/PCPLWebServices/Service1.asmx";


						 SERVER_URL = url;
						 AppSetting.setapiURL(SERVER_URL);
						 new EmployeeLoginBackgroundTaskMumbai().execute();
					 }
					 else {
						 //set Mumbai Api
						 //obj.setServerURL("http://120.63.240.30:8085/PCPLWebServices/Service1.asmx");

						 // SERVER_URL = "http://120.63.240.30:8085/PCPLWebServices/Service1.asmx";//Origin
						 SERVER_URL = "http://13.235.139.58:8080/Service1.asmx";
//"http://111.125.232.137:8085/PCPLWebServices/Service1.asmx";

						 SERVER_URL = url;
						 AppSetting.setapiURL(SERVER_URL);
						 new EmployeeLoginBackgroundTaskMumbai().execute();
					 }
					 //SERVER_URL= ((PcplApplication) this.getApplication()).getServerURL();
					 //SERVER_URL=obj.getServerURL();

					} else {
						 AppCommonDialog.showSimpleDialog(LoginActivity.this, getResources()
									.getString(R.string.app_name), getResources()
									.getString(R.string.check_network), getResources().getString(R.string.ok), "OK");
					}


			} /*else if ((emailText.length()>0)) {
				Toast.makeText(getApplicationContext(), "password field empty",
						Toast.LENGTH_SHORT).show();
			} else if ((passwordText.length()>0)) {
				Toast.makeText(getApplicationContext(), "Username field empty",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Username and Password field are empty",
						Toast.LENGTH_SHORT).show();
			}
			url = "http://206.183.111.109/TechvaluationWebservice/TechV.asmx/GetLoginData?name="+ URLEncoder.encode(emailText, "UTF-8")+ "&pass="+ URLEncoder.encode(passwordText, "UTF-8");*/

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

 	}

	
	private boolean validateForm() {
		boolean valid = true;
		
		 if (emailText.length() <= 0) {
			 AppCommonDialog.showSimpleDialog(LoginActivity.this, getResources()
						.getString(R.string.app_name), "Please Enter User Name",
						getResources().getString(R.string.ok), "OK");
			valid = false;
		}
		 
		 if (passwordText.length() <= 0) {
			 AppCommonDialog.showSimpleDialog(LoginActivity.this, getResources()
						.getString(R.string.app_name), "Please Enter Password",
						getResources().getString(R.string.ok), "OK");
			valid = false;
		}
		
		
		return valid;
	}

	
	private void prepareActivity() {
		AppDetails.setContext(this);
		AppDetails.setActivity(this);

	}

	public boolean isConnectedToServer(String url, int timeout) {
		try {
			URL myUrl = new URL(url);
			URLConnection connection = myUrl.openConnection();
			connection.setConnectTimeout(timeout);
			connection.connect();
			return true;
		} catch (Exception e) {
			// Handle your exceptions
			return false;
		}
		/*try {
			SocketAddress sockaddr = new InetSocketAddress(ip, port);
			// Create an unbound socket
			Socket sock = new Socket();
			// This method will block no more than timeoutMs.
			// If the timeout occurs, SocketTimeoutException is thrown.
			int timeoutMs = 2000;   // 2 seconds
			sock.connect(sockaddr, timeoutMs);
			return true;
		} catch(IOException e) {
			// Handle exception
			return false;
		}*/
	}
}
