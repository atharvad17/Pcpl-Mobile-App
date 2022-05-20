package com.techinvest.pcpl.commonutil;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techinvest.pcplrealestate.Layoutdetails;
import com.techinvest.pcplrealestate.LoginActivity;
import com.techinvest.pcplrealestate.MapviewActivity;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.SecondScreenActivity;


@SuppressLint("NewApi")
public class AppCommonDialog implements AppConstants {

    public static Dialog dialog;

    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        // TODO Auto-generated method stub
        TelephonyManager telephonyManager = (TelephonyManager) AppDetails
                .getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getDeviceModelName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    public static androidx.appcompat.app.AlertDialog showCustomAlertDialog(final Context context,
                                                                                  String alerttitle, String alerttxt, String btnok, String btnCancel, String btntype) {
        // TODO Auto-generated method stub
        Resources strRes = context.getResources();
        //final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.custom_dailog_box, null);
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(context);
        builder
                .setView(dialogView);
                /*.setTitle(alerttitle)
                .setMessage(alerttxt)
                .setCancelable(false);*/

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setContentView(dialogView,
                new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
       /* dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dailog_box);*/
        //	LinearLayout linlayrotation=(LinearLayout)dialog.findViewById(R.id.xlinlayRotation);
       // dialog.setContentView(R.layout.custom_dailog_box);


        TextView alertTxtTitle = (TextView) dialog
                .findViewById(R.id.xtxttitle);

        TextView alertTxtMsg = (TextView) dialog.findViewById(R.id.xtxtmessage);

        Button buttonCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        Button buttonOk = (Button) dialog.findViewById(R.id.xbtnok);


        alertTxtMsg.setText(alerttxt);
        if (alerttitle != null) {
            alertTxtTitle.setText(alerttitle.toUpperCase());
        } else {
        }
        alertTxtTitle.setTextSize(20);


        AppConstants.Buttons btnValue = AppConstants.Buttons.valueOf(btntype.toUpperCase());

        switch (btnValue) {
            case OK:
                buttonOk.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);
                buttonOk.setText(strRes.getString(R.string.ok).toUpperCase());

                buttonOk.setTextSize(20);
                buttonCancel.setTextSize(20);
                buttonOk.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                break;
      
            case CANCEL:
                buttonCancel.setVisibility(View.VISIBLE);
                buttonOk.setVisibility(View.GONE);
                //buttonOk.setText(strRes.getString(R.string.retry).toUpperCase());

                buttonOk.setTextSize(20);
                buttonCancel.setTextSize(20);
                buttonCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                break;

            case OKCANCEL:

                buttonOk.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.VISIBLE);

                buttonOk.setTextSize(20);
                buttonCancel.setTextSize(20);
            buttonOk.setText(strRes.getString(R.string.ok));
                buttonCancel.setText(strRes.getString(R.string.cancel));
                ///buttonRetry.setVisibility(View.GONE);

                buttonCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                break;

            default:
                break;
        }
        return dialog;
    }


    @SuppressLint("NewApi")
    public static ProgressDialog createProgressDialog(Activity mContext) {

        ProgressDialog dialog = new ProgressDialog(mContext);
        LayoutInflater inflator = mContext.getLayoutInflater();
        View dialogView = inflator.inflate(R.layout.loading_view,null);

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);

        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // String pPath = null;
       /* try {
            dialog.show();
        } catch (BadTokenException e) {

        }*/
        dialog.setCancelable(false);
        //dialog.onTouchEvent(null);
        dialog.setContentView(dialogView,
                new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.findViewById(R.id.webview).setVisibility(View.GONE);
        dialog.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        Activity activity = (Activity)mContext;
        if(!activity.isFinishing())
            dialog.show();
        // }
        return dialog;
    }

    static ProgressBar customProgressBar;
    static TextView textView_progress;

   


    public static Dialog showSimpleDialog(Activity context,
                                          String alerttitle, String message, String btnok, String btntype) {
        Resources strRes = context.getResources();


        // Create the AlertDialog object and return it

        /*final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);*/
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.custom_dailog_ok, null);
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(context);
        builder
                .setView(dialogView);
                /*.setTitle(alerttitle)
                .setMessage(alerttxt)
                .setCancelable(false);*/

        androidx.appcompat.app.AlertDialog dialog = builder.create();

        dialog.setContentView(dialogView,
                new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

      /*  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.custom_dailog_ok);*/
        TextView textTitle = (TextView) dialog.findViewById(R.id.cr_alertdialog_title);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.xalertdialog_textview);
            text.setText((message));
        


        if (alerttitle != null) {
                textTitle.setText(alerttitle.toUpperCase());

        } else {
               // textTitle.setBackgroundResource(R.drawable.ic_button_correct);
           
        }


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_confirm_ok);

        AppConstants.Buttons btnValue = AppConstants.Buttons.valueOf(btntype.toUpperCase());
        switch (btnValue) {
            case OK:

                dialogButton.setText(strRes.getString(R.string.ok));
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                break;

            default:
                break;
        }

        Activity activity = (Activity)context;

        if(!activity.isFinishing()) {
            dialog.show();
        }

        return dialog;

    }


   
    
    // Alert highlight fields
    public static void setErrorMsg(String msg, EditText et,
                                   boolean isRequestFocus) {
        int ecolor = Color.RED; // whatever color you want
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(msg);
        ssbuilder.setSpan(fgcspan, 0, msg.length(), 0);

        if (isRequestFocus) {
            et.requestFocus();
        }
        et.setError(ssbuilder);
    }

    // Alert highlight fields
    public static void setErrorMsgOnTextView(String msg, TextView et,
                                             boolean isRequestFocus) {
        int ecolor = Color.RED; // whatever color you want
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(msg);
        ssbuilder.setSpan(fgcspan, 0, msg.length(), 0);
        if (isRequestFocus) {
            et.requestFocus();
        }
        et.setError(ssbuilder);
    }



}
