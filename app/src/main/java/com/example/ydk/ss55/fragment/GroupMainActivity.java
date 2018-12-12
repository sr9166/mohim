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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.adapter.PenaltyInfo_List_Adapter;
import com.example.ydk.ss55.model.PenaltyInfoData;
import com.example.ydk.ss55.model.PenaltyUserData;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.adapter.ActiveUser_List_Adapter;
import com.example.ydk.ss55.adapter.PenaltyUser_List_Adapter;
import com.example.ydk.ss55.model.ActiveUserData;
import com.example.ydk.ss55.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.ydk.ss55.DBHelper.Database.club;
import static com.example.ydk.ss55.DBHelper.Database.cursor;

public class GroupMainActivity extends Fragment {

    private TextView myPenalty;
    private ListView listView;
    private PenaltyInfo_List_Adapter adapter;
    private ArrayList<PenaltyInfoData> arrayList;
    private final String TAG = "GROUPMAIN";

    ServerThread serverThread = ServerThread.getInstacne();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_my_penalty, container, false);

        Intent intent = getActivity().getIntent();
        String clubName = intent.getStringExtra("ClubName");

        TextView textView = (TextView)rootView.findViewById(R.id.takeUser);
        textView.setText(clubName + " 모임의 총 벌금액 : ");

        TextView textView1 = (TextView)rootView.findViewById(R.id.clubname);
        textView1.setText("유저 이메일");

        myPenalty = (TextView)rootView.findViewById(R.id.myPenalty);


        listView = (ListView)rootView.findViewById(R.id.lvData);
        arrayList = new ArrayList<>();
        adapter = new PenaltyInfo_List_Adapter(getContext(),arrayList);
        listView.setAdapter(adapter);

        serverThread.setFragmentHandler(mHandler);
        serverThread.setRegisterClub(clubName);
        serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.PENALTYCLUB);
        /*
        user = intent.getStringExtra("UserName");
        String Date = intent.getStringExtra("Date");
        String Time = intent.getStringExtra("Time");

        TextView textView = (TextView) rootView.findViewById(R.id.textView);
        textView.setText(user + "");

        mPenaltyList = (ListView) rootView.findViewById(R.id.listView1);
        penaltyUserData = new ArrayList<>();
        penaltyUser_list_adapter = new PenaltyUser_List_Adapter(getContext(), penaltyUserData);
//        showPenaltyList();
        mPenaltyList.setAdapter(penaltyUser_list_adapter);
        mPenaltyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(getContext(), penaltyUserData.get(i).getPenalty_User(), Toast.LENGTH_SHORT).show();
            }
        });

        mActiveList = (ListView) rootView.findViewById(R.id.listView2);
        activeUser_list_adapter = new ActiveUser_List_Adapter(getContext(), activeUserData);
        showListView();
        mActiveList.setAdapter(activeUser_list_adapter);
        mActiveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), activeUserData.get(i).getActive_user(), Toast.LENGTH_SHORT).show();
            }
        });
        */
        return rootView;
    }

    /*
    //attend점수랑 notice점수가 가장 많이 찍혀있는 사람 고르기.
//    static String[] clubMembers = {"_id","ClubName","ClubMembers","Penalty"};         //클럽 가입한 사람 스키마

    private void showPenaltyList() {
        penaltyUserData = new ArrayList<PenaltyUserData>();
        penaltyUser_list_adapter = new PenaltyUser_List_Adapter(getContext(), penaltyUserData);
        Database.ClubMembersData = Database.clubMembersHelper.getReadableDatabase();
        Cursor clubMember = Database.ClubMembersData.rawQuery("select ClubMembers, Penalty from ClubMembersData where ClubName = ?", new String[]{clubName});
        if (clubMember != null) {
            int members_col = clubMember.getColumnIndex("ClubMembers");
            int penalty_col = clubMember.getColumnIndex("Penalty");
            while (clubMember.moveToNext()) {
                String user_name = clubMember.getString(members_col);
                int penalty = clubMember.getInt(penalty_col);

                PenaltyUserData tmp = new PenaltyUserData(user_name, String.valueOf(penalty));
                if (penaltyUserData.size() != 0) {
                    if (!checkAdd(tmp)) {
                        penaltyUserData.add(tmp);
                    }
                } else {
                    penaltyUserData.add(tmp);
                }
                penaltyUser_list_adapter.notifyDataSetChanged();
            }
            cursor.close();
        }
    }
    public boolean checkAdd(PenaltyUserData tmp){
        boolean pandan = true;
        for(int i=0;i<penaltyUserData.size();i++){
            if(penaltyUserData.get(i).getPenalty_User().equals(tmp.getPenalty_User())){
                pandan = false;
                return pandan;
            }
        }
        return pandan;
    }
    public boolean checkAdd(ActiveUserData tmp){
        boolean pandan = true;
        for(int i=0;i<penaltyUserData.size();i++){
            if(penaltyUserData.get(i).getPenalty_User().equals(tmp.getActive_user())){
                pandan = false;
                return pandan;
            }
        }
        return pandan;
    }

    //attend점수랑 notice점수가 가장 많이 찍혀있는 사람 고르기.
    private void showListView() {
        activeUserData = new ArrayList<ActiveUserData>();
        activeUser_list_adapter = new ActiveUser_List_Adapter(getContext(), activeUserData);

        Database.ClubMembersData = Database.clubMembersHelper.getReadableDatabase();
        Cursor clubMember = Database.ClubMembersData.rawQuery("select ClubMembers from ClubMembersData where ClubName = ?", new String[]{clubName});
        if (clubMember != null) {
            int members_col = clubMember.getColumnIndex("ClubMembers");
            while (clubMember.moveToNext()) {
                String user_name = clubMember.getString(members_col);
                int attendCount = 0;

                Database.AttendReadOnce = Database.attendReadOnceHelper.getReadableDatabase();
                Cursor cursor = Database.AttendReadOnce.rawQuery("select UserName, AttendCheck from AttendReadOnce where UserName=?", new String[]{user_name});
                if (cursor != null) {
                    int acheck_col = cursor.getColumnIndex("AttendCheck");
                    while (cursor.moveToNext()) {
                        int acheck = cursor.getInt(acheck_col);
                        if (acheck != 0) {
                            attendCount++;
                        }
                    }
                    ActiveUserData tmp = new ActiveUserData(user_name, attendCount);
                    if (activeUserData.size() != 0) {
                        if (!checkAdd(tmp)) {
                            activeUserData.add(tmp);
                        }
                    } else {
                        activeUserData.add(tmp);
                    }
                    activeUser_list_adapter.notifyDataSetChanged();
                }
                cursor.close();
            }
        }
        clubMember.close();
    }
    */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.obj.toString());
            if(msg.what != ServerThread.Type.PENALTYCLUB)
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
                        PenaltyInfoData tmp = new PenaltyInfoData(u_email1, penalty);
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

