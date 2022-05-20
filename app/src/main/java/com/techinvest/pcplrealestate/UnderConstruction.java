package com.techinvest.pcplrealestate;


import android.app.Activity;
import android.os.Bundle;
//import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import androidx.core.app.NavUtils;

public class UnderConstruction extends Activity {

	//Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.under_construction);
		
		/*toolbar = (Toolbar) findViewById(R.id.tool_barundercon);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setTitle("Under Construction Details");
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		actionBar.show();*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.under_construction, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			NavUtils.navigateUpFromSameTask(this);
			return true;
		
		}
		return super.onOptionsItemSelected(item);
	}
}
