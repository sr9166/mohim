package com.example.ydk.ss55.fragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.activity.ClubStationActivity;
import com.example.ydk.ss55.activity.NoticeDetailsActivity;
import com.example.ydk.ss55.adapter.Attend_List_Adapter;
import com.example.ydk.ss55.adapter.Notice_List_Adapter;
import com.example.ydk.ss55.model.AttendData;
import com.example.ydk.ss55.model.NoticeData;
import com.example.ydk.ss55.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;
import static com.example.ydk.ss55.DBHelper.Database.cursor;
import static com.example.ydk.ss55.DBHelper.Database.myDBHelper;

public class NoticeInfoActivity extends Fragment {

    private ArrayList<NoticeData> mData;
    private String noticeName, noticeContents,clubName,user;
    private String limit_Date,limit_Time;
    private Button makeNoticeBtn;
    private ListView mList;

    private Notice_List_Adapter notice_list_adapter;
    private final String TAG = "NOTICEINFO";
    ServerThread serverThread = ServerThread.getInstacne();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_notice_info,container,false);

        TextView clubName_ = (TextView)rootView.findViewById(R.id.nnoticeName);

        Intent intent = getActivity().getIntent();

        clubName = intent.getStringExtra("ClubName");
        noticeContents =intent.getStringExtra("noticeContent");
        user = intent.getStringExtra("UserName");

        clubName_.setText(clubName + " 모임의 공지사항");

        mList = (ListView) rootView.findViewById(R.id.lvData);
        mData = new ArrayList<>();
        notice_list_adapter = new Notice_List_Adapter(getContext(),mData);
        //데이터베이스에서 전체 검색하기.
//        showListView();
        mList.setAdapter(notice_list_adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                /*
                if (belongToOurClub()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(mData.get(i).getNoticeName());
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setMessage(mData.get(i).getNoticeConent());
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            noReadBefore(i);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    Toast.makeText(getContext(), "동아리 회원만 보실수 있습니다!!", Toast.LENGTH_SHORT).show();
                }
                */
            }
        });

        makeNoticeBtn = (Button)rootView.findViewById(R.id.btnMakeNotice);
        makeNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBoss()) {
                    Intent intent = new Intent(getContext(), NoticeDetailsActivity.class);
                    intent.putExtra("ClubName", clubName);
                    intent.putExtra("UserName", user);
                    startActivityForResult(intent, 0);
                } else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText("Do not Permitted!")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            }
        });
        serverThread.setFragmentHandler(mHandler);
        serverThread.setRegisterClub(clubName);
        serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.SEARCHNOTICE);
        return rootView;
    }

    public boolean isBoss() {
        if(User.getEmail().equals(ClubStationActivity.clubBoss))
            return true;
        else
            return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String ClubName = data.getStringExtra("ClubName");
            String NoticeName = data.getStringExtra("NoticeName");
            String NoticeConent = data.getStringExtra("NoticeContents");
            NoticeData noticeData = new NoticeData(ClubName, NoticeName, NoticeConent, null, null);
            serverThread.setMakeNotice(ClubName, NoticeName, NoticeConent);
            serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.MAKENOTICE);
            notice_list_adapter.add(noticeData);
            notice_list_adapter.notifyDataSetChanged();
            serverThread.setRegisterClub(clubName);
            serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.SEARCHNOTICE);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showListView(){
        mData = new ArrayList<NoticeData>();
        notice_list_adapter = new Notice_List_Adapter(getContext(),mData);
        Database.NoticeData = Database.noticeHelper.getReadableDatabase();
        Database.cursor = Database.NoticeData.query("NoticeData", Database.notice, "ClubName=?",
                new String []{clubName}, null, null, null);
        if (Database.cursor != null) {
            int name_col = cursor.getColumnIndex("NoticeName");
            int content_col = cursor.getColumnIndex("NoticeContent");
            while (Database.cursor.moveToNext()) {
                noticeName = Database.cursor.getString(name_col);
                noticeContents = Database.cursor.getString(content_col);

                NoticeData tmp = new NoticeData(clubName,noticeName,noticeContents,limit_Date,limit_Time);
                mData.add(tmp);
                Log.d("database","info : "+clubName + noticeName + noticeContents + limit_Date + limit_Time);
                notice_list_adapter.notifyDataSetChanged();
            }
            Database.cursor.close();
        }
    }
    //이름, 공지 검색 후 안읽었을 경우에 돈 업뎃. 안읽었을경우 돈없뎃은안함.
    private void noReadBefore(int position) {
        Database.NoticeReadOnce = Database.noticeReadOnceHelper.getReadableDatabase();
        Cursor cursor = Database.NoticeReadOnce.query("NoticeReadOnce", Database.noticeReadOnce, "NoticeName=?",
                new String[]{noticeName}, null, null, null);
        if (cursor != null) {
            //모임 이름이 있다면,,,, 칼럼에서 유저이름을 다 뽑는다. 같은 모임을 가진 유저이름들 다 뽑히겠지?00
            int name_col = cursor.getColumnIndex("UserName");
            int check_col = cursor.getColumnIndex("NoticeCheck");
            while (cursor.moveToNext()) {
                String tmpName = cursor.getString(name_col);
                int tmpCheck = cursor.getInt(check_col);
                //지금 현재 접속해있는 유저 이름이랑 똑같아?
                if (tmpName.equals(user)) {
                    //지금 그 접속해있는 거 체크 되어있어??
                    if (tmpCheck == 0) {    //0이면 체크 안되어있는거니까 읽을 수 있음.
                        //여기서는 체크 되어있으면 돈업뎃 해주고 읽기는 가능하게
                        Earn_300(position);
                    }
                    else{
                        Toast.makeText(getContext(),"이미 확인 처리된 공지입니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void Earn_300(int position){
        //점수 오름 및 돈 획득
        Toast.makeText(getContext(),"공지 확인 정상적으로 처리되었습니다.",Toast.LENGTH_SHORT).show();
        Database.UserInfo = Database.myDBHelper.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        values.put("Money",takeUserMoney()+300);
        Database.UserInfo.update("UserInfo",values,"email=?",new String[]{user});
        myDBHelper.close();

        //공지체크 확인표시해주기.
        //유저의 이름, 모임 일치해야함.
        Database.NoticeReadOnce = Database.noticeReadOnceHelper.getWritableDatabase();
        ContentValues values1;
        values1 = new ContentValues();
        values1.put("NoticeCheck",takeUserReadCheck(position));
        //모임이름 일치하는거 구현함.
        if(compareAttendName(position)){
            Database.NoticeReadOnce.update("NoticeReadOnce",values1,"NoticeName=?",new String[]{noticeName});
        }else{
            Toast.makeText(getContext(),"Notice이름 불일치",Toast.LENGTH_LONG).show();
        }
    }


    private boolean compareAttendName(int position){
        boolean pandan = false;
        Database.NoticeReadOnce = Database.noticeReadOnceHelper.getReadableDatabase();
        Cursor cursor = Database.NoticeReadOnce.query("NoticeReadOnce",Database.noticeReadOnce,"NoticeName=?",
                new String []{noticeName},null,null,null);
        if(cursor != null){
            //모임 이름이 있다면,,,, 칼럼에서 유저이름을 다 뽑는다. 같은 모임을 가진 유저이름들 다 뽑히겠지?00
            int name_col = cursor.getColumnIndex("UserName");
            while (cursor.moveToNext()){
                String tmpName = cursor.getString(name_col);
                //지금 현재 접속해있는 유저 이름이랑 똑같아? 그럼 true 반영한다.
                if(tmpName.equals(user)){
                    pandan = true;
                }
            }
        }
        return pandan;
    }

    //앞으로 이 유저가 이 글을 읽었을 때 읽었다고 할지, 안읽었다고 할지 정해주는 함수.
    private boolean takeUserReadCheck(int position){
        Database.NoticeReadOnce = Database.noticeReadOnceHelper.getReadableDatabase();
        Cursor cursor = Database.NoticeReadOnce.query("NoticeReadOnce", Database.noticeReadOnce, "UserName=?",
                new String[]{user}, null, null, null);
        if(cursor != null){
            int name_col = cursor.getColumnIndex("NoticeName");
            while (cursor.moveToNext()){
                String name = cursor.getString(name_col);
                if(name == null){
                    continue;
                }
                if(name.equals(noticeName)){
                    return true;    //글을 읽었다.
                }
            }
            cursor.close();
        }
        return false;   //글을 읽지 않았다.
    }

    //해당 유저가 보유하고 있는 돈을 가져온다. 업데이트를 위해!!
    private int takeUserMoney(){
        int money=0;
        Database.UserInfo = Database.myDBHelper.getReadableDatabase();
        Cursor cursor = Database.UserInfo.query("UserInfo", Database.user, "email=?",
                new String[]{user}, null, null, null);
        if(cursor != null){
            int money_col = cursor.getColumnIndex("Money");
            while (cursor.moveToNext()){
                money = cursor.getInt(money_col);
            }
            cursor.close();
        }
        return money;
    }

    //동아리 회원일 경우 true값 반환, 아니면 false 반환
    private boolean belongToOurClub(){
        boolean pandan = false;
        Database.ClubMembersData = Database.clubMembersHelper.getReadableDatabase();
        Cursor cursor = Database.ClubMembersData.query("ClubMembersData",Database.clubMembers,"ClubName=?",
                new String[]{clubName},null,null,null);
        if(cursor != null){
            int members_col = cursor.getColumnIndex("ClubMembers");
            while (cursor.moveToNext()){
                String tmpName = cursor.getString(members_col);
                if(tmpName.equals(user)){
                    pandan = true;
                }
            }
        }
        return pandan;
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.obj.toString());
            switch (msg.what) {
                case ServerThread.Type.SEARCHNOTICE:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String code = jsonObject.getString("code");
                        if (code.equals("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            mData.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String c_club_name = jsonArray.getJSONObject(i).getString("c_club_name");
                                String title = jsonArray.getJSONObject(i).getString("title");
                                String content = jsonArray.getJSONObject(i).getString("content");

                                mData.add(new NoticeData(c_club_name, title, content, null, null));
                            }
                            notice_list_adapter = new Notice_List_Adapter(getContext(), mData);
                            mList.setAdapter(notice_list_adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ServerThread.Type.MAKENOTICE:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String code = jsonObject.getString("code");
                        if(code.equals("200")) {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("SUCCESS")
                                    .setContentText("Make Notice Success!")
                                    .setConfirmText("Confirm")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else if (code.equals("500")) {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("ERROR")
                                    .setContentText("You are not Permitted!")
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
                    break;
            }
        }
    };
}
