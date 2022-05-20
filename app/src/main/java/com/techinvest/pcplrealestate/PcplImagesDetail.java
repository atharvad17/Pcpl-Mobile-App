package com.techinvest.pcplrealestate;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.techinvest.pcpl.adapter.ImageDetailAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppDetails;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.DisplayImagesResponse;
import com.techinvest.pcpl.model.ImageDetailResponse;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;

public class PcplImagesDetail extends Activity
{
	Activity activity;
	List<ImageDetailResponse> imageData;
	private ProgressDialog loadingDialog;
	String url = AppSetting.getapiURL();
	SwipeRefreshLayout swipeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagedetail);
		activity=PcplImagesDetail.this;
		 prepareActivity();
		 imageData=new ArrayList<ImageDetailResponse>();
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		 
		 ((TextView)findViewById(R.id.xtxtheaderTittle)).setText(getResources().getString(R.string.title_activity_imagedetail));
	        ((ImageButton)findViewById(R.id.backId)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					finish();
				}
			});

		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
                finish();
                startActivity(new Intent(PcplImagesDetail.this,PcplImagesDetail.class));
				new GetImagesAsyncTask().execute();
			}
		});
		  if (GetWebServiceData.isNetworkAvailable(PcplImagesDetail.this)) {

				new GetImagesAsyncTask().execute();

			} else {

				AppCommonDialog.showSimpleDialog(PcplImagesDetail.this, getResources()
						.getString(R.string.app_name),
						getResources().getString(R.string.check_network),
						getResources().getString(R.string.ok), "OK");
			}
	}

	private void prepareActivity() {
		AppDetails.setContext(this);
		AppDetails.setActivity(this);

	}
	
	// Requestdetail Task
	 public class GetImagesAsyncTask extends AsyncTask<String, Void, String>
	{

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
				nameValuePairs.add(new BasicNameValuePair("requestID", AppSetting.getRequestId()));
			}

			return GetWebServiceData.getServerResponse(url	+ "/DisplayFile", nameValuePairs, data);

		}

		@Override
		protected void onPostExecute(String result)
		{
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

				try {
					JSONObject serverResponsere = new JSONObject(result);
					Log.d("addResActivity", "Save Review Successful");
					DisplayImagesResponse serverResponseRequest = new Gson()
							.fromJson(result, DisplayImagesResponse.class);

					String loginResult = serverResponsere.getString("status");
					if (loginResult.equals("OK"))
					{
						Log.d("request detail", "get data");

						if (serverResponseRequest != null && serverResponseRequest.getValuesForImages() != null)
						{
							//Log.e("000","Before : "+imageData.get(0).getColumn1());
                            imageData.clear();
							//imageData=null;
							for (ImageDetailResponse requestdata : serverResponseRequest.getValuesForImages())
							{	
								Log.e("000","\n\nIn Data From Server : "+requestdata.getColumn1() +" Array Pos : "+imageData.size());
                                imageData.add(requestdata);
							}
							Log.e("000"," Size : "+imageData.size());
							  if(imageData!=null  && imageData.size()>0)
							  {
									((GridView)findViewById(R.id.xgrdview)).setAdapter(new ImageDetailAdapter(PcplImagesDetail.this,imageData));
                                  //((GridView)findViewById(R.id.swiperefresh)).setAdapter(new MyImageDetailAdapter(imageData));
							  }
                              else{
                                  Toast.makeText(getApplicationContext(), " Images not available please add images  ", Toast.LENGTH_LONG).show();}
						}
					}
					else {
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
			swipeLayout.setRefreshing(false);
		}
	}

    public class MyImageDetailAdapter extends BaseAdapter
    {
		//Activity activity;
		List<ImageDetailResponse> imageDataa;
		String imgid;
		String url = AppSetting.getapiURL();
		private ProgressDialog loadingDialog;

        public MyImageDetailAdapter(List<ImageDetailResponse> imageData)
        {
            this.imageDataa = imageData;
            Log.e("000","Size"+imageDataa.size());
        }

        @Override
        public int getCount() {
            return imageDataa.size();
        }

        @Override
        public Object getItem(int position) {
            return imageDataa.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public class Holder
        {
            ImageView imgbuilding;
            ImageView imgdelete;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
		{
            /*ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(Integer.parseInt(imageDataa.get(position).getColumn1()));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(70, 70));*/

            final Holder holder = new Holder();
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagedisplay , parent, false);
            holder.imgbuilding = (ImageView) itemView.findViewById(R.id.ximgdisplay);
            holder.imgdelete = (ImageView) itemView.findViewById(R.id.imgdelete);
            holder.imgdelete.setTag(imageData.get(position).getLayOutDetails());
            Picasso.with(PcplImagesDetail.this)
                    .load(imageData.get(position).getColumn1())
                    .placeholder(R.drawable.plus_icon)
                    .error(R.drawable.plus_icon)
                    .fit()
                    .centerCrop()
                    .into(holder.imgbuilding);
            holder.imgdelete.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    imgid=(String) holder.imgdelete.getTag();
                    Displaydeletedialog(imgid);
                }
            });

            Log.e("000",""+imageDataa.get(position).getColumn1()+" Pos "+position);
            return itemView;
        }

        protected void Displaydeletedialog(String imgid2)
        {
            deleteImagesDialog(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.delete_message), activity.getResources().getString(R.string.delete), activity.getResources().getString(R.string.cancel), "OKCANCEL");
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

        }

        // RequestDetail Task
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
            protected String doInBackground(String... params)
            {
                // TODO Auto-generated method stub
                String data = "";
                List<NameValuePair> nameValuePairs = null;

                try { nameValuePairs = new ArrayList<NameValuePair>(2);
                } catch (Exception e) {          }

                nameValuePairs.add(new BasicNameValuePair("RequestID",AppSetting.getRequestId()));
                nameValuePairs.add(new BasicNameValuePair("LayoutDetailID",imgid));

                // Log.d("request", +);
                return GetWebServiceData.getServerResponse(url + "/DeleteFile", nameValuePairs, data);
            }

            @Override
            protected void onPostExecute(String result)
            {
                super.onPostExecute(result);
                try
                {
                    if (loadingDialog != null && loadingDialog.isShowing())
                    {loadingDialog.dismiss(); }

                } catch (IllegalArgumentException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                if (result != null)
                {
                    Log.i("Response", result);
                    try {
                        JSONObject serverResponsere = new JSONObject(result);

                        CommonResponse serverResponseRequest = new Gson().fromJson(result, CommonResponse.class);

                        String loginResult = serverResponsere.getString("status");
                        if (loginResult.equals("OK")) {
                            Log.d("reuest detail", "get data");
                            Toast.makeText(activity,"Image Deleted Successfully" , Toast.LENGTH_SHORT).show();
                            Toast.makeText(activity,"Please refresh screen to continue" , Toast.LENGTH_LONG).show();
                            new GetImagesAsyncTask().execute();
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

}


