package com.techinvest.pcpl.adapter;

import java.util.List;

import com.techinvest.pcpl.adapter.LocationDetailAdapter.ViewHolder;
import com.techinvest.pcpl.model.GetCarpetAreaResponse;
import com.techinvest.pcplrealestate.Carpetdetails;
import com.techinvest.pcplrealestate.R;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CarpetAreaAdapter extends BaseAdapter {
	Activity activity;
	List<GetCarpetAreaResponse> carpetAreaData;
	LayoutInflater inflater;

	public CarpetAreaAdapter(Activity activity,
			List<GetCarpetAreaResponse> carpetAreaData) {
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.carpetAreaData=carpetAreaData;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return carpetAreaData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return carpetAreaData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_carpetarea, null);
			holder = new ViewHolder();

			holder.edtCarpetArea = (TextView) convertView
					.findViewById(R.id.xedtcarpetarea);
			holder.edtLength = (TextView) convertView
					.findViewById(R.id.xedtlength);
			holder.edtWidht = (TextView) convertView
					.findViewById(R.id.xedtwidth);
			holder.edtTotalArea = (TextView) convertView
					.findViewById(R.id.xedtTotalarea);
			convertView.setTag(holder);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.edtCarpetArea.setText(carpetAreaData.get(position).getNames());
		holder.edtLength.setText(carpetAreaData.get(position).getLengths());
		holder.edtWidht.setText(carpetAreaData.get(position).getBreadths());
		holder.edtTotalArea.setText(carpetAreaData.get(position).getArea());

		
		
		
		

		return convertView;
	}
	
	
	static class ViewHolder 
	{
		public TextView edtCarpetArea;
		public TextView edtLength;
		public TextView edtWidht;
		public TextView edtTotalArea;
		
	

	}
	
	

}
