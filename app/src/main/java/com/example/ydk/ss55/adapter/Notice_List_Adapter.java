package com.example.ydk.ss55.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ydk.ss55.model.AttendData;
import com.example.ydk.ss55.model.NoticeData;
import com.example.ydk.ss55.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

public class Notice_List_Adapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<NoticeData> data;
    private final SparseBooleanArray mCollapsedStatus;

    public Notice_List_Adapter(Context ctx, ArrayList<NoticeData> data) {
        this.ctx = ctx;
        this.data = data;
        mCollapsedStatus = new SparseBooleanArray();
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
        final ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.notice_list,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TextView tv = (TextView) convertView.findViewById(R.id.noticeName);
        tv.setText(data.get(position).getNoticeName());
        viewHolder.expandableTextView.setText(data.get(position).getNoticeConent(), mCollapsedStatus, position);

        return convertView;
    }

    public void add(NoticeData data1) {
        data.add(0, data1);
    }

    private static class ViewHolder{
        ExpandableTextView expandableTextView;
    }
}
