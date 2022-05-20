package com.techinvest.pcplrealestate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppDetails;
import com.techinvest.pcpl.commonutil.AppSetting;


public class WelcomeActivity extends Activity {

   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        prepareActivity();
        // this condition when installed application and click from open handle instance of icon launch.
        if (!isTaskRoot()) {

            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
   
      
        startSplashScreen_Thread();
    }

    private void startSplashScreen_Thread() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                // FIXME Revert Back to HOME
                if (AppSetting.getRemeberMe()) {
                  
                    Intent intent = new Intent(WelcomeActivity.this,SecondScreenActivity.class);
                  //  Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
					startActivity(intent);
                    finish();
                  
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                 
                }
            }
        }, AppConstants.SPLASH_TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    protected void onStop()
    {
      
        super.onStop();
    }
    
    private void prepareActivity() {
		AppDetails.setContext(this);
		AppDetails.setActivity(this);

	}
}

