package com.techinvest.pcplrealestate.fragment;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.techinvest.pcpl.adapter.LocationDetailAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.commonutil.DateFormatter;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.GetLocationResponse;
import com.techinvest.pcpl.model.GetProjectDetailsResponse;
import com.techinvest.pcpl.model.LocationDetailsResponse;
import com.techinvest.pcpl.model.Offlinedatamodel;
import com.techinvest.pcpl.model.ProjectTabResponse;
import com.techinvest.pcpl.model.RequestDropdownData;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.SecondScreenActivity;

@SuppressLint("NewApi")
public class Projectdetails extends Fragment implements AppConstants, OnClickListener {
    private View view;
    Activity activity;
    private ProgressDialog loadingDialog;
    String url = AppSetting.getapiURL();
    List<GetProjectDetailsResponse> getProjectData;

    ListPopupWindow lstbuildingName;
    ListPopupWindow lstArchitect;
    ListPopupWindow lstCCNo;
    ListPopupWindow lstPlanNo;
    ListPopupWindow lstLiftInstalled;
    ListPopupWindow lstPlinth;
    ListPopupWindow lstShareDNo;
    ListPopupWindow lstShareNo;
    ListPopupWindow lstSocName;
    ListPopupWindow lstsocRegNo;
    ListPopupWindow lstUnitDetails;
    ListPopupWindow lstnoofFloor;
    ListPopupWindow lstRcc;
    ListPopupWindow lstExternalbrickworkcompletedfrom;
    ListPopupWindow lstInternalbrickworkcompletedfrom;
    ListPopupWindow lstInternalplastercompleted;
    ListPopupWindow lstExternalplastercompleted;
    ListPopupWindow lstexternalpaintingcompleted;
    ListPopupWindow lstInternalpaintingcompleted;
    ListPopupWindow lstflooringcompleted;
    ListPopupWindow lstwoodworker;
    ListPopupWindow lstslab;
    ListPopupWindow lstTotalnoofFloor;


    List<String> buildingNameData;
    List<String> architectData;
    List<String> cCNoData;
    List<String> planNoData;
    List<String> liftInstalledData;
    List<String> plinthData;
    List<String> shareDNoData;
    List<String> shareNoData;
    List<String> socNameData;
    List<String> socRegNoData;
    List<String> unitDetailsData;
    List<String> noofFloor;
    List<String> totalnoofSlab;
    List<String> totalRccfloor;

    private int year;
    private int month;
    private int day;
    private boolean bselectdate;
    static final int DATE_PICKER_ID = 1111;
    String buildingYear;
    String ageofBuilding;
    int age;
    EditText edtAgeofBuilding;
    EditText edtConstrationYear;
    TextWatcher txtedtyear;
    TextWatcher txtedtAge;
    int yearproudct;
    List<Offlinedatamodel> offlineData;
    List<SetOfflinedata> projectDataList;
    LinearLayout noOfSlabsId, projectCompletionId, internalBrickworkId, internalPaintingId, noOfFloorsId, internalPlasterId, projectStartId,
            flooringCompleteId, plinthCompleteId, externalBrickworkId, externalPlasterId, externalPaintingId,
            rccId, woodworkPlumbingId, otherId, yearsOfConstructionId, ageOfBuildingId;
    CheckBox xcheckBoxAllworkcompleted;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.projectdetails_fragment, container, false);

        noOfSlabsId = view.findViewById(R.id.noOfSlabsId);
        projectCompletionId = view.findViewById(R.id.projectCompletionId);
        internalBrickworkId = view.findViewById(R.id.internalBrickworkId);
        internalPaintingId = view.findViewById(R.id.internalPaintingId);
        noOfFloorsId = view.findViewById(R.id.noOfFloorsId);
        internalPlasterId = view.findViewById(R.id.internalPlasterId);
        projectStartId = view.findViewById(R.id.projectStartId);
        xcheckBoxAllworkcompleted = view.findViewById(R.id.xcheckBoxAllworkcompleted);
        flooringCompleteId = view.findViewById(R.id.flooringCompleteId);
        plinthCompleteId = view.findViewById(R.id.plinthCompleteId);
        externalBrickworkId = view.findViewById(R.id.externalBrickworkId);
        externalPlasterId = view.findViewById(R.id.externalPlasterId);
        externalPaintingId = view.findViewById(R.id.externalPaintingId);
        rccId = view.findViewById(R.id.rccId);
        woodworkPlumbingId = view.findViewById(R.id.woodworkPlumbingId);
        otherId = view.findViewById(R.id.otherId);
        yearsOfConstructionId = view.findViewById(R.id.yearsOfConstructionId);
        ageOfBuildingId = view.findViewById(R.id.ageOfBuildingId);

        edtAgeofBuilding = (EditText) view.findViewById(R.id.xedtageofbuilding);
        edtConstrationYear = (EditText) view.findViewById(R.id.xedtyearofconstruction);
        getProjectData = new ArrayList<GetProjectDetailsResponse>();
        buildingNameData = new ArrayList<String>();
        architectData = new ArrayList<String>();
        cCNoData = new ArrayList<String>();
        planNoData = new ArrayList<String>();
        shareDNoData = new ArrayList<String>();
        shareNoData = new ArrayList<String>();
        liftInstalledData = new ArrayList<String>();
        plinthData = new ArrayList<String>();
        socRegNoData = new ArrayList<String>();
        socNameData = new ArrayList<String>();

        unitDetailsData = new ArrayList<String>();

        lstbuildingName = new ListPopupWindow(getActivity());
        lstArchitect = new ListPopupWindow(getActivity());
        lstCCNo = new ListPopupWindow(getActivity());
        lstPlanNo = new ListPopupWindow(getActivity());
        lstLiftInstalled = new ListPopupWindow(getActivity());
        lstPlinth = new ListPopupWindow(getActivity());
        lstShareDNo = new ListPopupWindow(getActivity());
        lstsocRegNo = new ListPopupWindow(getActivity());
        lstUnitDetails = new ListPopupWindow(getActivity());
        lstSocName = new ListPopupWindow(getActivity());
        lstShareNo = new ListPopupWindow(getActivity());
        lstnoofFloor = new ListPopupWindow(getActivity());
        lstRcc = new ListPopupWindow(getActivity());
        lstExternalbrickworkcompletedfrom = new ListPopupWindow(getActivity());
        lstInternalbrickworkcompletedfrom = new ListPopupWindow(getActivity());
        lstInternalplastercompleted = new ListPopupWindow(getActivity());
        lstExternalplastercompleted = new ListPopupWindow(getActivity());
        lstexternalpaintingcompleted = new ListPopupWindow(getActivity());
        lstInternalpaintingcompleted = new ListPopupWindow(getActivity());
        lstflooringcompleted = new ListPopupWindow(getActivity());
        lstwoodworker = new ListPopupWindow(getActivity());
        lstslab = new ListPopupWindow(getActivity());
        lstTotalnoofFloor = new ListPopupWindow(getActivity());

        noofFloor = new ArrayList<String>();
        totalRccfloor = new ArrayList<String>();
        totalnoofSlab = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        offlineData = new ArrayList<Offlinedatamodel>();
        projectDataList = new ArrayList<SetOfflinedata>();

        if (offlineData != null) {
            offlineData = SecondScreenActivity.mypcplData.getProjectDetail(AppSetting.getRequestId());
        }

        for (int i = 0; i < 60; i++) {

            if (i == 0) {
                noofFloor.add(" Ground floor");
            } else if (i == 1) {
                noofFloor.add(String.valueOf(i) + " " + "st floor");
            } else if (i == 2) {
                noofFloor.add(String.valueOf(i) + " " + "nd floor");
            } else if (i == 3) {
                noofFloor.add(String.valueOf(i) + " " + "rd floor");
            } else {
                noofFloor.add(String.valueOf(i) + " " + "th floor");
            }
        }

        for (int i = 0; i < 60; i++) {
            if (i == 0) {
                totalRccfloor.add("Ground floor");
            } else if (i == 1) {
                totalRccfloor.add(String.valueOf(i) + " " + "st Slab");
            } else if (i == 2) {
                totalRccfloor.add(String.valueOf(i) + " " + "nd Slab");
            } else if (i == 3) {
                totalRccfloor.add(String.valueOf(i) + " " + "rd Slab");
            } else {
                totalRccfloor.add(String.valueOf(i) + " " + "th Slab");
            }
        }
        for (int i = 0; i < 100; i++) {
            totalnoofSlab.add(String.valueOf(i));
        }

        initview(view);

        if (GetWebServiceData.isNetworkAvailable(getActivity())) {
            new ProjectDetailAsyncTask().execute();

        } else {
            if (offlineData.size() > 0) {
                for (Offlinedatamodel datalocal : offlineData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestid().equals(AppSetting.getRequestId())) {

                        ProjectTabResponse serverResponseRequest = new Gson()
                                .fromJson(datalocal.getResponsejson(), ProjectTabResponse.class);
                        displaydataOffOn(serverResponseRequest);
                    }
                }

                updateandDisplayofflineData(AppSetting.getRequestId());

                /*AppCommonDialog.showSimpleDialog(getActivity(), getResources()
                                .getString(R.string.app_name),
                        getResources().getString(R.string.check_network),
                        getResources().getString(R.string.ok), "OK");*/
            }
        }


        txtedtyear = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                edtConstrationYear.append("");
                edtConstrationYear.setSelection(edtConstrationYear.getText().length());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!(edtConstrationYear.getText().toString().equalsIgnoreCase(""))) {

                    buildingYear = edtConstrationYear.getText().toString();

                    if (year != 0 && buildingYear != null && buildingYear.length() > 0) {
                        age = year - Integer.parseInt(buildingYear);
                        ageofBuilding = String.valueOf(age);
                        edtConstrationYear.removeTextChangedListener(txtedtyear);
                        edtAgeofBuilding.setText(ageofBuilding);
                        edtConstrationYear.addTextChangedListener(txtedtyear);
                    }
                } else {
                }
            }
        };
        edtConstrationYear.addTextChangedListener(txtedtyear);

        txtedtAge = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                edtAgeofBuilding.append("");
                edtAgeofBuilding.setSelection(edtAgeofBuilding.getText().length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!(edtAgeofBuilding.getText().toString().equalsIgnoreCase(""))) {
                    ageofBuilding = edtAgeofBuilding.getText().toString();

                    if (year != 0 && ageofBuilding != null && ageofBuilding.length() > 0) {
                        yearproudct = year - Integer.parseInt(ageofBuilding);
                        buildingYear = String.valueOf(yearproudct);
                        edtAgeofBuilding.removeTextChangedListener(txtedtAge);
                        edtConstrationYear.setText(buildingYear);
                        edtAgeofBuilding.addTextChangedListener(txtedtAge);
                    }
                }
            }
        };
        edtAgeofBuilding.addTextChangedListener(txtedtAge);
     
     
	
	
	/*((EditText)view.findViewById(R.id.xedtyearofconstruction)).addTextChangedListener(new TextWatcher() {

		   public void afterTextChanged(Editable s) {
		   }
		 
		   public void beforeTextChanged(CharSequence s, int start, 
		     int count, int after) {
		   }
		 
		   public void onTextChanged(CharSequence s, int start, 
		     int before, int count) {
			 String yearofBuilding  = s.toString();
			 
			 if(yearofBuilding!=null&&yearofBuilding.length()>0)
			     buildingYear=Double.parseDouble(yearofBuilding);
			    if(year!=0 && buildingYear!=0){
				   ageofBuilding=year-buildingYear;
				 ((EditText)view.findViewById(R.id.xedtageofbuilding)).setText(String.valueOf(ageofBuilding));
				    
				}
		   }
		  });
	
	
	((EditText)view.findViewById(R.id.xedtageofbuilding)).addTextChangedListener(new TextWatcher() {
		  
		   public void afterTextChanged(Editable s) {
		   }
		 
		   public void beforeTextChanged(CharSequence s, int start, 
		     int count, int after) {
		   }
		 
		   public void onTextChanged(CharSequence s, int start, 
		     int before, int count) {
			 String strageOfBuilding  = s.toString();
			 
			 if(strageOfBuilding!=null&&strageOfBuilding.length()>0)
				 ageofBuilding=Double.parseDouble(strageOfBuilding);
			    if(year!=0 && ageofBuilding!=0){
			    	buildingYear=year-ageofBuilding;
				 ((EditText)view.findViewById(R.id.xedtyearofconstruction)).setText(String.valueOf(buildingYear));
				    
				}
		   }
		  });*/

        lstbuildingName.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //if(parent.getItemAtPosition(position).toString()!=null)
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildername)).setText(selecteditem);
                lstbuildingName.dismiss();

            }
        });


        lstArchitect.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoarchitech)).setText(selecteditem);
                lstArchitect.dismiss();

            }
        });


        lstPlanNo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplanno)).setText(selecteditem);
                lstPlanNo.dismiss();

            }
        });

        lstShareNo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position)
                        .toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).setText(selecteditem);
                lstShareNo.dismiss();

            }
        });

        lstCCNo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoccno)).setText(selecteditem);
                lstCCNo.dismiss();

            }
        });


        lstShareDNo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).setText(selecteditem);
                lstShareDNo.dismiss();

            }
        });


        lstsocRegNo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautosocietyno)).setText(selecteditem);
                lstsocRegNo.dismiss();

            }
        });


        lstUnitDetails.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoUnitdetails)).setText(selecteditem);
                lstUnitDetails.dismiss();

            }
        });


        lstPlinth.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplinthcomplited)).setText(selecteditem);
                lstPlinth.dismiss();

            }
        });


        lstnoofFloor.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).setText(selecteditem);
                lstnoofFloor.dismiss();

            }
        });


        lstRcc.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautorcccompleted)).setText(selecteditem);
                lstRcc.dismiss();

            }
        });
        lstExternalbrickworkcompletedfrom.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalbrickworkcompletedfrom)).setText(selecteditem);
                lstExternalbrickworkcompletedfrom.dismiss();

            }
        });


        lstInternalbrickworkcompletedfrom.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalbrickworkcompleted)).setText(selecteditem);
                lstInternalbrickworkcompletedfrom.dismiss();

            }
        });

        lstexternalpaintingcompleted.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalpaintingcompleted)).setText(selecteditem);
                lstexternalpaintingcompleted.dismiss();

            }
        });

        lstInternalpaintingcompleted.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalpaintingcompleted)).setText(selecteditem);
                lstInternalpaintingcompleted.dismiss();

            }
        });


        lstExternalplastercompleted.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalplastercompleted)).setText(selecteditem);
                lstExternalplastercompleted.dismiss();

            }
        });


        lstInternalplastercompleted.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalplastercompleted)).setText(selecteditem);
                lstInternalplastercompleted.dismiss();

            }
        });


        lstwoodworker.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautowoodworker)).setText(selecteditem);
                lstwoodworker.dismiss();

            }
        });


        lstflooringcompleted.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoflooringcompleted)).setText(selecteditem);
                lstflooringcompleted.dismiss();

            }
        });

        lstTotalnoofFloor.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautototalnooffloors)).setText(selecteditem);
                lstTotalnoofFloor.dismiss();

            }
        });

        lstslab.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNoofslabs)).setText(selecteditem);
                lstslab.dismiss();

            }
        });

        lstLiftInstalled.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String selecteditem = parent.getItemAtPosition(position).toString();
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoliftinstalled)).setText(selecteditem);
                lstLiftInstalled.dismiss();

            }
        });


        ((EditText) view.findViewById(R.id.xedtprojectcompletiondate)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bselectdate = false;
                setDate();
                return false;
            }
        });

        ((TextView) view.findViewById(R.id.xedtprojectstartdate)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bselectdate = true;
                setDate();
                return false;
            }
        });

	/*((EditText)view.findViewById(R.id.xedtprojectstartdate)).setOnClickListener(new OnClickListener() {
        @Override
		public void onClick(View v) {
			bselectdate=true;
            setDate();
			
		}
	});*/
        return view;
    }


    private void updateandDisplayofflineData(String RequestID) {
        projectDataList = SecondScreenActivity.mypcplData.getProjectAllvalues(RequestID);
        if (projectDataList != null && projectDataList.size() > 0) {
            for (SetOfflinedata dataOffline : projectDataList) {
                if (dataOffline.getSaveuserdata() != null && dataOffline.getSaveuserdata().length() > 0) {
                    ProjectTabResponse serverResponseRequest = new Gson()
                            .fromJson(dataOffline.getSaveuserdata(), ProjectTabResponse.class);
                    if (getProjectData != null && getProjectData.size() > 0)
                        getProjectData.clear();
                    for (GetProjectDetailsResponse requestdata : serverResponseRequest
                            .getValues()) {
                        getProjectData.add(requestdata);
                    }
                }


            }

            if (getProjectData != null && getProjectData.size() > 0) {
                for (GetProjectDetailsResponse datalocal : getProjectData) {
                    if (AppSetting.getRequestId() != null && datalocal.getRequestId().equals(AppSetting.getRequestId())) {
                        displayProjectDetail(getProjectData);
                    }
                }
            }
        }

    }

    @SuppressWarnings("deprecation")
    public void setDate() {
        getActivity().showDialog(DATE_PICKER_ID);
    }


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(getActivity(), pickerListener, year, month, day);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);

            Calendar c = Calendar.getInstance();

            int res = calendar.compareTo(c);

            if (res >= 0) {
                // setErrorMsg(getResources().getString(R.string.valid_date_of_birth), ((EditText) findViewById(R.id.textview_dob)), false);

                if (bselectdate) {
                    ((EditText) view.findViewById(R.id.xedtprojectstartdate)).setText("DD/MM/YYYY");
                } else {
                    ((EditText) view.findViewById(R.id.xedtprojectcompletiondate)).setText("DD/MM/YYYY");
                }
            } else {
                if (bselectdate) {
                    ((EditText) view.findViewById(R.id.xedtprojectstartdate)).setText(DateFormatter.formatDateCalendarToString("dd/MM/yyyy", calendar));
                } else {
                    ((EditText) view.findViewById(R.id.xedtprojectcompletiondate)).setText(DateFormatter.formatDateCalendarToString("dd/MM/yyyy", calendar));
                }

            }


        }
    };


    private void initview(View v) {
        // TODO Auto-generated method stub
        // for locing comment
        //v.findViewById(R.id.ximgdropdownbuildername).setOnClickListener(this);
        //v.findViewById(R.id.ximgdropdownarchitech).setOnClickListener(this);
        //v.findViewById(R.id.ximgdropdownplanno).setOnClickListener(this);
        //v.findViewById(R.id.ximgdropdownShareCertificationno).setOnClickListener(this);
        //v.findViewById(R.id.ximgdropdownccno).setOnClickListener(this);
        //v.findViewById(R.id.ximgdropdownsharedistinctiveno).setOnClickListener(this);
        //v.findViewById(R.id.ximgdropdownsocietyno).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautoUnitdetails).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownfloor).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownexternalpaintingcompleted).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdowninternalbrickworkcompleted).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautointernalpaintingcompleted).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautoexternalbrickworkcompletedfrom).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautoexternalplastercompleted).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdowninternalplastercompleted).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautowoodworker).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownflooringcompleted).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautorcccompleted).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownNoofslabs).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautototalnooffloors).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautoliftinstalled).setOnClickListener(this);
        v.findViewById(R.id.ximgdropdownautoplinthcomplited).setOnClickListener(this);

        v.findViewById(R.id.xcheckBoxDeveloped).setOnClickListener(this);
        v.findViewById(R.id.xcheckBoxUnderConstration).setOnClickListener(this);
        v.findViewById(R.id.xcheckBoxAllworkcompleted).setOnClickListener(this);

        v.findViewById(R.id.xbtnSave).setOnClickListener(this);
        // EditText edtcompletedate=(EditText) view.findViewById(R.id.xedtprojectcompletiondate);

        ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildername)).setFocusable(false);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoarchitech)).setFocusable(false);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplanno)).setFocusable(false);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).setFocusable(false);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoccno)).setFocusable(false);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).setFocusable(false);

        ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildername)).setBackgroundColor(getResources().getColor(R.color.gray));
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoarchitech)).setBackgroundColor(getResources().getColor(R.color.gray));
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplanno)).setBackgroundColor(getResources().getColor(R.color.gray));
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).setBackgroundColor(getResources().getColor(R.color.gray));
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoccno)).setBackgroundColor(getResources().getColor(R.color.gray));
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).setBackgroundColor(getResources().getColor(R.color.gray));

        try {
            setAutoFillData();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error :" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void setAutoFillData() {
        String[] tmp1 = new String[buildingNameData.size()];
        tmp1 = buildingNameData.toArray(tmp1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp1);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildername)).setAdapter(adapter1);

        String[] tmp2 = new String[architectData.size()];
        tmp2 = architectData.toArray(tmp2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp2);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoarchitech)).setAdapter(adapter2);

        String[] tmp3 = new String[planNoData.size()];
        tmp3 = planNoData.toArray(tmp3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp3);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplanno)).setAdapter(adapter3);

        String[] tmp4 = new String[shareNoData.size()];
        tmp4 = shareNoData.toArray(tmp4);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp4);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).setAdapter(adapter4);

        String[] tmp5 = new String[cCNoData.size()];
        tmp5 = cCNoData.toArray(tmp5);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp5);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoccno)).setAdapter(adapter5);

        String[] tmp6 = new String[shareDNoData.size()];
        tmp6 = shareDNoData.toArray(tmp6);
        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp6);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).setAdapter(adapter6);

        String[] tmp7 = new String[socRegNoData.size()];
        tmp7 = socRegNoData.toArray(tmp7);
        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp7);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautosocietyno)).setAdapter(adapter7);

        String[] tmp8 = new String[totalnoofSlab.size()];
        tmp8 = totalnoofSlab.toArray(tmp8);
        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp8);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNoofslabs)).setAdapter(adapter8);

        String[] tmp9 = new String[unitDetailsData.size()];
        tmp9 = unitDetailsData.toArray(tmp9);
        ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp9);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoUnitdetails)).setAdapter(adapter9);

        String[] tmp10 = new String[noofFloor.size()];
        tmp10 = noofFloor.toArray(tmp10);
        ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp10);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalbrickworkcompleted)).setAdapter(adapter10);

        String[] tmp11 = new String[totalnoofSlab.size()];
        tmp11 = totalnoofSlab.toArray(tmp11);
        ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp11);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautototalnooffloors)).setAdapter(adapter11);

        String[] tmp12 = new String[noofFloor.size()];
        tmp12 = noofFloor.toArray(tmp12);
        ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp12);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalplastercompleted)).setAdapter(adapter12);

        String[] tmp13 = new String[noofFloor.size()];
        tmp13 = noofFloor.toArray(tmp13);
        ArrayAdapter<String> adapter13 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp13);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoflooringcompleted)).setAdapter(adapter13);

        String[] tmp14 = new String[plinthData.size()];
        tmp14 = plinthData.toArray(tmp14);
        ArrayAdapter<String> adapter14 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp14);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplinthcomplited)).setAdapter(adapter14);

        String[] tmp15 = new String[totalRccfloor.size()];
        tmp15 = totalRccfloor.toArray(tmp15);
        ArrayAdapter<String> adapter15 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp15);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautorcccompleted)).setAdapter(adapter15);

		/*String[] tmp16 = new String[structureTypeData.size()];
        tmp16 = structureTypeData.toArray(tmp16);
		ArrayAdapter<String> adapter16 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,tmp16);
		((AutoCompleteTextView)view.findViewById(R.id.xedtautoOther)).setAdapter(adapter16);*/

        String[] tmp17 = new String[noofFloor.size()];
        tmp17 = noofFloor.toArray(tmp17);
        ArrayAdapter<String> adapter17 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp17);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalbrickworkcompletedfrom)).setAdapter(adapter1);

        String[] tmp18 = new String[noofFloor.size()];
        tmp18 = noofFloor.toArray(tmp18);
        ArrayAdapter<String> adapter18 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp18);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).setAdapter(adapter18);

        String[] tmp19 = new String[noofFloor.size()];
        tmp19 = noofFloor.toArray(tmp19);
        ArrayAdapter<String> adapter19 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp19);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautowoodworker)).setAdapter(adapter19);

        String[] tmp20 = new String[noofFloor.size()];
        tmp20 = noofFloor.toArray(tmp20);
        ArrayAdapter<String> adapter20 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp20);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalpaintingcompleted)).setAdapter(adapter20);

        String[] tmp21 = new String[noofFloor.size()];
        tmp21 = noofFloor.toArray(tmp21);
        ArrayAdapter<String> adapter21 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp21);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalpaintingcompleted)).setAdapter(adapter21);

        String[] tmp22 = new String[noofFloor.size()];
        tmp22 = noofFloor.toArray(tmp22);
        ArrayAdapter<String> adapter22 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp22);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalplastercompleted)).setAdapter(adapter22);

        String[] tmp23 = new String[liftInstalledData.size()];
        tmp23 = liftInstalledData.toArray(tmp23);
        ArrayAdapter<String> adapter23 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tmp23);
        ((AutoCompleteTextView) view.findViewById(R.id.xedtautoliftinstalled)).setAdapter(adapter23);

    }


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;

    }


    // Requestdetail Task
    public class ProjectDetailAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                loadingDialog = new ProgressDialog(getActivity());
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

            if (AppSetting.getRequestId() != null) {
                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("requestID",
                        AppSetting.getRequestId()));
            }
            //Log.d("LoginActivity", "Attempting Signup");
            return GetWebServiceData.getServerResponse(url
                    + "/GetProjectTabDetails", nameValuePairs, data);

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
                Log.i("Request", result);
                try {
                    JSONObject serverResponsere = new JSONObject(result);
                    Log.d("addResActivity", "Save Review Successful");
                    ProjectTabResponse serverResponseRequest = new Gson()
                            .fromJson(result, ProjectTabResponse.class);

                    String loginResult = serverResponsere.getString("status");
                    if (loginResult.equalsIgnoreCase("OK")) {
                        Log.d("request detail", "get data");

                        if (result != null && AppSetting.getRequestId() != null) {
                            //	SecondScreenActivity.mypcplData.insertProjectdata(result, AppSetting.getRequestId());

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


    public void displayProjectDetail(List<GetProjectDetailsResponse> getProjectData) {
        // TODO Auto-generated method stub
        if (getProjectData != null && getProjectData.size() > 0) {
            if (getProjectData.get(0).getConstruction() != null && getProjectData.get(0).getConstruction().equalsIgnoreCase("Developed")) {
                ((CheckBox) view.findViewById(R.id.xcheckBoxDeveloped)).setChecked(true);
                //AppSetting.setKeyCheckedValue("Developed");
                //displayworkcompleUpdate();
            } else {
                ((CheckBox) view.findViewById(R.id.xcheckBoxUnderConstration)).setChecked(true);
                //AppSetting.setKeyCheckedValue("Under Construction");
                // displayworkcompleUpdate();
            }

            if (getProjectData.get(0).getAllWorkCompleted() != null && getProjectData.get(0).getAllWorkCompleted().equals("Y")) {
                ((CheckBox) view.findViewById(R.id.xcheckBoxAllworkcompleted)).setChecked(true);
            } else {
                //((CheckBox)view.findViewById(R.id.xcheckBoxAllworkcompleted)).setChecked(false);
            }
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildername)).setText(getProjectData.get(0).getBuilder_Name());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoarchitech)).setText(getProjectData.get(0).getArchitectName());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplanno)).setText(getProjectData.get(0).getPlanNo());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).setText(getProjectData.get(0).getShareCertificationNO());

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoccno)).setText(getProjectData.get(0).getCCNo());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).setText(getProjectData.get(0).getShareDistinctiveNo());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautosocietyno)).setText(getProjectData.get(0).getSocietyRegNO());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNoofslabs)).setText(getProjectData.get(0).getNoOfSlabs());
            ((EditText) view.findViewById(R.id.xedtsocietyname)).setText(getProjectData.get(0).getSocietyName());
            ((EditText) view.findViewById(R.id.xedtprojectcompletiondate)).setText(getProjectData.get(0).getProjectCompletionDate());

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoUnitdetails)).setText(getProjectData.get(0).getUnitDetail());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalbrickworkcompleted)).setText(getProjectData.get(0).getInternalBrickWork());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautototalnooffloors)).setText(getProjectData.get(0).getTotalNoOfFloors());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalplastercompleted)).setText(getProjectData.get(0).getInternalPlaster());
            ((EditText) view.findViewById(R.id.xedtprojectstartdate)).setText(getProjectData.get(0).getProjectStartDate());

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoflooringcompleted)).setText(getProjectData.get(0).getFloorNo());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplinthcomplited)).setText(getProjectData.get(0).getPLinth());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautorcccompleted)).setText(getProjectData.get(0).getRCCCompleted());

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOther)).setText(getProjectData.get(0).getOther());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalbrickworkcompletedfrom)).setText(getProjectData.get(0).getExternalBrickWork());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).setText(getProjectData.get(0).getFlooring());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautowoodworker)).setText(getProjectData.get(0).getWoodWork());

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalpaintingcompleted)).setText(getProjectData.get(0).getExternalPainting());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalpaintingcompleted)).setText(getProjectData.get(0).getInternalPainting());
            //((AutoCompleteTextView)view.findViewById(R.id.xedtautof)).setText(getProjectData.get(0).getExternalBrickWork());

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalplastercompleted)).setText(getProjectData.get(0).getExternalPlaster());
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoliftinstalled)).setText(getProjectData.get(0).getLiftInstalled());
            ((EditText) view.findViewById(R.id.xedtMarketraterange)).setText(getProjectData.get(0).getMarketRate());
            edtAgeofBuilding.setText(getProjectData.get(0).getAgeOfBuilding());
            ((EditText) view.findViewById(R.id.xedtothercharges)).setText(getProjectData.get(0).getOtherCharge());
            edtConstrationYear.setText(getProjectData.get(0).getYearOfConstruction());
            ((EditText) view.findViewById(R.id.xedtcarpetarea)).setText(getProjectData.get(0).getCarpetArea());
            ((EditText) view.findViewById(R.id.xedtflooerise)).setText(getProjectData.get(0).getFloorRise());
    /* if(getProjectData.get(0).getYearOfConstruction()!=null&& getProjectData.get(0).getYearOfConstruction().length()>0){
	    buildingYear=Integer.parseInt(getProjectData.get(0).getYearOfConstruction());
	    if(year!=0 && buildingYear!=0){
		     ageofBuilding=year-buildingYear;
		    
		     }
	 }
	 
	 if(getProjectData.get(0).getAgeOfBuilding()!=null&&getProjectData.get(0).getAgeOfBuilding().length()>0){
		 ageofBuilding=Integer.parseInt(getProjectData.get(0).getAgeOfBuilding());
		 buildingYear=year-ageofBuilding;
	 }*/

            displayContructionProgressUpdate();
            displayworkcompleUpdate();
        }

    }


    public void displaydataOffOn(ProjectTabResponse serverResponseRequest) {
        if (serverResponseRequest != null && serverResponseRequest.getValues() != null
                && serverResponseRequest.getBuilderName() != null && serverResponseRequest.getArchitect() != null && serverResponseRequest.getCCNo() != null && serverResponseRequest.getPlanNo() != null
                && serverResponseRequest.getPlinth() != null && serverResponseRequest.getShareDNo() != null && serverResponseRequest.getShareNo() != null && serverResponseRequest.getSocName() != null && serverResponseRequest.getSocRegNo() != null
                && serverResponseRequest.getUnitDetails() != null) {

            for (GetProjectDetailsResponse projectdata : serverResponseRequest.getValues()) {
                getProjectData.add(projectdata);
            }
            for (RequestDropdownData dropdata : serverResponseRequest
                    .getBuilderName()) {
                buildingNameData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getArchitect()) {

                architectData.add(dropdata.getDatavalue());
            }
            for (RequestDropdownData dropdata : serverResponseRequest.getCCNo()) {

                cCNoData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getPlanNo()) {

                planNoData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getLiftInstalled()) {

                liftInstalledData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getPlinth()) {

                plinthData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getShareDNo()) {
                shareDNoData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getShareNo()) {
                shareNoData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getSocName()) {
                socNameData.add(dropdata.getDatavalue());
            }


            for (RequestDropdownData dropdata : serverResponseRequest.getSocRegNo()) {
                socRegNoData.add(dropdata.getDatavalue());
            }

            for (RequestDropdownData dropdata : serverResponseRequest.getUnitDetails()) {
                unitDetailsData.add(dropdata.getDatavalue());
            }


            if (getProjectData != null && getProjectData.size() > 0) {
                displayProjectDetail(getProjectData);
            }


        }

    }

    private void displayContructionProgressUpdate() {
        displayworkcompleUpdate();
        if (((CheckBox) view.findViewById(R.id.xcheckBoxUnderConstration)).isChecked()) {
            AppSetting.setKeyCheckedValue("Under Construction");
            String result = AppSetting.getKeyCheckedValue();

            noOfSlabsId.setVisibility(View.VISIBLE);
            projectCompletionId.setVisibility(View.VISIBLE);
            internalBrickworkId.setVisibility(View.VISIBLE);
            internalPaintingId.setVisibility(View.VISIBLE);
            noOfFloorsId.setVisibility(View.VISIBLE);
            internalPlasterId.setVisibility(View.VISIBLE);
            projectStartId.setVisibility(View.VISIBLE);
            xcheckBoxAllworkcompleted.setVisibility(View.VISIBLE);
            flooringCompleteId.setVisibility(View.VISIBLE);
            plinthCompleteId.setVisibility(View.VISIBLE);
            externalBrickworkId.setVisibility(View.VISIBLE);
            externalPaintingId.setVisibility(View.VISIBLE);
            externalPlasterId.setVisibility(View.VISIBLE);
            rccId.setVisibility(View.VISIBLE);
            woodworkPlumbingId.setVisibility(View.VISIBLE);
            otherId.setVisibility(View.VISIBLE);
            yearsOfConstructionId.setVisibility(View.GONE);
            ageOfBuildingId.setVisibility(View.GONE);

            ((EditText) view.findViewById(R.id.xedtyearofconstruction)).setFocusable(true);
            ((EditText) view.findViewById(R.id.xedtageofbuilding)).setFocusable(true);
            ((EditText) view.findViewById(R.id.xedtsocietyname)).setFocusable(true);
            if (!((EditText) view.findViewById(R.id.xedtprojectstartdate)).isFocusable())
                ((EditText) view.findViewById(R.id.xedtprojectstartdate)).setFocusable(true);
            ((EditText) view.findViewById(R.id.xedtprojectcompletiondate)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautototalnooffloors)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNoofslabs)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautosocietyno)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).setFocusable(false);
            ((EditText) view.findViewById(R.id.xedtyearofconstruction)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((EditText) view.findViewById(R.id.xedtageofbuilding)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautosocietyno)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).setBackgroundColor(getResources().getColor(R.color.gray));

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautototalnooffloors)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNoofslabs)).setBackgroundColor(getResources().getColor(R.color.white));
            ((EditText) view.findViewById(R.id.xedtprojectstartdate)).setBackgroundResource(R.drawable.edittextstyle2);
            ((EditText) view.findViewById(R.id.xedtprojectcompletiondate)).setBackgroundResource(R.drawable.edittextstyle2);


            view.findViewById(R.id.ximgdropdownsharedistinctiveno).setClickable(false);
            view.findViewById(R.id.ximgdropdownsocietyno).setClickable(true);
            view.findViewById(R.id.ximgdropdownShareCertificationno).setClickable(false);
            view.findViewById(R.id.ximgdropdownNoofslabs).setClickable(true);
            view.findViewById(R.id.ximgdropdownautototalnooffloors).setClickable(true);

        } else {
            AppSetting.setKeyCheckedValue("Developed");
            //if (((CheckBox) view.findViewById(R.id.xcheckBoxDeveloped)).isChecked()) {
            String result = AppSetting.getKeyCheckedValue();

                noOfSlabsId.setVisibility(View.GONE);
                projectCompletionId.setVisibility(View.GONE);
                internalBrickworkId.setVisibility(View.GONE);
                internalPaintingId.setVisibility(View.GONE);
                noOfFloorsId.setVisibility(View.GONE);
                internalPlasterId.setVisibility(View.GONE);
                projectStartId.setVisibility(View.GONE);
                xcheckBoxAllworkcompleted.setVisibility(View.GONE);
                flooringCompleteId.setVisibility(View.GONE);
                plinthCompleteId.setVisibility(View.GONE);
                externalBrickworkId.setVisibility(View.GONE);
                externalPaintingId.setVisibility(View.GONE);
                externalPlasterId.setVisibility(View.GONE);
                rccId.setVisibility(View.GONE);
                woodworkPlumbingId.setVisibility(View.GONE);
                otherId.setVisibility(View.GONE);
                yearsOfConstructionId.setVisibility(View.VISIBLE);
                ageOfBuildingId.setVisibility(View.VISIBLE);

                ((EditText) view.findViewById(R.id.xedtyearofconstruction)).setFocusable(true);
                edtConstrationYear.setFocusable(true);
                edtAgeofBuilding.setFocusable(true);
                ((EditText) view.findViewById(R.id.xedtageofbuilding)).setFocusable(true);
                ((EditText) view.findViewById(R.id.xedtsocietyname)).setFocusable(true);
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).setFocusable(true);
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautosocietyno)).setFocusable(true);
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).setFocusable(true);
                ((EditText) view.findViewById(R.id.xedtprojectstartdate)).setFocusable(false);
                ((EditText) view.findViewById(R.id.xedtprojectcompletiondate)).setFocusable(false);
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautototalnooffloors)).setFocusable(false);
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNoofslabs)).setFocusable(false);
                ((EditText) view.findViewById(R.id.xedtyearofconstruction)).setBackgroundResource(R.drawable.edittextstyle2);
                ((EditText) view.findViewById(R.id.xedtageofbuilding)).setBackgroundResource(R.drawable.edittextstyle2);
                ((EditText) view.findViewById(R.id.xedtsocietyname)).setBackgroundResource(R.drawable.edittextstyle2);
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).setBackgroundColor(getResources().getColor(R.color.gray));
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautosocietyno)).setBackgroundColor(getResources().getColor(R.color.gray));
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).setBackgroundColor(getResources().getColor(R.color.gray));

                ((AutoCompleteTextView) view.findViewById(R.id.xedtautototalnooffloors)).setBackgroundColor(getResources().getColor(R.color.gray));
                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNoofslabs)).setBackgroundColor(getResources().getColor(R.color.gray));
                ((EditText) view.findViewById(R.id.xedtprojectstartdate)).setBackgroundColor(getResources().getColor(R.color.gray));
                ((EditText) view.findViewById(R.id.xedtprojectcompletiondate)).setBackgroundColor(getResources().getColor(R.color.gray));

                view.findViewById(R.id.ximgdropdownsharedistinctiveno).setClickable(false);
                view.findViewById(R.id.ximgdropdownsocietyno).setClickable(false);
                view.findViewById(R.id.ximgdropdownShareCertificationno).setClickable(false);
                view.findViewById(R.id.ximgdropdownNoofslabs).setClickable(false);
                view.findViewById(R.id.ximgdropdownautototalnooffloors).setClickable(false);
            //}
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ximgdropdownbuildername:

                if (buildingNameData != null && buildingNameData.size() > 0)
                    lstbuildingName.setAdapter(new LocationDetailAdapter(
                            getActivity(), buildingNameData));
                lstbuildingName.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildername)));
                lstbuildingName.setModal(true);
                lstbuildingName.show();

                break;


            case R.id.ximgdropdownarchitech:

                if (architectData != null && architectData.size() > 0)
                    lstArchitect.setAdapter(new LocationDetailAdapter(
                            getActivity(), architectData));
                lstArchitect.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoarchitech)));
                lstArchitect.setModal(true);
                lstArchitect.show();

                break;


            case R.id.ximgdropdownplanno:

                if (planNoData != null && planNoData.size() > 0)
                    lstPlanNo.setAdapter(new LocationDetailAdapter(
                            getActivity(), planNoData));
                lstPlanNo.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoplanno)));
                lstPlanNo.setModal(true);
                lstPlanNo.show();

                break;


            case R.id.ximgdropdownShareCertificationno:

                if (shareNoData != null && shareNoData.size() > 0)
                    lstShareNo.setAdapter(new LocationDetailAdapter(
                            getActivity(), shareNoData));
                lstShareNo.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)));
                lstShareNo.setModal(true);
                lstShareNo.show();

                break;


            case R.id.ximgdropdownccno:

                if (cCNoData != null && cCNoData.size() > 0)
                    lstCCNo.setAdapter(new LocationDetailAdapter(
                            getActivity(), cCNoData));
                lstCCNo.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoccno)));
                lstCCNo.setModal(true);
                lstCCNo.show();

                break;

            case R.id.ximgdropdownsharedistinctiveno:

                if (shareDNoData != null && shareDNoData.size() > 0)
                    lstShareDNo.setAdapter(new LocationDetailAdapter(
                            getActivity(), shareDNoData));
                lstShareDNo.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)));
                lstShareDNo.setModal(true);
                lstShareDNo.show();

                break;


            case R.id.ximgdropdownsocietyno:

                if (socRegNoData != null && socRegNoData.size() > 0)
                    lstsocRegNo.setAdapter(new LocationDetailAdapter(
                            getActivity(), socRegNoData));
                lstsocRegNo.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautosocietyno)));
                lstsocRegNo.setModal(true);
                lstsocRegNo.show();

                break;

            case R.id.ximgdropdownautoUnitdetails:

                if (unitDetailsData != null && unitDetailsData.size() > 0)
                    lstUnitDetails.setAdapter(new LocationDetailAdapter(
                            getActivity(), unitDetailsData));
                lstUnitDetails.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoUnitdetails)));
                lstUnitDetails.setModal(true);
                lstUnitDetails.show();

                break;


            case R.id.ximgdropdownfloor:

                if (noofFloor != null && noofFloor.size() > 0)
                    lstnoofFloor.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofFloor));
                lstnoofFloor.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)));
                lstnoofFloor.setModal(true);
                lstnoofFloor.show();

                break;


            case R.id.ximgdropdownexternalpaintingcompleted:

                if (noofFloor != null && noofFloor.size() > 0)
                    lstexternalpaintingcompleted.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofFloor));
                lstexternalpaintingcompleted.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalpaintingcompleted)));
                lstexternalpaintingcompleted.setModal(true);
                lstexternalpaintingcompleted.show();

                break;

            case R.id.ximgdropdownautointernalpaintingcompleted:

                if (noofFloor != null && noofFloor.size() > 0)
                    lstInternalpaintingcompleted.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofFloor));
                lstInternalpaintingcompleted.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalpaintingcompleted)));
                lstInternalpaintingcompleted.setModal(true);
                lstInternalpaintingcompleted.show();

                break;


            case R.id.ximgdropdownautoexternalbrickworkcompletedfrom:

                if (noofFloor != null && noofFloor.size() > 0)
                    lstExternalbrickworkcompletedfrom.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofFloor));
                lstExternalbrickworkcompletedfrom.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalbrickworkcompletedfrom)));
                lstExternalbrickworkcompletedfrom.setModal(true);
                lstExternalbrickworkcompletedfrom.show();

                break;


            case R.id.ximgdropdowninternalbrickworkcompleted:

                if (noofFloor != null && noofFloor.size() > 0)
                    lstInternalbrickworkcompletedfrom.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofFloor));
                lstInternalbrickworkcompletedfrom.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalbrickworkcompleted)));
                lstInternalbrickworkcompletedfrom.setModal(true);
                lstInternalbrickworkcompletedfrom.show();

                break;


            case R.id.ximgdropdownautorcccompleted:

                if (totalRccfloor != null && totalRccfloor.size() > 0)
                    lstRcc.setAdapter(new LocationDetailAdapter(
                            getActivity(), totalRccfloor));
                lstRcc.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautorcccompleted)));
                lstRcc.setModal(true);
                lstRcc.show();

                break;


            case R.id.ximgdropdownautoexternalplastercompleted:

                if (noofFloor != null && noofFloor.size() > 0)
                    lstExternalplastercompleted.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofFloor));
                lstExternalplastercompleted.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalplastercompleted)));
                lstExternalplastercompleted.setModal(true);
                lstExternalplastercompleted.show();

                break;


            case R.id.ximgdropdowninternalplastercompleted:

                if (noofFloor != null && noofFloor.size() > 0)
                    lstInternalplastercompleted.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofFloor));
                lstInternalplastercompleted.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalplastercompleted)));
                lstInternalplastercompleted.setModal(true);
                lstInternalplastercompleted.show();

                break;


            case R.id.ximgdropdownautowoodworker:

                if (noofFloor != null && noofFloor.size() > 0)
                    lstwoodworker.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofFloor));
                lstwoodworker.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautowoodworker)));
                lstwoodworker.setModal(true);
                lstwoodworker.show();

                break;

            case R.id.ximgdropdownflooringcompleted:

                if (noofFloor != null && noofFloor.size() > 0)
                    lstflooringcompleted.setAdapter(new LocationDetailAdapter(
                            getActivity(), noofFloor));
                lstflooringcompleted.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoflooringcompleted)));
                lstflooringcompleted.setModal(true);
                lstflooringcompleted.show();

                break;

            case R.id.ximgdropdownautototalnooffloors:

                if (totalnoofSlab != null && totalnoofSlab.size() > 0)
                    lstTotalnoofFloor.setAdapter(new LocationDetailAdapter(
                            getActivity(), totalnoofSlab));
                lstTotalnoofFloor.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautototalnooffloors)));
                lstTotalnoofFloor.setModal(true);
                lstTotalnoofFloor.show();

                break;


            case R.id.ximgdropdownNoofslabs:

                if (totalnoofSlab != null && totalnoofSlab.size() > 0)
                    lstslab.setAdapter(new LocationDetailAdapter(
                            getActivity(), totalnoofSlab));
                lstslab.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoNoofslabs)));
                lstslab.setModal(true);
                lstslab.show();

                break;

            case R.id.ximgdropdownautoplinthcomplited:

                if (plinthData != null && plinthData.size() > 0)
                    lstPlinth.setAdapter(new LocationDetailAdapter(
                            getActivity(), plinthData));
                lstPlinth.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoplinthcomplited)));
                lstPlinth.setModal(true);
                lstPlinth.show();

                break;

            case R.id.ximgdropdownautoliftinstalled:

                if (liftInstalledData != null && liftInstalledData.size() > 0)
                    lstLiftInstalled.setAdapter(new LocationDetailAdapter(
                            getActivity(), liftInstalledData));
                lstLiftInstalled.setAnchorView(((AutoCompleteTextView) view.findViewById(R.id.xedtautoliftinstalled)));
                lstLiftInstalled.setModal(true);
                lstLiftInstalled.show();

                break;


            case R.id.xcheckBoxAllworkcompleted:

                //((CheckBox)view.findViewById(R.id.xcheckBoxAllworkcompleted)).setChecked(true);
                displayworkcompleUpdate();
                break;

            case R.id.xcheckBoxDeveloped:
                ((CheckBox) view.findViewById(R.id.xcheckBoxDeveloped)).setChecked(true);
                ((CheckBox) view.findViewById(R.id.xcheckBoxUnderConstration)).setChecked(false);
                ((CheckBox) view.findViewById(R.id.xcheckBoxAllworkcompleted)).setChecked(true);
                displayContructionProgressUpdate();

                break;

            case R.id.xcheckBoxUnderConstration:
                ((CheckBox) view.findViewById(R.id.xcheckBoxDeveloped)).setChecked(false);
                ((CheckBox) view.findViewById(R.id.xcheckBoxUnderConstration)).setChecked(true);
                ((CheckBox) view.findViewById(R.id.xcheckBoxAllworkcompleted)).setChecked(false);
                displayContructionProgressUpdate();
                break;

            case R.id.xbtnSave:

                if (validateForm()) {
                    if (GetWebServiceData.isNetworkAvailable(getActivity())) {

                        new SetProjectAsyncTask().execute();

                    } else {
                        String construction = "";
                        String allworkCompleted = "";
                        //List<NameValuePair> nameValuePairs = null;
                        List<GetProjectDetailsResponse> projectTab = null;
                        try {
                            //nameValuePairs = new ArrayList<NameValuePair>(1);
                            projectTab = new ArrayList<GetProjectDetailsResponse>();
                        } catch (Exception e) {
                        }
                        //nameValuePairs=saveSetdataoffline(nameValuePairs);

                        if (((CheckBox) view.findViewById(R.id.xcheckBoxUnderConstration)).isChecked()) {
                            construction = "N";
                        } else {
                            if (((CheckBox) view.findViewById(R.id.xcheckBoxDeveloped)).isChecked()) {
                                construction = "Y";
                            }
                        }
                        if (((CheckBox) view.findViewById(R.id.xcheckBoxAllworkcompleted)).isChecked()) {
                            allworkCompleted = "Y";
                        } else {
                            allworkCompleted = "N";
                        }

                        projectTab.add(new GetProjectDetailsResponse(AppSetting.getRequestId(), construction, ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildername)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoarchitech)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplanno)).getText().toString(),
                                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoccno)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautosocietyno)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).getText().toString(), ((EditText) view.findViewById(R.id.xedtsocietyname)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoUnitdetails)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautototalnooffloors)).getText().toString(),
                                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNoofslabs)).getText().toString(), ((EditText) view.findViewById(R.id.xedtprojectstartdate)).getText().toString(), ((EditText) view.findViewById(R.id.xedtprojectcompletiondate)).getText().toString(), allworkCompleted, ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplinthcomplited)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautorcccompleted)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalbrickworkcompleted)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalbrickworkcompletedfrom)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalplastercompleted)).getText().toString(),
                                ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalplastercompleted)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautowoodworker)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOther)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoliftinstalled)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoflooringcompleted)).getText().toString(), edtConstrationYear.getText().toString(), edtAgeofBuilding.getText().toString(), ((EditText) view.findViewById(R.id.xedtcarpetarea)).getText().toString(), ((EditText) view.findViewById(R.id.xedtMarketraterange)).getText().toString(),
                                ((EditText) view.findViewById(R.id.xedtflooerise)).getText().toString(), ((EditText) view.findViewById(R.id.xedtothercharges)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalpaintingcompleted)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalpaintingcompleted)).getText().toString(), ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).getText().toString()));

                        if (projectTab != null && projectTab.size() > 0) {
                            displayOfflineSaveDataDialog(projectTab);
                        }

                        /*AppCommonDialog.showSimpleDialog(getActivity(),
                                getResources().getString(R.string.app_name),
                                getResources().getString(R.string.check_network),
                                getResources().getString(R.string.ok), "OK");*/
                    }
                }

                break;

            default:
                break;
        }

    }


    private void displayOfflineSaveDataDialog(final List<GetProjectDetailsResponse> projectTab) {
       /* final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dailog_box);
        TextView textTitle = (TextView) dialog.findViewById(R.id.xtxttitle);
        textTitle.setText("");
        // set the custom dialog components - text, image and button
        String strmessage = getResources().getString(R.string.savelocal_datamessage);
        ((TextView) dialog.findViewById(R.id.xtxtmessage)).setText(strmessage);


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
                ProjectTabResponse data = new ProjectTabResponse();
                data.setRemarks("yes");
                data.setStatus("sucessfully");
                data.setValues(projectTab);
                strRequest = gson.toJson(data);
                boolean bupdate = SecondScreenActivity.mypcplData.updateProjectdata(AppSetting.getRequestId(), strRequest, strRequest);
                if (bupdate) {
                    Toast.makeText(getActivity(),
                            "sucessfully store data  ",
                            Toast.LENGTH_SHORT).show();
                    updateandDisplayofflineData("");
                    AppSetting.setUpdateOfflinedata(true);
                }
                dialog.dismiss();
            }

        });
        dialog.show();*/

        Gson gson = new Gson();
        String strRequest = "";
        ProjectTabResponse data = new ProjectTabResponse();
        data.setRemarks("yes");
        data.setStatus("successfully");
        data.setValues(projectTab);
        strRequest = gson.toJson(data);
        boolean bupdate = SecondScreenActivity.mypcplData.updateProjectdata(AppSetting.getRequestId(), strRequest, strRequest);
        if (bupdate) {
            Toast.makeText(getActivity(), "successfully Update offline data ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData(AppSetting.getRequestId());
            AppSetting.setUpdateOfflinedata(true);
        } else if (!bupdate) {
            SecondScreenActivity.mypcplData.insertProjectdata(strRequest, AppSetting.getRequestId());
            Toast.makeText(getActivity(), "successfully store data offline  ", Toast.LENGTH_SHORT).show();
            updateandDisplayofflineData(AppSetting.getRequestId());
        }

    }

    private List<NameValuePair> saveSetdataoffline(
            List<NameValuePair> nameValuePairs) {
        if (AppSetting.getRequestId() != null) {

            nameValuePairs.add(new BasicNameValuePair("RequestId", AppSetting.getRequestId()));
            if (((CheckBox) view.findViewById(R.id.xcheckBoxUnderConstration)).isChecked()) {
                nameValuePairs.add(new BasicNameValuePair("Construction", "N"));
            } else {
                if (((CheckBox) view.findViewById(R.id.xcheckBoxDeveloped)).isChecked()) {
                    nameValuePairs.add(new BasicNameValuePair("Construction", "Y"));
                }
            }

            nameValuePairs.add(new BasicNameValuePair("Builder_Name", ((AutoCompleteTextView) view.findViewById(R.id.xedtautobuildername)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("ArchitectName", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoarchitech)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("PlanNo", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplanno)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("CCNo", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoccno)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("SocietyRegNO", ((AutoCompleteTextView) view.findViewById(R.id.xedtautosocietyno)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("ShareCertificationNO", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoShareCertificationno)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("SocietyName", ((EditText) view.findViewById(R.id.xedtsocietyname)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("ShareDistinctiveNo", ((AutoCompleteTextView) view.findViewById(R.id.xedtautosharedistinctiveno)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("UnitDetail", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoUnitdetails)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("TotalNoOfFloors", ((AutoCompleteTextView) view.findViewById(R.id.xedtautototalnooffloors)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("NoOfSlabs", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoNoofslabs)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("ProjectStartDate", ((EditText) view.findViewById(R.id.xedtprojectstartdate)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("ProjectCompletionDate", ((EditText) view.findViewById(R.id.xedtprojectcompletiondate)).getText().toString()));


            if (((CheckBox) view.findViewById(R.id.xcheckBoxAllworkcompleted)).isChecked()) {
                nameValuePairs.add(new BasicNameValuePair("AllWorkCompleted", "Y"));
            } else {
                nameValuePairs.add(new BasicNameValuePair("AllWorkCompleted", "N"));
            }

            nameValuePairs.add(new BasicNameValuePair("PLinth", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplinthcomplited)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("RCCCompleted", ((AutoCompleteTextView) view.findViewById(R.id.xedtautorcccompleted)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("InternalBrickWork", ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalbrickworkcompleted)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("EcternalBrickWork", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalbrickworkcompletedfrom)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("InternalPlaster", ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalplastercompleted)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("ExternalPlaster", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalplastercompleted)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("WoodWork", ((AutoCompleteTextView) view.findViewById(R.id.xedtautowoodworker)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Other", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOther)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("LiftInstalled", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoliftinstalled)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("FloorNo", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoflooringcompleted)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("YearOfConstruction", edtConstrationYear.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("AgeOfBuilding", edtAgeofBuilding.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("CarpetArea", ((EditText) view.findViewById(R.id.xedtcarpetarea)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("MarketRate", ((EditText) view.findViewById(R.id.xedtMarketraterange)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("FloorRise", ((EditText) view.findViewById(R.id.xedtflooerise)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("OtherCharge", ((EditText) view.findViewById(R.id.xedtothercharges)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Flooring", ((AutoCompleteTextView) view.findViewById(R.id.xedtautofloor)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("InternalPainting", ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalpaintingcompleted)).getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("ExternalPainting", ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalpaintingcompleted)).getText().toString()));
        }
        return nameValuePairs;
    }

    private void displayworkcompleUpdate() {
        if (((CheckBox) view.findViewById(R.id.xcheckBoxAllworkcompleted)).isChecked()) {
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalbrickworkcompleted)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalpaintingcompleted)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalplastercompleted)).setFocusable(false);
            //((AutoCompleteTextView)view.findViewById(R.id.xedtautoElectrification)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplinthcomplited)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautorcccompleted)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOther)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalbrickworkcompletedfrom)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalpaintingcompleted)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautowoodworker)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalplastercompleted)).setFocusable(false);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoflooringcompleted)).setFocusable(false);

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalbrickworkcompleted)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalpaintingcompleted)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalplastercompleted)).setBackgroundColor(getResources().getColor(R.color.gray));
            //((AutoCompleteTextView)view.findViewById(R.id.xedtautoElectrification)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplinthcomplited)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautorcccompleted)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOther)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalbrickworkcompletedfrom)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalpaintingcompleted)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautowoodworker)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalplastercompleted)).setBackgroundColor(getResources().getColor(R.color.gray));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoflooringcompleted)).setBackgroundColor(getResources().getColor(R.color.gray));

            view.findViewById(R.id.ximgdropdownexternalpaintingcompleted).setClickable(false);
            view.findViewById(R.id.ximgdropdowninternalbrickworkcompleted).setClickable(false);
            view.findViewById(R.id.ximgdropdownautointernalpaintingcompleted).setClickable(false);
            view.findViewById(R.id.ximgdropdownautoexternalbrickworkcompletedfrom).setClickable(false);
            view.findViewById(R.id.ximgdropdownautoexternalplastercompleted).setClickable(false);
            view.findViewById(R.id.ximgdropdowninternalplastercompleted).setClickable(false);
            view.findViewById(R.id.ximgdropdownautowoodworker).setClickable(false);
            view.findViewById(R.id.ximgdropdownflooringcompleted).setClickable(false);
            view.findViewById(R.id.ximgdropdownautorcccompleted).setClickable(false);
            //view.findViewById(R.id. ximgdropdownElectrification).setClickable(false);
            view.findViewById(R.id.ximgdropdownautoplinthcomplited).setClickable(false);
        } else {

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalbrickworkcompleted)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalpaintingcompleted)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalplastercompleted)).setFocusable(true);
            //((AutoCompleteTextView)view.findViewById(R.id.xedtautoElectrification)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplinthcomplited)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautorcccompleted)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOther)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalbrickworkcompletedfrom)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalpaintingcompleted)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautowoodworker)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalplastercompleted)).setFocusable(true);
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoflooringcompleted)).setFocusable(true);

            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalbrickworkcompleted)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalpaintingcompleted)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautointernalplastercompleted)).setBackgroundColor(getResources().getColor(R.color.white));
            //((AutoCompleteTextView)view.findViewById(R.id.xedtautoElectrification)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoplinthcomplited)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautorcccompleted)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoOther)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalbrickworkcompletedfrom)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalpaintingcompleted)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautowoodworker)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoexternalplastercompleted)).setBackgroundColor(getResources().getColor(R.color.white));
            ((AutoCompleteTextView) view.findViewById(R.id.xedtautoflooringcompleted)).setBackgroundColor(getResources().getColor(R.color.white));

            view.findViewById(R.id.ximgdropdownexternalpaintingcompleted).setClickable(true);
            view.findViewById(R.id.ximgdropdowninternalbrickworkcompleted).setClickable(true);
            view.findViewById(R.id.ximgdropdownautointernalpaintingcompleted).setClickable(true);
            view.findViewById(R.id.ximgdropdownautoexternalbrickworkcompletedfrom).setClickable(true);
            view.findViewById(R.id.ximgdropdownautoexternalplastercompleted).setClickable(true);
            view.findViewById(R.id.ximgdropdowninternalplastercompleted).setClickable(true);
            view.findViewById(R.id.ximgdropdownautowoodworker).setClickable(true);
            view.findViewById(R.id.ximgdropdownflooringcompleted).setClickable(true);
            view.findViewById(R.id.ximgdropdownautorcccompleted).setClickable(true);
            //view.findViewById(R.id. ximgdropdownElectrification).setClickable(true);
            view.findViewById(R.id.ximgdropdownautoplinthcomplited).setClickable(true);


        }
    }

    private boolean validateForm() {
        // TODO Auto-generated method stub
        boolean valid = true;

        //	if (((EditText) view.findViewById(R.id.xedtautobu)).getText()
        //	.toString().length() <= 0) {
        // AppCommonDialog.setErrorMsg("Please Enter Name",
        // ((EditText)view.findViewById(R.id.xedtEnginnerName)), true);
        //	((EditText) view.findViewById(R.id.xedtautobuildername))
        //			.setError("Please Enter Builder Name");
        //	valid = false;
        //	} else
	/*	if (((EditText) view.findViewById(R.id.xedtautoarchitech))
				.getText().toString().trim().length() <= 0) {
			// AppCommonDialog.setErrorMsg("Please Enter Customer Name",
			// ((EditText)view.findViewById(R.id.xedtCustomerName)), true);
			((EditText) view.findViewById(R.id.xedtautoarchitech))
					.setError("Please Enter Architech Name");
			valid = false;

		}
*/
        if (((CheckBox) view.findViewById(R.id.xcheckBoxUnderConstration)).isChecked()) {

            if (((EditText) view.findViewById(R.id.xedtautoUnitdetails)).getText()
                    .toString().length() <= 0) {
                AppCommonDialog.setErrorMsg("Please Enter Unit Details",
                        ((EditText) view.findViewById(R.id.xedtautoUnitdetails)), true);
                valid = false;
            } else if (((TextView) view.findViewById(R.id.xedtautototalnooffloors)).getText()
                    .toString().trim().length() <= 0) {
                ((TextView) view.findViewById(R.id.xedtautototalnooffloors))
                        .setError("Please Enter Total No. Of Floors");
                valid = false;

            } else if (((EditText) view.findViewById(R.id.xedtautoNoofslabs)).getText()
                    .toString().trim().length() <= 0) {
                ((EditText) view.findViewById(R.id.xedtautoNoofslabs))
                        .setError("Please Enter No. Of Slabs");
                valid = false;

            }


        } else if (((CheckBox) view.findViewById(R.id.xcheckBoxDeveloped)).isChecked()) {

            if (((EditText) view.findViewById(R.id.xedtautofloor)).getText()
                    .toString().trim().length() <= 0) {
                ((EditText) view.findViewById(R.id.xedtautofloor))
                        .setError("Please Enter Floor No.");
                valid = false;

            } else if (((EditText) view.findViewById(R.id.xedtyearofconstruction)).getText()
                    .toString().trim().length() <= 0) {
                ((EditText) view.findViewById(R.id.xedtyearofconstruction))
                        .setError("Please Enter Year Of Construction");
                valid = false;

            } else if (((EditText) view.findViewById(R.id.xedtageofbuilding)).getText()
                    .toString().trim().length() <= 0) {
                ((EditText) view.findViewById(R.id.xedtageofbuilding))
                        .setError("Please Enter Age Of Building");
                valid = false;

            }

		/*	else if (((EditText) view.findViewById(R.id.xedtautointernalpaintingcompleted)).getText()
					.toString().trim().length() <= 0) {
				((EditText) view.findViewById(R.id.xedtautointernalpaintingcompleted))
						.setError("Please Enter Internal Painting");
				valid = false;

			}

			else if (((EditText) view.findViewById(R.id.xedtautoexternalpaintingcompleted)).getText()
					.toString().trim().length() <= 0) {
				((EditText) view.findViewById(R.id.xedtautoexternalpaintingcompleted))
						.setError("Please Enter External Painting");
				valid = false;

			}


			else if (((EditText) view.findViewById(R.id.xedtautoplinthcomplited)).getText()
					.toString().trim().length() <= 0) {
				((EditText) view.findViewById(R.id.xedtautoplinthcomplited))
						.setError("Please Enter Plinth Completed");
				valid = false;

			}



			else if (((EditText) view.findViewById(R.id.xedtautoflooringcompleted)).getText()
					.toString().trim().length() <= 0) {
				((EditText) view.findViewById(R.id.xedtautoflooringcompleted))
						.setError("Please Enter Flooring Completed");
				valid = false;

			}

			*/

        }


        return (valid);

    }


    // Requestdetail Task
    public class SetProjectAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                loadingDialog = new ProgressDialog(getActivity());
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
                nameValuePairs = new ArrayList<NameValuePair>(22);
            } catch (Exception e) {

            }
            nameValuePairs = saveSetdataoffline(nameValuePairs);


            //Log.d("request", +);
            return GetWebServiceData.getServerResponse(url
                    + "/SetProjectTabDetails", nameValuePairs, data);

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

                    CommonResponse serverResponseRequest = new Gson()
                            .fromJson(result, CommonResponse.class);

                    String loginResult = serverResponsere.getString("status");
                    if (loginResult.equals("OK")) {
                        Log.d("request detail", "get data");
                        Toast.makeText(getActivity(), "Successfully " + loginResult, Toast.LENGTH_SHORT).show();

                       //create intent and check if Under Construction is selected or not

							/*if (serverResponseRequest != null&& serverResponseRequest.getStatus()!=null) {
								AppCommonDialog.showSimpleDialog(getActivity(),
										getResources().getString(R.string.app_name),"Data Update Sucessfully",
										getResources().getString(R.string.ok), "OK");
							}*/
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
