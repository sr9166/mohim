package com.example.ydk.ss55.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.adapter.Club_List_Adapter;
import com.example.ydk.ss55.model.ClubData;
import com.example.ydk.ss55.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.ydk.ss55.DBHelper.Database.clubMembers;

public class GroupInfoActivity extends Fragment {

    private TextView clubName_;  //동아리 이름
    private TextView clubBoss_;  //동아리 방장
    private TextView clubLevel_;    //동아리 레벨
    private TextView userCount_; //동아리 인원 수
    private TextView members_;  //동아리 맴버 전체 이름
    private TextView clubInfo_;  //동아리 소개 맨트
    private TextView clubFame_;  //동아리의 명성
    private Button signInBtn;   //동아리 가입하는 버튼
    private String clubName,clubBoss,clubLevel,clubInfo,clubFame,user,member;
    private int userCount;

    ServerThread serverThread = ServerThread.getInstacne();
    private final String TAG = "GROUPINFOACTIVITY";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_group_info,container,false);
        clubName_ = (TextView)rootView.findViewById(R.id.groupName);
        clubBoss_ = (TextView)rootView.findViewById(R.id.clubBoss);
        clubLevel_ = (TextView)rootView.findViewById(R.id.clubLevel);
        userCount_ = (TextView)rootView.findViewById(R.id.userCount);
        clubInfo_ = (TextView)rootView.findViewById(R.id.clubInfo);
        clubFame_ = (TextView)rootView.findViewById(R.id.clubFame);

        Intent intent = getActivity().getIntent();
        user = intent.getStringExtra("UserName");
        clubName = intent.getStringExtra("ClubName");
        clubName_.setText("ClubName | " + clubName);
        clubBoss = intent.getStringExtra("clubBoss");
        clubBoss_.setText(clubBoss);
        clubLevel_.setText("1");
        userCount = intent.getIntExtra("userCount", 0);
        userCount_.setText(String.valueOf(userCount));
        clubInfo = intent.getStringExtra("clubInfo");
        clubInfo_.setText(clubInfo);



        /*
        //맴버 동아리 인원 수와, 구성원들을 보여주는 곳이다.
        members_ = (TextView) rootView.findViewById(R.id.members);
            Database.ClubMembersData = Database.clubMembersHelper.getReadableDatabase();
            Cursor cursor = Database.ClubMembersData.query("ClubMembersData", clubMembers, "ClubName=?",
                    new String[]{clubName}, null, null, null);
            if (cursor != null) {
                members_.setText("");
                int name_col = cursor.getColumnIndex("ClubName");
                int member_col = cursor.getColumnIndex("ClubMembers");
                while (cursor.moveToNext()) {
                    member = cursor.getString(member_col);
                    String name = cursor.getString(name_col);
                    members_.append("name : " + name + ", member : " +member + ", ");
                }
            }
        */

        //가입하기 누르면 유저증가한다.
        signInBtn = (Button)rootView.findViewById(R.id.clubSignInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("가입 신청")
                        .setContentText(clubInfo + " 모임에 가입하시겠습니까?")
                        .setConfirmText("Confirm")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                serverThread.setFragmentHandler(mHandler);
                                serverThread.setRegisterClub(clubName);
                                serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.REGISTERCLUB);
                            }
                        })
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });


        //데이터베이스 이름에 맞는 소개 맨트 검색 과정
        //SetData1();

        //동아리 인원 데이터베이스에서 가져와서 표시해주기
        //SetData2();

        //방장은 누구인지 표시해주기
        //SetData3();


        return rootView;
    }
    //동아리 맨트를 띄워주는 데이터베이스를 찾아서 표시해줍니다.
    private void SetData1(){
        Database.ClubData = Database.clubInfoHelper.getReadableDatabase();
        Cursor cursor;
        cursor = Database.ClubData.query("ClubData",Database.club,"ClubName=?",
                new String[]{clubName},null,null,null);
        if(cursor != null){
            clubInfo_.setText("");
            int ClubInfo_ = cursor.getColumnIndex("ClubInfo");
            while (cursor.moveToNext()){
                clubInfo = cursor.getString(ClubInfo_);
                clubInfo_.setText(clubInfo);
            }
            cursor.close();
        }
        Database.clubInfoHelper.close();
    }

    //동아리 인원수를 띄워주는 데이터베이스를 찾아서 표시해줍니다.
    private void SetData2(){
        Database.ClubData = Database.clubInfoHelper.getReadableDatabase();
        Cursor cursor;
        cursor = Database.ClubData.query("ClubData",Database.club,"ClubName=?",
                new String[]{clubName},null,null,null);
        if(cursor != null){
            userCount_.setText("");
            int count = cursor.getColumnIndex("UserCount");
            while (cursor.moveToNext()){
                userCount = cursor.getInt(count);
                userCount_.setText(String.valueOf(userCount));
            }
            cursor.close();
        }
        Database.clubInfoHelper.close();
    }

    //방장은 누구인가를 띄워주는 데이터베이스를 찾아서 표시해줍니다.
    private void SetData3(){
        Database.ClubData = Database.clubInfoHelper.getReadableDatabase();
        Cursor cursor;
        cursor = Database.ClubData.query("ClubData",Database.club,"ClubName=?",
                new String[]{clubName},null,null,null);
        if(cursor != null){
            clubBoss_.setText("");
            int ClubBoss__ = cursor.getColumnIndex("WhoMakeClub");
            while (cursor.moveToNext()){
                clubBoss = cursor.getString(ClubBoss__);
                Log.d("clubBoss",clubBoss + "가 만들었음.");
                clubBoss_.setText(clubBoss);
            }
            cursor.close();
        }
        else{
            Log.e("errorCheck","cursor is null");
        }
        Database.clubInfoHelper.close();
    }

    //동아리 가입
//    static String[] clubMembers = {"_id","ClubName","ClubMembers","Penalty"};         //클럽 가입한 사람 스키마

    private void clubSignIn(){
        if(!signinUser(user)){//가입한 회원이 아닐경우
            Database.ClubMembersData = Database.clubMembersHelper.getWritableDatabase();
            Database.values = new ContentValues();
            Database.values.put("ClubName", clubName);
            Database.values.put("ClubMembers", user);
            Database.values.put("Penalty",0);
            Database.ClubMembersData.insert("ClubMembersData", null, Database.values);
            Database.clubMembersHelper.close();
            Log.d("ClubSignIn", "\nClubName : " + clubName +
                    "\nClubMembers : " + user
            );
            updateUserCount();  //동아리 유저 카운트를 올려준다.
        }
        else{
            Toast.makeText(getContext(),"이미 가입한 회원입니다.",Toast.LENGTH_SHORT).show();
        }
    }
    private void updateUserCount(){
        Database.ClubData = Database.clubInfoHelper.getWritableDatabase();
        String name = String.valueOf(clubName);
        Database.values = new ContentValues();
        Database.values.put("UserCount",++userCount);
        Database.ClubData.update("ClubData",Database.values,"ClubName=?",new String[]{name});
        Log.d("update",name+" " + userCount);
        Database.clubInfoHelper.close();
        userCount_.setText(String.valueOf(userCount));  //회원 증가업데이트
        members_.append("name : " + name + ", member : " +member + ", ");   //맴버 구성원 업데이트
    }

    private boolean signinUser(String user){
        Database.ClubMembersData = Database.clubMembersHelper.getReadableDatabase();
        //클럽 맴버를 찾음
        //SELECT ClubMembers
        //FROM ClubMembersData
        //WHERE ClubName="????";
        Cursor cur = Database.ClubMembersData.query("ClubMembersData",new String[]{"ClubMembers"},"ClubName=?",
                new String[]{clubName},null,null,null);
        if(cur != null){
            int tmpuser_col = cur.getColumnIndex("ClubMembers");
            while (cur.moveToNext()){
                String tmptmp = cur.getString(tmpuser_col);
                if(tmptmp.equals(user)){
                    return true;
                }
            }
            cur.close();
        }
        Database.clubMembersHelper.close();
        return false;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.obj.toString());
            if(msg.what != ServerThread.Type.REGISTERCLUB)
                return;
            try {
                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                String code = jsonObject.getString("code");
                if(code.equals("200")) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("SUCCESS")
                            .setContentText("Register Club Success!")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                    userCount_.setText(String.valueOf(userCount + 1));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
