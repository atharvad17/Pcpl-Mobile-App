package com.techinvest.pcpl.adapter;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CompletevisitsAdapter extends FragmentPagerAdapter
{
	CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
 

	public CompletevisitsAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb)
	{
		super(fm);
		 
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
	}

	@Override
	public Fragment getItem(int i)
   {
		switch (i) {
        case 0:
            
        //    return new ClientActivity();
        case 1:
          
          //  return new CustomerActivity();
        case 2:
          
           //  return new ContactActivity();
        case 3:
          
            // return new AreaActivity();
       
        }
		return null;
	}

	 // This method return the titles for the Tabs in the Tab Strip
	 
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
 
    // This method return the Number of tabs for the tabs Strip
 
    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}