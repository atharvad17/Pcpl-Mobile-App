package com.techinvest.pcpl.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcplrealestate.Layoutdetails;
import com.techinvest.pcplrealestate.R;
import com.techinvest.pcplrealestate.SecondScreenActivity;

public class LayoutdetailsPendingImagesGridViewAdapter extends ArrayAdapter<SetOfflinedata> {

    private Context mContext;
    private int layoutResourceId;
    private List<SetOfflinedata> mGridData; //= new List<SetOfflinedata>();

    public LayoutdetailsPendingImagesGridViewAdapter(Context mContext, int layoutResourceId, List<SetOfflinedata> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(List<SetOfflinedata> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.item_del_imageView = (ImageView) row.findViewById(R.id.grid_item_delete_image);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final SetOfflinedata item = mGridData.get(position);
        //holder.titleTextView.setText(Html.fromHtml(item.getTitle()));

        Log.e(LayoutdetailsPendingImagesGridViewAdapter.class.getName(),"Pos : "+position);
        if(item.getUploadStatus() == 0){
            File imgFile = new  File(item.getSaveuserdata());
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.imageView.setImageBitmap(myBitmap);
        }

        holder.item_del_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean delitem = SecondScreenActivity.mypcplData.deleteImageRecordByPath(AppSetting.getRequestId(),item.getSaveuserdata());
                if(delitem){
                    File fdelete = new File(item.getSaveuserdata());
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            //System.out.println("file Deleted :" + item.getSaveuserdata());
                            Log.i(LayoutdetailsPendingImagesGridViewAdapter.class.getName(), "file Deleted :" + item.getSaveuserdata());
                        } else {
                            //System.out.println("file not Deleted :" + localFilePath);
                            Log.i(LayoutdetailsPendingImagesGridViewAdapter.class.getName(), "file not Deleted :" + item.getSaveuserdata());
                        }
                    }
                    mGridData.remove(position);
                    notifyDataSetChanged();
                }else{
                    mGridData.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        return row;
    }

    static class ViewHolder {
        //TextView titleTextView;
        ImageView imageView;
        ImageView item_del_imageView;
    }

}
