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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.activity.ClubStationActivity;
import com.example.ydk.ss55.adapter.Club_List_Adapter;
import com.example.ydk.ss55.model.ClubData;
import com.example.ydk.ss55.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyClub extends Fragment {

    private ListView listView;
    private String user;
    private TextView helloUser;
    private ArrayList<String> mData = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;
    private String ClubName, ClubInfo;

    private ArrayList<ClubData> mDatas;
    private ListView lili;
    private Club_List_Adapter club_list_adapter;

    ServerThread serverThread = ServerThread.getInstacne();
    private final String TAG = "MYCLUB";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.my_club, container, false);

        Intent intent = getActivity().getIntent();
        user = intent.getStringExtra("email_To_HelloMain");
        helloUser = (TextView) rootView.findViewById(R.id.takeUser);
        helloUser.setText(User.getUname() + " 님이 가입한 동아리.");

        //showListView1();
        mDatas = new ArrayList<>();
        lili = (ListView) rootView.findViewById(R.id.listView);
        lili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Intent intent = new Intent(getActivity().getApplicationContext(), GroupMainActivity.class);
                ClubData temp = mDatas.get(i);
                Intent intent = new Intent(getContext(), ClubStationActivity.class);
                intent.putExtra("ClubName", temp.getClubName());
                intent.putExtra("UserName", user);
                intent.putExtra("CLubInfo", ClubInfo);
                intent.putExtra("clubBoss", temp.getU_email());
                intent.putExtra("userCount", temp.getUser_count());
                intent.putExtra("clubInfo", temp.getClubInfo());
                Log.d(TAG, temp.toString());
                startActivity(intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });
        serverThread.setFragmentHandler(mHandler);
        serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.SEARCHMYCLUB);
        return rootView;
    }

    /*
    private void showListView1(){
        mDatas = new ArrayList<ClubData>();
        club_list_adapter = new Club_List_Adapter(getContext(),mDatas);
        Database.ClubMembersData = Database.clubMembersHelper.getReadableDatabase();
        Cursor cur = Database.ClubMembersData.query("ClubMembersData",Database.clubMembers,"ClubMembers=?",
                new String[]{user},null,null,null);
        if(cur != null){
            int name_col = cur.getColumnIndex("ClubName");
            while (cur.moveToNext()) {
                ClubName = cur.getString(name_col);
                Log.d("database1123", "info : " + ClubName);
                Database.ClubData = Database.clubInfoHelper.getReadableDatabase();
                Cursor cur1 = Database.ClubData.query("ClubData",Database.club,"ClubName=?",
                        new String[]{ClubName},null,null,null);
                if(cur1 != null){
                    ClubData tmp = new ClubData(R.drawable.playing, ClubName, ClubInfo);
                    mDatas.add(tmp);
                    Log.d("database1123", "info : " + ClubName + ClubInfo);
                    club_list_adapter.notifyDataSetChanged();
                }
                cur1.close();
            }
            cur.close();
        }
        Database.clubInfoHelper.close();
        Database.clubMembersHelper.close();
    }
    */
    private void showListView(String user) {
        //클럽 맴버를 찾음
        //SELECT ClubMembers
        //FROM ClubMembersData
        //WHERE ClubName="????";
        //TODO : 이거 왜 안되는지 알기..! where ClubMembers = user;
        //해결함. 이유는 user값이 null이어서. 코드 순서 바꿔주니까 해결되었다.
        mData = new ArrayList<>();
        mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mData);
        Database.ClubMembersData = Database.clubMembersHelper.getReadableDatabase();
        Cursor cur = Database.ClubMembersData.query("ClubMembersData", Database.clubMembers, "ClubMembers=?",
                new String[]{user}, null, null, null);
        if (cur != null) {
            int tmpClubName_col = cur.getColumnIndex("ClubName");
            while (cur.moveToNext()) {
                String tmptmp = cur.getString(tmpClubName_col);
                mData.add(tmptmp);
                mAdapter.notifyDataSetChanged();
            }
            cur.close();
        }
        Database.clubMembersHelper.close();
        mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mData);
        mAdapter.notifyDataSetChanged();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.obj.toString());
            if(msg.what != ServerThread.Type.SEARCHMYCLUB)
                return;
            try {
                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                String code = jsonObject.getString("code");
                if(code.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        ClubName = json.getString("club_name");
                        ClubInfo = json.getString("club_info");
                        int penalty = json.getInt("penalty");
                        String u_email = json.getString("u_email");
                        int user_count = json.getInt("user_count");
                        ClubData tmp = new ClubData(R.drawable.playing,ClubName,ClubInfo, u_email, user_count, penalty);
                        mDatas.add(tmp);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            club_list_adapter = new Club_List_Adapter(getContext(),mDatas);
            club_list_adapter = new Club_List_Adapter(getContext(), mDatas);
            lili.setAdapter(club_list_adapter);
        }
    };
}

