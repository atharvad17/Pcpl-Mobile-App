package com.techinvest.pcplrealestate;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;


/**
 * Created by Sandip .
 */
public class PcplApplication extends Application
{
    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    private String serverURL;

    private static PcplApplication mInstance;
    public static final String TAG = PcplApplication.class
            .getSimpleName();
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        mInstance = this;
    }
    public static synchronized PcplApplication getInstance() {
        return mInstance;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
   /* public String getAPIURL() {

        return serverURL;
    }

    public void setAPIURL(String Locationstring) {

        this.serverURL = Locationstring;
    }*/




}
