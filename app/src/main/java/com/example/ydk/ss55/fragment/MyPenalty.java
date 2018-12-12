package com.example.ydk.ss55.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.adapter.Club_List_Adapter;
import com.example.ydk.ss55.adapter.PenaltyInfo_List_Adapter;
import com.example.ydk.ss55.model.ClubData;
import com.example.ydk.ss55.model.PenaltyInfoData;
import com.example.ydk.ss55.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPenalty extends Fragment {

    private TextView myPenalty;
    private ListView listView;
    private PenaltyInfo_List_Adapter adapter;
    private ArrayList<PenaltyInfoData> arrayList;
    private final String TAG = "MYPENALTY";

    ServerThread serverThread = ServerThread.getInstacne();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_my_penalty,container,false);

        TextView textView = (TextView)rootView.findViewById(R.id.takeUser);
        textView.setText(User.getUname() + " 님의 총 벌금액 : ");

        myPenalty = (TextView)rootView.findViewById(R.id.myPenalty);


        listView = (ListView)rootView.findViewById(R.id.lvData);
        arrayList = new ArrayList<>();
        adapter = new PenaltyInfo_List_Adapter(getContext(),arrayList);
        listView.setAdapter(adapter);

        serverThread.setFragmentHandler(mHandler);
        serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.PENALTYUSER);


       /*
       Intent intent = getActivity().getIntent();
       String tmp = intent.getStringExtra("email_To_HelloMain");
       Log.d("penalty",tmp);
       Database.UserInfo = Database.myDBHelper.getReadableDatabase();
        Cursor cursor = Database.UserInfo.query("UserInfo",Database.user,"email=?",
                new String[]{tmp},null,null,null);
        if(cursor != null){
            myPenalty.setText("");
            int penalty_col = cursor.getColumnIndex("Penalty");
            while (cursor.moveToNext()){
                int penalty = cursor.getInt(penalty_col);
                Log.d("penalty","내가 지출한 벌금 액 : " + String.valueOf(penalty));
                myPenalty.setText(" " + String.valueOf(penalty) + " 원");
            }
            cursor.close();
        }
        Database.myDBHelper.close();
        */
        return rootView;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.obj.toString());
            if(msg.what != ServerThread.Type.PENALTYUSER)
                return;
            try {
                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                String code = jsonObject.getString("code");
                if(code.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    int sum = 0;
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        String club_name = json.getString("c_club_name");
                        String u_email1 = json.getString("u_email");
                        int penalty = json.getInt("penalty");
                        sum += penalty;
                        PenaltyInfoData tmp = new PenaltyInfoData(club_name, penalty);
                        arrayList.add(tmp);
                    }
                    myPenalty.setText(String.valueOf(sum));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new PenaltyInfo_List_Adapter(getContext(),arrayList);
            listView.setAdapter(adapter);
        }
    };
}
