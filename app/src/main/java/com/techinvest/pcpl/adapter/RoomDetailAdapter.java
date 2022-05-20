package com.techinvest.pcpl.adapter;

import java.util.List;

import com.techinvest.pcpl.adapter.RequestDetailAdapter.ViewHolder;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.GetRoomdetailResponse;
import com.techinvest.pcpl.model.RequestDropdownData;
import com.techinvest.pcplrealestate.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RoomDetailAdapter extends BaseAdapter {
	Activity activity;
	LayoutInflater inflater;
	List<GetRoomdetailResponse> roomdetailData;
	//List<String> roomTypeData;
	//List<String> flooringData;
	//List<String> doorsTypeData;
	//List<String> windowTypeData;
	//List<String> wCTypeData;
	//List<String> kitchenType;
	
	//ListPopupWindow lstroomType;
	//ListPopupWindow lstkitchenType;
	

	@SuppressLint("NewApi")
	public RoomDetailAdapter(Activity activity,
			List<GetRoomdetailResponse> roomdetailData) {
		
		this.activity=activity;
		this.roomdetailData=roomdetailData;
		//this.roomTypeData=roomTypeData;
		//this.flooringData=flooringData;
		//this.doorsTypeData=doorsTypeData;
		//this.windowTypeData=windowTypeData;
		//this.wCTypeData=wCTypeData;
		//this.kitchenType=kitchenType;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//lstroomType=new ListPopupWindow(activity);
		//lstkitchenType=new ListPopupWindow(activity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return roomdetailData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return roomdetailData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_roomdetailnew, null);
			holder = new ViewHolder();

			holder.edtAutoroomType = (TextView) convertView
					.findViewById(R.id.xedtautorooms);
			holder.edtAutoFlooring = (TextView) convertView
					.findViewById(R.id.xedtautoroomflooring);
			holder.edtAutodoorsType = (TextView) convertView
					.findViewById(R.id.xedtautodoorsdiscription);
			holder.edtAutowindowType = (TextView) convertView
					.findViewById(R.id.xedtautowindowsdiscription);
			holder.edtAutoKichanType = (TextView) convertView
					.findViewById(R.id.xedtautokitchenplatform);
			holder.edtAutowCType = (TextView) convertView
					.findViewById(R.id.xedtautowctype);
			
			/*holder.imgRoomtype = (ImageView) convertView
					.findViewById(R.id.ximgdropdownrooms);
			
			holder.imgFlooring = (ImageView) convertView
					.findViewById(R.id.ximgdropdownroomflooring);
			holder.imgdoorsType = (ImageView) convertView
					.findViewById(R.id.ximgdropdowndoorsdiscription);
			holder.imgwindowType = (ImageView) convertView
					.findViewById(R.id.ximgdropdownwindowsdiscription);
			holder.imgKichanType = (ImageView) convertView
					.findViewById(R.id.ximgdropdownkitchenplatform);
			holder.imgwCType = (ImageView) convertView
					.findViewById(R.id.ximgdropdownwctype);
			
			holder.imgKichanType.setTag(position);*/
			convertView.setTag(holder);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}
      // if(roomdetailData.get(position).getRequestId().equals(AppSetting.getRequestId())){
		holder.edtAutoroomType.setText(roomdetailData.get(position).getRoomType());
		holder.edtAutoFlooring.setText(roomdetailData.get(position).getFlooringType());
		holder.edtAutodoorsType.setText(roomdetailData.get(position).getDoorType());
		holder.edtAutowindowType.setText(roomdetailData.get(position).getWindowType());
		holder.edtAutoKichanType.setText(roomdetailData.get(position).getKitchenPlatformType());
		holder.edtAutowCType.setText(roomdetailData.get(position).getWCType());
     // }

		
		
		
		/*lstkitchenType.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
			
				//if(parent.getItemAtPosition(position).toString()!=null)
				String selecteditem = parent.getItemAtPosition(position)
						.toString();
				holder.edtAutoKichanType.setText(selecteditem);
				lstkitchenType.dismiss();

			}
		});
		
		holder.imgKichanType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (kitchenType != null && kitchenType.size() > 0)
					lstkitchenType.setAdapter(new LocationDetailAdapter(activity, kitchenType));
				lstkitchenType.setAnchorView(holder.edtAutoKichanType);
				lstkitchenType.setModal(true);
				lstkitchenType.show();
				
			}
		});*/
		

		return convertView;
	}
	
	
	static class ViewHolder 
	{
		//public TextView selectedname;
		TextView edtAutoroomType;
		TextView edtAutoFlooring;
		TextView edtAutodoorsType;
		TextView edtAutowindowType;
		TextView edtAutoKichanType;
		TextView edtAutowCType;
		/*ImageView imgRoomtype;
		ImageView imgFlooring;
		ImageView imgdoorsType;
		ImageView imgwindowType;
		ImageView imgKichanType;
		ImageView imgwCType;*/
		
	

	}
	
}
