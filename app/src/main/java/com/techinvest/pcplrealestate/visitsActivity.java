package com.techinvest.pcplrealestate;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.techinvest.pcpl.adapter.UnAssignedReqListViewAdapter;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.Jj;
import com.techinvest.pcpl.model.UnsignedReq;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class visitsActivity extends AppCompatActivity {

    public String TAG = "visitsActivity";
    String url = AppSetting.getapiURL();
    UnAssignedReqListViewAdapter adapter;
    ListView list;
    // Search EditText
    EditText editsearch;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);
        Log.d(TAG, url + "/GetUnAssignedRequest");
        list = (ListView)findViewById(R.id.list);
        editsearch = (EditText) findViewById(R.id.search);

        new GetUnassignedReqBackgroundTask().execute();
        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


    }

    private class GetUnassignedReqBackgroundTask extends AsyncTask<String, Void, String>
    {
        final ProgressDialog pDialog = new ProgressDialog(visitsActivity.this);
        @Override
        protected void onPreExecute() {
            pDialog.setMessage("Loading...");
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params)
        {
            // TODO Auto-generated method stub
            String data = "";
            List<NameValuePair> nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("",""));

            return GetWebServiceData.getServerResponse(url + "/GetUnAssignedRequest",nameValuePairs, data);
        }

        @Override
        protected void onPostExecute(String result)
        {
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

            if (result != null && !result.equals(""))
            {
                Log.i("Request", result);
                try
                {
                    JSONObject serverResponsere = new JSONObject(result);
                    Log.d(TAG, "Save Review Successful");
                    UnsignedReq obj = new Gson().fromJson(result, UnsignedReq.class);
                    List<Jj> jjList=new ArrayList<Jj>();
                    for(int i=0;i<obj.getJj().size();i++)
                    {
                        jjList.add(obj.getJj().get(i));
                    }
                    adapter=new UnAssignedReqListViewAdapter(visitsActivity.this, jjList);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.d("LoginActivity", "JSON Result parse error");
                    e.printStackTrace();
                }

            }
        }
    }
}
