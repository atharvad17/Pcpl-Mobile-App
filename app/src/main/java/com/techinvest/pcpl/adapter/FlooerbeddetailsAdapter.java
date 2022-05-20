package com.techinvest.pcpl.adapter;

import java.util.List;

import com.techinvest.pcpl.adapter.CarpetAreaAdapter.ViewHolder;
import com.techinvest.pcpl.model.GetCarpetAreaResponse;
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

public class FlooerbeddetailsAdapter extends BaseAdapter {

	Activity activity;
	List<GetCarpetAreaResponse> floorAreaData;
	LayoutInflater inflater;

	public FlooerbeddetailsAdapter(Activity activity,
			List<GetCarpetAreaResponse> floorAreaData) {
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.floorAreaData=floorAreaData;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return floorAreaData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return floorAreaData.get(position);
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
			convertView = inflater.inflate(R.layout.item_floorbeddetail, null);
			holder = new ViewHolder();

			holder.edtfloorbed = (TextView) convertView
					.findViewById(R.id.xedtflowerbeds);
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

		holder.edtfloorbed.setText(floorAreaData.get(position).getNames());
		holder.edtLength.setText(floorAreaData.get(position).getLengths());
		holder.edtWidht.setText(floorAreaData.get(position).getBreadths());
		holder.edtTotalArea.setText(floorAreaData.get(position).getArea());

		
		
		
		

		return convertView;
	}
	
	
	static class ViewHolder 
	{
		public TextView edtfloorbed;
		public TextView edtLength;
		public TextView edtWidht;
		public TextView edtTotalArea;
		
	

	}
	

}
