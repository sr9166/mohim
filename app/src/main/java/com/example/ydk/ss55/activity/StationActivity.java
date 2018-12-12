package com.example.ydk.ss55.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.activity.MakeNewGroupActivity;
import com.example.ydk.ss55.fragment.MyClub;
import com.example.ydk.ss55.fragment.MyInformationActivity;
import com.example.ydk.ss55.fragment.MyNotice;
import com.example.ydk.ss55.fragment.MyPenalty;
import com.example.ydk.ss55.fragment.hellomain;
import com.example.ydk.ss55.model.ClubData;
import com.example.ydk.ss55.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StationActivity extends AppCompatActivity {
    private String TAG = "StationActivity";
    private MyInformationActivity myInformation;
    private MyPenalty myPenalty;
    private MyNotice myNotice;
    private com.example.ydk.ss55.fragment.hellomain hellomain;    //메인 리스트뷰를 나타낸다.
    private MyClub myClub;          //내가 속한 동아리를 나타낸다.
    private ArrayList<String> mData = new ArrayList<String>();
    private ListView mList;
    private ArrayAdapter<String> mAdapter;
    private Cursor cursorr;
    private String ClubName, ClubInfo, WhoMakeClub, user;
    private int UserCount, penalty;
    private Button btnback; //뒤로가기 버튼을 눌렀을 때
    private Button makeClub;    //그룹 생성 버튼을 눌렀을 때

    ServerThread serverThread = ServerThread.getInstacne();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        Log.d(TAG, User.toJSson().toString());
        Intent intent = getIntent();
        user = intent.getStringExtra("email_To_HelloMain");
        Log.d("helllo",user + "");

        //그룹을 생성해주는 버튼 입력해주기
        makeClub = (Button)findViewById(R.id.make_group);
        makeClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("checkbtn","그룹 생성 버튼 눌렀을 때 함수 안으로 들어왔어용~");
                Intent intent = new Intent(getApplicationContext(), MakeNewGroupActivity.class);
                intent.putExtra("WhoMakeClub", user);
                startActivityForResult(intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            }
        });
        //뒤로가기 버튼을 눌렀을 때
        btnback = (Button)findViewById(R.id.btn_logout);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        myInformation = new MyInformationActivity();
        myPenalty = new MyPenalty();
        myNotice = new MyNotice();
        hellomain = new hellomain();
        myClub = new MyClub();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().add(R.id.container,hellomain).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("피드"));
        tabs.addTab(tabs.newTab().setText("나의\n 스터디"));
        tabs.addTab(tabs.newTab().setText("벌금"));
        tabs.addTab(tabs.newTab().setText("내정보"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if(position ==0){
                    selected = hellomain;
                }else if(position==1){
                    selected = myClub;
                }else if(position==2){
                    selected = myPenalty;
                }else if(position ==3){
                    selected = myInformation;
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
    public void listExpand(int index){
        Toast.makeText(getApplicationContext(),"Tsdfs",Toast.LENGTH_SHORT).show();
        if(index == 0 ){
            Log.d("btncheck", "버튼 클릭 완료------!!");
            Database.ClubData = Database.clubInfoHelper.getReadableDatabase();
            Database.cursor = Database.ClubData.query("ClubData", Database.club, null,
                    null, null, null, null);
            if (Database.cursor != null) {
                showResult1(Database.cursor);
                Database.cursor.close();
            }
        }
    }

    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == 0 && resultCode == RESULT_OK) {
                Log.d("MakeClubData", "인텐트 잘 넘어왔어요------");
                ClubName = data.getStringExtra("ClubName");
                ClubInfo = data.getStringExtra("ClubInfo");
                WhoMakeClub = user;
                Log.d("MakeClubData", " final make" +
                        "\nClubName : " + ClubName +
                        "\nClubInfo : " + ClubInfo +
                        "\nWhoMakeClub : " + WhoMakeClub
                );
                ClubData tmp = new ClubData(R.drawable.playing,ClubName,ClubInfo, 0);
                hellomain.listViewRevalidate(tmp);
                serverThread.setFragmentHandler(mHandler);
                serverThread.setAddClub(ClubName, ClubInfo);
                serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.ADDCLUB);
//                showResult1(cursorr);
            }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showResult1(Cursor cursor) {
        //test.setText("");
        int name_col = Database.cursor.getColumnIndex("ClubName");
        int info_col = Database.cursor.getColumnIndex("ClubInfo");
        int who_col = Database.cursor.getColumnIndex("WhoMakeClub");
        int count_col = Database.cursor.getColumnIndex("UserCount");
        while (Database.cursor.moveToNext()) {
            ClubName = Database.cursor.getString(name_col);
            ClubInfo = Database.cursor.getString(info_col);
            WhoMakeClub = Database.cursor.getString(who_col);
            UserCount = Database.cursor.getInt(count_col);
            //test.append(ClubName + ", " + ClubInfo + ", " + WhoMakeClub + ", " + String.valueOf(UserCount) + "\n");
            mData.add(ClubName);
        }
        Log.d("onon", "인텐트 잘 넘어왔어요------!!");
    }
    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.obj.toString());
            if(msg.what != ServerThread.Type.ADDCLUB)
                return;
            try {
                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                String code = jsonObject.getString("code");
                if(code.equals("200")) {
                    new SweetAlertDialog(StationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("SUCCESS")
                            .setContentText("Add Club Success!")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
