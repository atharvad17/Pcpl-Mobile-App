package com.techinvest.pcpl.adapter;

import java.util.List;

import com.techinvest.pcpl.adapter.CustomArrayAdapter.ViewHolder;
import com.techinvest.pcplrealestate.R;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
//import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class RequestDetailAdapter extends BaseAdapter {
	Activity activity;
	List<String> requestdropdownData;
	LayoutInflater inflater;

	public RequestDetailAdapter(FragmentActivity activity, List<String> areaData) {

		this.activity =activity;
		this.requestdropdownData=areaData;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
