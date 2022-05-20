package com.techinvest.pcplrealestate;

import static com.techinvest.pcpl.commonutil.AppDetails.getActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
//import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppDetails;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.commonutil.GPSTracker;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcplrealestate.Layoutdetails.UploadImagesAsyncTask;

@SuppressLint("NewApi")
public class MapviewActivity extends FragmentActivity
{
	public static final String APP_PATH = Environment.getExternalStorageDirectory() + File.separator + "PCPL";
	private double userlatitude, userlongitude;
	Activity activity;
	private GPSTracker gps;
	Bitmap bitmap;
	int index=0;
	RelativeLayout rellayCapture;
	private String imagePath = "";
	String encodedString;
	String url = AppSetting.getapiURL();
	private ProgressDialog loadingDialog;
	String pictureFilePath = "";
 //   static final LatLng PARIS = new LatLng(48.858093, 2.294694);
    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview_activity);

		if(AppSetting.getUserLatitude().equals(null) && AppSetting.getUserLongitude().equals(null) || AppSetting.getUserLatitude().equalsIgnoreCase("") && AppSetting.getUserLongitude().equalsIgnoreCase(""))
		{
			AppCommonDialog.showSimpleDialog(MapviewActivity.this,
					getResources().getString(R.string.app_name),
					"Please enter latitude and longitude in locationDetail Screen",
					getResources().getString(R.string.ok), "OK");

//			AlertDialog.Builder builder = new AlertDialog.Builder(MapviewActivity.this);
//			builder.setTitle(getResources().getString(R.string.app_name))
//					.setMessage("Please enter latitude and longitude in locationDetail Screen")
//					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							Intent intent = new Intent(MapviewActivity.this, Layoutdetails.class);
//							startActivity(intent);
//						}
//					})
//					.setCancelable(false);

			//Toast.makeText(MapviewActivity.this, "Please enter latitude and longitude in locationDetail Screen", Toast.LENGTH_LONG).show();

			/*  gps = new GPSTracker(this);
	            // check if GPS enabled
	            if (gps.canGetLocation()) {
	                userlatitude = gps.getLatitude();
	                userlongitude = gps.getLongitude();
	                AppSetting.setUserLatitude(String.valueOf(userlatitude));
	                AppSetting.setUserLongitude(String.valueOf(userlongitude));

	              //  currentCity = new GPSLocationReader().getAddress(userlatitude, userlongitude);
	              //  Toast.makeText(getApplicationContext(), "Your Location is - " + currentCity, Toast.LENGTH_LONG).show();
	            } else {
	                // can't get location
	                // GPS or Network is not enabled
	                CheckGPSEnable();
	            }*/

		}
		else{
			userlatitude=Double.parseDouble(AppSetting.getUserLatitude());
			userlongitude=Double.parseDouble(AppSetting.getUserLongitude());
			Log.e("123","Lat: "+userlatitude+"\nLong: "+userlongitude);

		}

			prepareActivity();

			((TextView) findViewById(R.id.xtxtheaderTittle)).setText(getResources().getString(R.string.mapview));
			((ImageButton) findViewById(R.id.backId)).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					Intent intent = new Intent(v.getContext(), Layoutdetails.class);
//					v.getContext().startActivity(intent);
					finish();
				}
			});

			initview();
			((Button) findViewById(R.id.xbtncapture)).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//index++;
						//takeScreenshot();
					captureMapScreen();

//						bitmap = takeScreenshot();
//						saveBitmap(bitmap);
//					 	Toast.makeText(getApplicationContext(), "screen shot capture successfully", Toast.LENGTH_SHORT).show();


				}


				//		}
			});


			((Button) findViewById(R.id.xbtnsubmit)).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (GetWebServiceData.isNetworkAvailable(MapviewActivity.this)) {
						if (imagePath != null) {
							new UploadImagesAsyncTask().execute();
						} else {
							Toast.makeText(getApplicationContext(), "Please capture images or select from gallery", Toast.LENGTH_LONG).show();
						}

					} else {

						AppCommonDialog.showSimpleDialog(MapviewActivity.this,
								getResources().getString(R.string.app_name),
								getResources().getString(R.string.check_network),
								getResources().getString(R.string.ok), "OK");
					}


				}
			});

	}

	public File takeScreenshot() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = new File(Environment.getExternalStorageDirectory().toString(), Environment.DIRECTORY_PICTURES);
		File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		storageDir.mkdirs(); // make sure you call mkdirs() and not mkdir()
		File image = File.createTempFile(
				imageFileName,  // prefix
				".jpg",         // suffix
				storageDir      // directory
		);

		pictureFilePath = image.getAbsolutePath();
		Log.e("our file", image.toString());
		return image;
	}

	private void openScreenshot(File imageFile) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(imageFile);
		intent.setDataAndType(uri, "image/*");
		startActivity(intent);
	}

	public void saveBitmap(Bitmap bitmap) {
		//File f = new File(android.os.Environment
		//		.getExternalStorageDirectory(), "temp.jpg");
		File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"screenshot.png");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			imagePath = file.getPath();
		} catch (FileNotFoundException e) {
			Log.e("GREC", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("GREC", e.getMessage(), e);
		}
	}

	//public final void snapshot (GoogleMap.SnapshotReadyCallback callback)

	public void captureMapScreen() {

		final SnapshotReadyCallback callback = new SnapshotReadyCallback() {

			@SuppressLint("WrongConstant")
			@Override
			public void onSnapshotReady(Bitmap snapshot) {
				try {
					//bitmap=snapshot;
					View mView = rellayCapture.getRootView();
					mView.setDrawingCacheEnabled(true);
	                   /* Bitmap backBitmap = mView.getDrawingCache();
	                    Bitmap bmOverlay = Bitmap.createBitmap(
	                            backBitmap.getWidth(), backBitmap.getHeight(),
	                            backBitmap.getConfig());
	                    Canvas canvas = new Canvas(bmOverlay);
	                    canvas.drawBitmap(backBitmap, 0, 0, null);
	                    canvas.drawBitmap(snapshot, new Matrix(), null);*/
					// Use camera
					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
					String imageFileName = "JPEG_" + timeStamp + "_";

					File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
					storageDir.mkdirs(); // make sure you call mkdirs() and not mkdir()
					File image = File.createTempFile(
							imageFileName,  // prefix
							".jpg",         // suffix
							storageDir      // directory
					);

					FileOutputStream out;
					File file = null;
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
						file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), image.getName());
					} else {
						file = new File(image.getAbsolutePath());
					}

					try {
						out = new FileOutputStream(image);
						snapshot.compress(Bitmap.CompressFormat.JPEG, 90, out);
						out.flush();
						out.close();
						imagePath = file.getPath();
					}catch (Exception e){
						e.toString();
					}

					bitmap=snapshot;
					//saveBitmap(bitmap);
					Toast.makeText(getApplicationContext(), "screen shot captured successfully", 1).show();
				} catch (Exception e) {
				    System.out.println(e.toString());
				    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		};
		((MapFragment) getFragmentManager()
				.findFragmentById(R.id.mapviewbuilding)).getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap googleMap) {
				googleMap.snapshot(callback);
			}
		});


	}




	private void CheckGPSEnable() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

		}else{
			//  DisplayGPSDisabledAlertDialog();
		}

	}

////////////////////////////////////////////////////CHANGE///////////////////////////////////////////
	private void initview()
	{

			rellayCapture = (RelativeLayout) findViewById(R.id.xrellaycapturescreen);// get ur root view id;
		((MapFragment) getFragmentManager().findFragmentById(R.id.mapviewbuilding)).getMapAsync(new OnMapReadyCallback()
		{
                @SuppressLint("MissingPermission")
				@Override
                public void onMapReady(GoogleMap googleMap)
				{
                    LatLng latLng = new LatLng(Double.parseDouble(AppSetting.getUserLatitude()), Double.parseDouble(AppSetting.getUserLongitude()));
					Log.e("123","In Mapview"+AppSetting.getUserLatitude()+" # "+AppSetting.getUserLongitude());

					googleMap.addMarker(new MarkerOptions().title("Location").position(latLng));
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.getUiSettings().setZoomGesturesEnabled(true);
                    googleMap.setTrafficEnabled(true);
					googleMap.setMyLocationEnabled(true);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

					/*LatLng sydney = new LatLng(SecondScreenActivity.current_Lattitude, SecondScreenActivity.current_Longitude);
					googleMap.setMyLocationEnabled(true);
					googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
					googleMap.addMarker(new MarkerOptions()
							.title("Location")
							.snippet("The most populous city in Australia.")
							.position(sydney));*/

                }
          });
////////////////////////////////////////////////////CHANGE///////////////////////////////////////////


		//	googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
		//		@Override
		//		public void onCameraChange(CameraPosition cameraPosition) {
//	                    Toast.makeText(AppDetails.getContext(), "hii.. get touch event", Toast.LENGTH_SHORT).show();
//	                    ((CustomScrollView) findViewById(R.id.scrollview_parent)).setScrollingEnabled(false);
		//		}
		//	});


			//  googleMap.addMarker(new MarkerOptions().position(loaction).title(currentCity);

		//	googleMap.addMarker(new MarkerOptions().position(latLng));
		//	googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		//	googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));



	}

	/* public void DisplayGPSDisabledAlertDialog() {
	        final Dialog dialog = new Dialog(MapviewActivity.this, R.style.DialogSlideAnim);
	        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        //dialog.setCancelable(false);
	        dialog.setCanceledOnTouchOutside(false);
	        dialog.setContentView(R.layout.custom_gps_dialogbox);
	        TextView textTitle = (TextView) dialog.findViewById(R.id.gps_alertdialog_title);
	        // set the custom dialog components - text, image and button
	        textTitle.setText(getResources().getString(	R.string.alaaraji));
	        TextView text = (TextView) dialog
	                .findViewById(R.id.xtxtgps_message);
	        text.setText(getResources().getString(	R.string.enable_gps));

	        Button dialogsetting = (Button) dialog
	                .findViewById(R.id.btn_setting);

	        Button dialogcancel = (Button) dialog
	                .findViewById(R.id.xbtnCancel);


	        // if button is clicked, close the custom dialog
	        dialogsetting.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                dialog.dismiss();
	                Intent callGPSSettingIntent = new Intent(
	                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                startActivity(callGPSSettingIntent);

	            }
	        });
	 }*/


	//  Task
	public class UploadImagesAsyncTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				loadingDialog = new ProgressDialog(MapviewActivity.this);
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
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			String resultdata = null;
			String data = "mumbai";
			List<NameValuePair> nameValuePairs = null;
			if (AppSetting.getRequestId() != null && imagePath != null)
			{
				BitmapFactory.Options options = null;
				options = new BitmapFactory.Options();
				options.inSampleSize = 3;
				bitmap = BitmapFactory.decodeFile(imagePath,options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// Must compress the Image to reduce image size to make upload easy
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
				byte[] byte_arr = stream.toByteArray();
				// Encode Image to String
				encodedString = Base64.encodeToString(byte_arr, 0);

				//encodedString = Base64.encodeBytes(byte_arr);
				System.out.println("Base 64 String: " + "file path" +encodedString);

				nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("myBase64String",encodedString));
				nameValuePairs.add(new BasicNameValuePair("fileName","map"));
				nameValuePairs.add(new BasicNameValuePair("requestID",AppSetting.getRequestId()));
				nameValuePairs.add(new BasicNameValuePair("filepath",imagePath));

			}

			System.out.println("Request multipart final: " + "post_data" +nameValuePairs.toString());

			return GetWebServiceData.getServerResponse(url+ "/UploadFile", nameValuePairs, data);

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

					String loginResult =serverResponsere.getString("status");
					String message =serverResponsere.getString("Remarks");
					if (loginResult.equals("OK")) {
						//Log.d("reuest detail", "get data");
						if(message!=null)
							Toast.makeText(MapviewActivity.this,"successfully "  +message,Toast.LENGTH_SHORT).show();
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

	private void prepareActivity() {
		AppDetails.setContext(this);
		AppDetails.setActivity(this);

	}

}
