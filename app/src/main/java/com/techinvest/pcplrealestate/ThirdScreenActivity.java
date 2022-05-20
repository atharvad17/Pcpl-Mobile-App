package com.techinvest.pcplrealestate;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.techinvest.pcpl.model.PendingVisit;

public class ThirdScreenActivity extends Activity {
	// Toolbar toolbar;
	Button proceed;
	int position;
	private ArrayList<PendingVisit> clientPendingdata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_popupdialog);
		Bundle bundaledata = getIntent().getExtras();
		if (bundaledata != null) {
			clientPendingdata = (ArrayList<PendingVisit>) bundaledata
					.getSerializable("CUSTOMER");
			position = bundaledata.getInt("POS");

		}

		if (clientPendingdata != null && clientPendingdata.size() > 0) {
			((EditText) findViewById(R.id.editText1)).setText(clientPendingdata
					.get(position).getClientName());
			((EditText) findViewById(R.id.editText2)).setText(clientPendingdata
					.get(position).getApplicantPhoneNo());
			((EditText) findViewById(R.id.editText3)).setText(clientPendingdata
					.get(position).getBuildingName());
		}

		// toolbar = (Toolbar) findViewById(R.id.tool_bar);
		// setSupportActionBar(toolbar);

		proceed = (Button) findViewById(R.id.button1);
		proceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ThirdScreenActivity.this,
						Services.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.third_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
