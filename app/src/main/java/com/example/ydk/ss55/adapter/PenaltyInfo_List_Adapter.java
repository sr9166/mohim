package com.example.ydk.ss55.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ydk.ss55.R;
import com.example.ydk.ss55.model.NoticeData;
import com.example.ydk.ss55.model.PenaltyInfoData;

import java.util.ArrayList;

public class PenaltyInfo_List_Adapter extends BaseAdapter{
    private Context ctx;
    private ArrayList<PenaltyInfoData> data;


    public PenaltyInfo_List_Adapter(Context ctx, ArrayList<PenaltyInfoData> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
            convertView = inflater.inflate(R.layout.penaltyinfo,parent,false);
        }

        TextView clubName_ = (TextView)convertView.findViewById(R.id.clubName);
        clubName_.setText(data.get(position).getClubName());

        TextView penalty_ = (TextView)convertView.findViewById(R.id.penalty);
        penalty_.setText(String.valueOf(data.get(position).getPenalty()));


        return convertView;
    }

    public void add(PenaltyInfoData data1) {
        data.add(0, data1);
    }
}