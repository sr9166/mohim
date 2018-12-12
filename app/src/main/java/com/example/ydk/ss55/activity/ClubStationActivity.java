package com.example.ydk.ss55.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ydk.ss55.fragment.GroupInfoActivity;
import com.example.ydk.ss55.fragment.GroupMainActivity;
import com.example.ydk.ss55.fragment.NoticeInfoActivity;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.fragment.AttendInfo;

public class ClubStationActivity extends AppCompatActivity {

    private GroupInfoActivity groupInfoActivity;
    private GroupMainActivity groupMainActivity;
    private AttendInfo attendInfo;
    private NoticeInfoActivity noticeInfoActivity;
    private String clubName,userName;
    private FragmentManager fragmentManager;
    public static String clubBoss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_station);

        Intent intent =getIntent();

        String tmpName = intent.getStringExtra("VoteName");
        clubName = intent.getStringExtra("ClubName");
        userName = intent.getStringExtra("UserName");
        clubBoss = intent.getStringExtra("clubBoss");

        Log.d("asdf",tmpName+", "+ clubName + " 을 투표제목으로 작성했네요.");

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        groupInfoActivity = new GroupInfoActivity();
        groupMainActivity = new GroupMainActivity();
        attendInfo = new AttendInfo();
        noticeInfoActivity = new NoticeInfoActivity();


        getSupportFragmentManager().beginTransaction().add(R.id.container,groupInfoActivity).commit();

        //탭에 띄우는 거 정하는 영역임.
        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Main"));
        tabs.addTab(tabs.newTab().setText("Attend"));
        tabs.addTab(tabs.newTab().setText("Notice"));
        tabs.addTab(tabs.newTab().setText("Penalty"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                android.support.v4.app.Fragment selected = null;
                if(position==0){
                    selected = groupInfoActivity;
                }else if(position==1) {
                    selected = attendInfo;
                }else if(position==2){
                    selected = noticeInfoActivity;
                }else if(position==3){
                    selected = groupMainActivity;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
