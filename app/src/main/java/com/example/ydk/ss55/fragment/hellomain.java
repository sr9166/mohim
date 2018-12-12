package com.example.ydk.ss55.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.activity.ClubStationActivity;
import com.example.ydk.ss55.activity.StationActivity;
import com.example.ydk.ss55.adapter.Club_List_Adapter;
import com.example.ydk.ss55.model.ClubData;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class hellomain extends Fragment {
    private final String TAG = "HELLOMAIN";
    private TextView helloUser;
    private ArrayList<String> mData = new ArrayList<String>();
    private ListView mList;
    private ArrayAdapter<String> mAdapter;
    private String ClubName, ClubInfo, WhoMakeClub, user;
    private int UserCount;

    private MaterialSearchBar searchBar;
    public static ArrayList<ClubData> mDatas;
    private ListView lili;
    private Club_List_Adapter club_list_adapter;

    ServerThread serverThread = ServerThread.getInstacne();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.activity_hellomain, container, false);
        mDatas = new ArrayList<>();
        lili = (ListView)v.findViewById(R.id.lvData);
        lili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Intent intent = new Intent(getActivity().getApplicationContext(), GroupMainActivity.class);
                ClubData temp = mDatas.get(i);
                Intent intent = new Intent(getContext(), ClubStationActivity.class);
                intent.putExtra("ClubName", temp.getClubName());
                intent.putExtra("UserName", user);
                intent.putExtra("CLubInfo", ClubInfo);
                intent.putExtra("WhoMakeClub", WhoMakeClub);
                intent.putExtra("clubBoss", temp.getU_email());
                intent.putExtra("userCount", temp.getUser_count());
                intent.putExtra("clubInfo", temp.getClubInfo());
                Log.d(TAG, temp.toString());

                Log.d("MakeClubData", " final make" +
                        "\nClubName : " + mDatas.get(i).getClubName() +
                        "\nClubInfo : " + ClubInfo +
                        "\nWhoMakeClub : " + WhoMakeClub
                );
                startActivity(intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });
        serverThread.setFragmentHandler(mHandler);
        serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.SEARCHCLUB);
        //showListView1();

        Intent intent = getActivity().getIntent();
        user = intent.getStringExtra("email_To_HelloMain");
        //helloUser = (TextView) v.findViewById(R.id.textView3);
        //helloUser.setText(user + " 님, 환영합니다.");

        club_list_adapter = new Club_List_Adapter(getContext(),mDatas);
        searchBar = (MaterialSearchBar) v.findViewById(R.id.searchBar);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, s.toString());
                club_list_adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Log.d("MakeClubData", "인텐트 잘 넘어왔어요------");
            ClubName = data.getStringExtra("ClubName");
            ClubInfo = data.getStringExtra("ClubInfo");
            WhoMakeClub = data.getStringExtra("WhoMakeClub");
            Log.d("MakeClubData", " final make" +
                    "\nClubName : " + ClubName +
                    "\nClubInfo : " + ClubInfo +
                    "\nWhoMakeClub : " + WhoMakeClub
            );
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    private void showListView1(){
        mDatas = new ArrayList<ClubData>();
        club_list_adapter = new Club_List_Adapter(getContext(),mDatas);
        Database.ClubData = Database.clubInfoHelper.getReadableDatabase();
        Database.cursor = Database.ClubData.query("ClubData", Database.club, null,
                null, null, null, null);
        if (Database.cursor != null) {
            int name_col = Database.cursor.getColumnIndex("ClubName");
            int info_col = Database.cursor.getColumnIndex("ClubInfo");
            while (Database.cursor.moveToNext()) {
                ClubName = Database.cursor.getString(name_col);
                ClubInfo = Database.cursor.getString(info_col);
                ClubData tmp = new ClubData(R.drawable.playing,ClubName,ClubInfo);
                mDatas.add(tmp);
                Log.d("database", "info : " + ClubName + ClubInfo + WhoMakeClub + UserCount);
                club_list_adapter.notifyDataSetChanged();
            }
            Database.cursor.close();
        }
    }
    */

    public void listViewRevalidate(ClubData data) {
        mDatas.add(0, data);
        club_list_adapter.addItem(data);
        club_list_adapter.notifyDataSetChanged();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.obj.toString());
            if(msg.what != ServerThread.Type.SEARCHCLUB)
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
            Log.d(TAG, String.valueOf(mDatas.size()));
            club_list_adapter = new Club_List_Adapter(getContext(),mDatas);
            lili.setAdapter(club_list_adapter);
        }
    };
}