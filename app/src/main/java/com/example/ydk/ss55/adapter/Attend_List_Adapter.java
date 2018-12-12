package com.example.ydk.ss55.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ydk.ss55.model.AttendData;
import com.example.ydk.ss55.R;

import java.util.ArrayList;

public class Attend_List_Adapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<AttendData> data1;


    public Attend_List_Adapter(Context ctx, ArrayList<AttendData> data1) {
        this.ctx = ctx;
        this.data1 = data1;
    }

    @Override
    public int getCount() {
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
            convertView = inflater.inflate(R.layout.attend_list,parent,false);
        }

        TextView attendName_ = (TextView)convertView.findViewById(R.id.attendTitle);
        attendName_.setText(data1.get(position).getAttendName());

        TextView limitDate_ = (TextView)convertView.findViewById(R.id.limitDate);
//        limitDate_.setText(data1.get(position).getDate());

        TextView limitTime_ = (TextView)convertView.findViewById(R.id.limitTime);
        limitTime_.setText(data1.get(position).dateFormat());

        return convertView;
    }

    public void add(AttendData data) {
        data1.add(0, data);
    }
}
