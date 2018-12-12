package com.example.ydk.ss55.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MakeNewGroupActivity extends AppCompatActivity {

    private final String TAG = "MakeNewGroupActivity";
    private EditText club_name;
    private EditText club_introduction;
    private String whoMake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_group);
        club_name = (EditText) findViewById(R.id.groupName);
        club_introduction = (EditText) findViewById(R.id.group_Introduction);

        Intent receiver = getIntent();
        whoMake = receiver.getStringExtra("WhoMakeClub");
        Log.d("kkk", whoMake + " make");
    }

    public void MakeNewGroup_Btn(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            //제출 클릭 시 처리하는 인텐트..
            case R.id.submit:
                //TODO : 나중에 스키마 더 추가할꺼임.. 지금은 두개만 만들자.. 라디오버튼도 만들자..
                if (club_name.getText().length() > 0 && club_introduction.getText().length() > 0) {
                    //동아리 이름에 중복이 있는지 확인해보는 코드
                    if (nameDoubleCheck()) {
                        /*
                        int init = 0;
                        Database.ClubData = Database.clubInfoHelper.getWritableDatabase();
                        Database.values = new ContentValues();
                        Database.values.put("ClubName", club_name.getText().toString());
                        Database.values.put("ClubInfo", club_introduction.getText().toString());
                        Database.values.put("UserCount", init);
                        Database.values.put("WhoMakeClub", whoMake);
                        Database.ClubData.insert("ClubData", null, Database.values);
                        Database.clubInfoHelper.close();
                        Log.d("MakeClubData", "CLubName : " + club_name.getText().toString() +
                                "\nClubInfo : " + club_introduction.getText().toString() +
                                "\nWhoMakeClub : " + whoMake +
                                "\n동아리 인원 수 : " + init
                        );
                        */
                        Intent intent = new Intent();
                        intent.putExtra("ClubName", club_name.getText().toString());
                        intent.putExtra("ClubInfo", club_introduction.getText().toString());
                        intent.putExtra("UserCount", 1);
                        intent.putExtra("WhoMakeClub", whoMake);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }else {
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING")
                            .setContentText("Please fill in the blanks")
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
    }

    private boolean nameDoubleCheck() {
        if(true) return true;
        String tmp = club_name.getText().toString();
        Database.ClubData = Database.clubInfoHelper.getReadableDatabase();
        Cursor cursor = Database.ClubData.query("ClubData", Database.club, null,
                null, null, null, null);
        if (cursor != null) {
            int name_col = cursor.getColumnIndex("ClubName");
            while (cursor.moveToNext()) {
                String name = cursor.getString(name_col);
                if (tmp.equals(name)) {
                    Toast.makeText(getApplicationContext(), "중복된 이름의 동아리가 존재합니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}

