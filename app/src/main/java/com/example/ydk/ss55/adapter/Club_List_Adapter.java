package com.example.ydk.ss55.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ydk.ss55.R;
import com.example.ydk.ss55.fragment.hellomain;
import com.example.ydk.ss55.model.ClubData;

import java.util.ArrayList;
import java.util.Locale;

public class Club_List_Adapter extends BaseAdapter {
    private Context ctx;
    //private ClubData[] data;
    private ArrayList<ClubData> data1;
    private final String TAG = "CLUBLISTADAPTER";

    public Club_List_Adapter(Context ctx/*, ClubData[] data*/, ArrayList<ClubData> data1) {
        this.ctx = ctx;
        //this.data = data;
        this.data1 = new ArrayList<>();
        this.data1.addAll(data1);
    }

    @Override
    public int getCount() {
        //return data.length;
        return data1.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.club_list,parent,false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.clubImage);
        imageView.setImageResource(R.drawable.playing);

        TextView clubName_ = (TextView)convertView.findViewById(R.id.clubName);
        //clubName_.setText(data[position].getClubName());
        clubName_.setText(data1.get(position).getClubName());

        TextView clubDuringTime_ = (TextView)convertView.findViewById(R.id.duringTime);
        //clubDuringTime_.setText(data[position].getDuringTime());
        clubDuringTime_.setText(data1.get(position).getDuringTime());

        return convertView;
    }

    public void addItem(ClubData data) {
        data1.add(0, data);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        Log.d(TAG,charText);
        data1.clear();
        if (charText.length() == 0) {
            data1.addAll(hellomain.mDatas);
        } else {
            Log.d(TAG,"ELSE 시작" + data1.size());
            for (ClubData data : hellomain.mDatas) {
                Log.d(TAG, data.getClubName() + "\t" + charText);
                if (data.getClubName().toLowerCase().contains(charText)) {
                    data1.add(data);
                    Log.d(TAG,data.toString());
                }
            }
        }
        notifyDataSetChanged();
    }
}