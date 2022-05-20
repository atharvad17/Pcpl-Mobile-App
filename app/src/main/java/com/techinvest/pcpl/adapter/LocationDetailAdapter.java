package com.techinvest.pcpl.adapter;

import java.util.List;

import com.techinvest.pcpl.adapter.RequestDetailAdapter.ViewHolder;
import com.techinvest.pcplrealestate.R;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
//import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class LocationDetailAdapter extends BaseAdapter {

	Activity activity;
	List<String> requestdropdownData;
	LayoutInflater inflater;

	public LocationDetailAdapter(Activity activity2, List<String> areaData) {

		this.activity =activity2;
		this.requestdropdownData=areaData;
		inflater = (LayoutInflater) activity2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return requestdropdownData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return requestdropdownData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_autocomplete, null);
			holder = new ViewHolder();

			holder.selectedname = (TextView) convertView
					.findViewById(R.id.xtxtselectName);
			convertView.setTag(holder);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.selectedname.setText(requestdropdownData.get(position));

		
		
		
		

		return convertView;
	}
	
	
	static class ViewHolder 
	{
		public TextView selectedname;
		
	//	public ImageView img;

	}

}
