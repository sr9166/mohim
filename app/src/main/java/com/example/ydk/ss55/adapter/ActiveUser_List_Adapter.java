package com.example.ydk.ss55.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ydk.ss55.model.ActiveUserData;
import com.example.ydk.ss55.R;

import java.util.ArrayList;

public class ActiveUser_List_Adapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<ActiveUserData> data;


    public ActiveUser_List_Adapter(Context ctx, ArrayList<ActiveUserData> data) {
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
            convertView = inflater.inflate(R.layout.active_user_list,parent,false);
        }

        TextView active_user_name_ = (TextView)convertView.findViewById(R.id.active_user_name);
        active_user_name_.setText("유저 이름 : " + data.get(position).getActive_user());

        TextView attendPoint_ = (TextView)convertView.findViewById(R.id.attendPoint);
        attendPoint_.setText("활동 횟수 : " + data.get(position).getAttendCount());

        return convertView;
    }
}
