package com.techinvest.pcpl.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.techinvest.pcplrealestate.Layoutdetails;
import com.techinvest.pcplrealestate.Requestdetails;
import com.techinvest.pcplrealestate.Roomdetails;
import com.techinvest.pcplrealestate.fragment.Buildingdetails;
import com.techinvest.pcplrealestate.fragment.Flatspecifications;
import com.techinvest.pcplrealestate.fragment.Locationdetails;
import com.techinvest.pcplrealestate.fragment.PendingvisitFragment;
import com.techinvest.pcplrealestate.fragment.Projectdetails;

public class ServicesAdapter extends FragmentStatePagerAdapter {
	Requestdetails requestDetail;
	Locationdetails locationDetail;
	Projectdetails projectdetails;
	Buildingdetails buildingdetails;
	Flatspecifications flatspecifications;
	Roomdetails roomDetails;
	Layoutdetails layoutDetails;

	// Build a Constructor and assign the passed Values to appropriate values in
	// the class
	public ServicesAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb)
	{
		super(fm);

		this.Titles = mTitles;
		this.NumbOfTabs = mNumbOfTabsumb;
		requestDetail = new Requestdetails();
		locationDetail = new Locationdetails();
		projectdetails = new Projectdetails();
		buildingdetails = new Buildingdetails();
		flatspecifications = new Flatspecifications();
		roomDetails = new Roomdetails();
		layoutDetails = new Layoutdetails();

	}

	CharSequence Titles[]; // This will Store the Titles of the Tabs which are
	// Going to be passed when ViewPagerAdapter is
	// created
	int NumbOfTabs; // Store the number of tabs, this will also be passed when
	// the ViewPagerAdapter is created

	// This method return the fragment for the every position in the View Pager
	@Override
	public Fragment getItem(int i) {
		switch (i)
		{
			case 0:
				return requestDetail;
			case 1:
				return locationDetail;
			case 2:
				return projectdetails;
			case 3:
				return buildingdetails;
			case 4:
				return flatspecifications;
			case 5:
				return roomDetails;
			case 6:
				return layoutDetails;
		}
		return null;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return Titles[position];
	}

	@Override
	public int getCount() {
		return NumbOfTabs;
	}
}
