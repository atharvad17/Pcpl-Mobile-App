package com.techinvest.pcplrealestate;



import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
/*import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;*/
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.techinvest.pcpl.adapter.ServicesAdapter;
import com.techinvest.pcpl.tabs.SlidingTabLayout;

@SuppressLint("NewApi")
public class Services extends FragmentActivity
{
	//Toolbar toolbar;
	ViewPager pager;
	ServicesAdapter adapter;
	SlidingTabLayout tabs;
	

	CharSequence Titles[] = { "REQUEST DETAILS", "LOCATION DETAILS",
			"PROJECT DETAILS", "BUILDING DETAILS", "FLAT SPECIFICATIONS",
			"ROOM DETAILS", "LAYOUT DETAILS"};
	
	int Numboftabs = 7;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.services);
		((TextView)findViewById(R.id.xtxtheaderTittle)).setText(getResources().getString(R.string.title_activity_services));
         ((ImageButton)findViewById(R.id.backId)).setOnClickListener(new OnClickListener()
		 {
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
        
		
		//toolbar = (Toolbar) findViewById(R.id.tool_bar);
		//setSupportActionBar(toolbar);

		// Creating The ViewPagerAdapter and Passing Fragment Manager, Titles
		// fot the Tabs and Number Of Tabs.
		adapter = new ServicesAdapter(getSupportFragmentManager(), Titles,Numboftabs);

		// Assigning ViewPager View and setting the adapter
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		// Assiging the Sliding Tab Layout View
		tabs = (SlidingTabLayout) findViewById(R.id.tabs);
		tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
										// This makes the tabs Space Evenly in
										// Available width

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});
		// Setting the ViewPager For the SlidingTabsLayout
		tabs.setViewPager(pager);

	}
}
