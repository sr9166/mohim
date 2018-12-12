package com.example.ydk.ss55.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ydk.ss55.model.PenaltyUserData;
import com.example.ydk.ss55.R;

import java.util.ArrayList;

public class PenaltyUser_List_Adapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<PenaltyUserData> data;


    public PenaltyUser_List_Adapter(Context ctx, ArrayList<PenaltyUserData> data) {
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
            convertView = inflater.inflate(R.layout.penalty_user_list,parent,false);
        }

        TextView penalty_user_name_ = (TextView)convertView.findViewById(R.id.penalty_user_name);
        penalty_user_name_.setText("유저 이름 : " + data.get(position).getPenalty_User());

        TextView penalty_point_ = (TextView)convertView.findViewById(R.id.penalty_point);
        penalty_point_.setText("벌금 액수 : " + data.get(position).getPenalty());


        return convertView;
    }
}
