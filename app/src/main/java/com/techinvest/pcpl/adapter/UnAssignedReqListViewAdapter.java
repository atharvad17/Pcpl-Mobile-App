package com.techinvest.pcpl.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.Jj;
import com.techinvest.pcpl.model.UnsignedReq;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.SecondScreenActivity;
import com.techinvest.pcplrealestate.visitsActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by shashank on 2/18/2018.
 */

public class UnAssignedReqListViewAdapter extends BaseAdapter {

    private Activity activity;
    List<Jj> jj = null;
    ArrayList<Jj> arraylist;
    LayoutInflater inflater;
    String url = AppSetting.getapiURL();
    public static String selectedReqID;

    public UnAssignedReqListViewAdapter(Activity a, List<Jj> ur) {
        activity = a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        jj = ur;

        this.arraylist = new ArrayList<Jj>();
        this.arraylist.addAll(jj);
    }

    public int getCount() {
        return jj.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.row_requests, null);

        TextView clientNameTextView = (TextView) vi.findViewById(R.id.textViewApplicantName); // title
        TextView areaTextView = (TextView) vi.findViewById(R.id.textViewArea); // artist name
        TextView branchTextView = (TextView) vi.findViewById(R.id.textViewBranch); // duration
        ImageView assignReqImageView = (ImageView) vi.findViewById(R.id.imageViewGetRequest); // thumb image

        clientNameTextView.setText("" + jj.get(position).getApplicantName());
        areaTextView.setText("" + jj.get(position).getAreaOrWard());
        branchTextView.setText("" + jj.get(position).getBranchName());

        assignReqImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogBox = new Dialog(activity, R.style.DialogSlideAnim);
                dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogBox.setContentView(R.layout.customer_popupdialog_req_detail);
                TextView textTitle = (TextView) dialogBox.findViewById(R.id.xtxttitle);
                textTitle.setText("Request Detail");
                // set the custom dialog components - text, image and button
                if (jj != null && jj.size() > 0) {
                    ((TextView) dialogBox.findViewById(R.id.xedtArea)).setText(jj.get(position).getAreaOrWard());
                    ((TextView) dialogBox.findViewById(R.id.xedtApplicantName)).setText(jj.get(position).getApplicantName());
                    ((TextView) dialogBox.findViewById(R.id.xedtBranch)).setText(jj.get(position).getBranchName());
                    ((TextView) dialogBox.findViewById(R.id.xedtCustomer)).setText(jj.get(position).getClientName());
                }

                Button btnproceed = (Button) dialogBox.findViewById(R.id.xbtnok);
                Button btnCancel = (Button) dialogBox.findViewById(R.id.xbtncancel);
                // btnsendemail.setText("OK");
                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialogBox.dismiss();
                    }
                });
                // if button is clicked, close the custom dialog
                btnproceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedReqID = jj.get(position).getRequestID();
                        Log.e("12345",""+position);
                       // arraylist.remove(position);
                        //0009
                        Jj tempItem = null;
                        boolean isItemExisted = false;
                        for (Jj item : arraylist) {
                            if (item.getRequestID().equals(jj.get(position).getRequestID())) {
                                tempItem = item;
                                isItemExisted = true;
                                break;
                            }
                        }
                        if (isItemExisted) {
                            arraylist.remove(tempItem);
                        }
                        jj.remove(position);

                        notifyDataSetChanged();
                        new ShowAssignReqBackgroundTask().execute();
                        dialogBox.dismiss();
                    }
                });
                dialogBox.show();
            }
        });

        return vi;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class ShowAssignReqBackgroundTask extends AsyncTask<String, Void, String> {
        final ProgressDialog pDialog = new ProgressDialog(activity);

        @Override
        protected void onPreExecute() {
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String data = "";
            List<NameValuePair> nameValuePairs;
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("RequestID", selectedReqID));
            nameValuePairs.add(new BasicNameValuePair("UserID", AppSetting.getUserId()));
            Log.d("123456", data.toString());
            return GetWebServiceData.getServerResponse(url + "/SHOW_AssignRequest", nameValuePairs, data);
        }

        @Override
        protected void onPostExecute(String result) {
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

            if (result != null && !result.equals("")) {
                Log.d("123456", result);
               /* try
                {
                    *//*UnsignedReq obj = new Gson().fromJson(result, UnsignedReq.class);
                    String loginResult = obj.getJj().get(0).getApplicantName();
                    List<Jj> jjList=new ArrayList<Jj>();
                    for(int i=0;i<obj.getJj().size();i++)
                    {    jjList.add(obj.getJj().get(i));    }
                    adapter=new UnAssignedReqListViewAdapter(visitsActivity.this, jjList);
                    list.setAdapter(adapter);*//*
                    //Log.d(TAG, loginResult);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.d("LoginActivity", "JSON Result parse error");
                    e.printStackTrace();
                }*/
                notifyDataSetChanged();
                Toast.makeText(activity,"Request has been Assigned Successfully !!",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(pDialog.getContext(), SecondScreenActivity.class);
//                pDialog.getContext().startActivity(intent);
            }
        }
    }
    // Filter Class
    public void filter(String charText) {
        /*if(charText.equals(null) || charText.equals(""))
        {
            Toast.makeText(activity,"Please search a keyword",Toast.LENGTH_LONG).show();
            return;
        }*/
        charText = charText.toLowerCase(Locale.getDefault());
        jj.clear();
        if (charText.length() == 0) {
            jj.addAll(arraylist);
        }
        else
        {
            for (Jj wp : arraylist)
            {
                if (wp.getApplicantName().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getAreaOrWard().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getClientName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    jj.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
