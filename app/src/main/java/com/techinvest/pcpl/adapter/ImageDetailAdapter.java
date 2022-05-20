package com.techinvest.pcpl.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.techinvest.pcpl.adapter.LocationDetailAdapter.ViewHolder;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.ImageDetailResponse;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.Roomdetails.RoomdetailAsyncTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ImageDetailAdapter extends BaseAdapter {
	Activity activity;
	List<ImageDetailResponse> imageData;
	String imgid;
	String url = AppSetting.getapiURL();
	private ProgressDialog loadingDialog;

	public ImageDetailAdapter(Activity activity,List<ImageDetailResponse> imageData) {
		this.activity = activity;
		this.imageData = imageData;
		//inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.e("000","Size"+imageData.size());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.e("000","getCount "+imageData.size());
		return imageData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		//return imageData.get(position);
		Log.e("000","Position "+position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public class ViewHolder
	{
		ImageView imgbuilding;
		ImageView imgdelete;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent)
	{
		ViewHolder holder = new ViewHolder();
		View rowView ;

		/*LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(R.layout.item_imagedisplay, null);
		holder.imgbuilding = (ImageView) rowView.findViewById(R.id.ximgdisplay);
		holder.imgdelete = (ImageView) rowView.findViewById(R.id.imgdelete);
		//holder.imgdelete.setTag(imageData.get(pos).getLayOutDetails());

		Picasso.with(activity)
				.load(imageData.get(pos).getColumn1())
				.placeholder(R.drawable.plus_icon)
				.error(R.drawable.plus_icon)
				.fit()
				.centerCrop()
				.into(holder.imgbuilding);
		String strImgPath = imageData.get(pos).getColumn1();
		Log.e("123","URL : "+strImgPath+"  // "+pos);
		Log.e("123","Det : "+this.imageData.get(pos).getLayOutDetails()+"  "+pos);

		holder.imgdelete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				imgid=(String) holder.imgdelete.getTag();
				Displaydeletedialog(imgid);
			}
		});*/

		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_imagedisplay, null);

			holder.imgbuilding = (ImageView) convertView.findViewById(R.id.ximgdisplay);
			holder.imgdelete = (ImageView) convertView.findViewById(R.id.imgdelete);
			holder.imgdelete.setTag(imageData.get(pos).getLayOutDetails());
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
        
		
		 Picasso.with(activity)
				 //.load("https://th.bing.com/th/id/OIP.0mrZL_6SLB8ttKY065K1MwHaLk?pid=ImgDet&rs=1")
				 .load(imageData.get(pos).getColumn1())
				 .placeholder(R.drawable.plus_icon)
				 .error(R.drawable.plus_icon)
				 .fit()
				 .centerCrop()
				 .into(holder.imgbuilding);
		Log.e("123","URL : "+imageData.get(pos).getColumn1()+"  "+pos);
 //imageviewlocal.setTag(listMediaimages.get(i).getId());

		final ViewHolder finalHolder = holder;
		holder.imgdelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 imgid=(String) finalHolder.imgdelete.getTag();
				 Displaydeletedialog(imgid);
				
			}
		});

		//return rowView;
		return convertView;


	}

	protected void Displaydeletedialog(String imgid2) {
		 deleteImagesDialog(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.delete_message), activity.getResources()
                         .getString(R.string.delete), activity.getResources().getString(R.string.cancel), "OKCANCEL");
		
	}
	 Dialog deleteDialog;
	private void deleteImagesDialog(Activity activity2, String alerttitle,String messagetxt, String btndeleteTxt, String btncanceltxt, String btnvalue) {
		 if (deleteDialog != null)
	            deleteDialog.dismiss();
	        deleteDialog = AppCommonDialog.showCustomAlertDialog(activity2,
	                alerttitle, messagetxt, btndeleteTxt, btncanceltxt, btnvalue);
	        deleteDialog.show();

	        Button btndelete = (Button) deleteDialog.findViewById(R.id.btn_cancel);
	        Button btnCancel = (Button) deleteDialog.findViewById(R.id.xbtnok);

	        btndelete.setText(activity.getResources().getString(R.string.delete));
	        btnCancel.setText(activity.getResources().getString(R.string.cancel));

	        btndelete.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                deleteDialog.dismiss();
	                if (GetWebServiceData.isNetworkAvailable(activity)) {
                       if(imgid!=null){
						 new DeleteImageAsyncTask().execute();
                       }else{
                    	   
                       }

					} else {

						AppCommonDialog.showSimpleDialog(activity, activity.getResources()
								.getString(R.string.app_name),
								activity.getResources().getString(R.string.check_network),
								activity.getResources().getString(R.string.ok), "OK");
					}

	            }
	        });

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
		
	}

	// Requestdetail Task
		public class DeleteImageAsyncTask extends AsyncTask<String, Void, String> {
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				try {
					loadingDialog = new ProgressDialog(activity);
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
					nameValuePairs = new ArrayList<NameValuePair>(2);
				} catch (Exception e) {

				}
				
	                nameValuePairs.add(new BasicNameValuePair("RequestID",AppSetting.getRequestId()));
					nameValuePairs.add(new BasicNameValuePair("LayoutDetailID",imgid));
				
				

				

				// Log.d("request", +);
				return GetWebServiceData.getServerResponse(url
						+ "/DeleteFile", nameValuePairs, data);

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
							Toast.makeText(activity,
									"sucessfully " + loginResult,
									Toast.LENGTH_SHORT).show();
							
							
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
