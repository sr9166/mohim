package com.example.ydk.ss55.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NoticeDetailsActivity extends AppCompatActivity {

    private EditText noticeName;
    private EditText noticeContent;
    private TextView clubName;
    private Button submitBtn;
    private String ClubName,user;

    private final String TAG = "NOTICEDETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        noticeName = (EditText)findViewById(R.id.noticeName);
        noticeContent = (EditText)findViewById(R.id.noticeContent);
        clubName = (TextView)findViewById(R.id.clubName);
        submitBtn = (Button)findViewById(R.id.submitBtn);

        final Intent intent = getIntent();
        ClubName = intent.getStringExtra("ClubName");
        user = intent.getStringExtra("UserName");
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticeName.getText().length() > 0) {
                    /*
                    Database.NoticeData = Database.noticeHelper.getWritableDatabase();
                    Database.values = new ContentValues();
                    Database.values.put("ClubName", ClubName);
                    Database.values.put("NoticeName", noticeName.getText().toString());
                    Database.values.put("NoticeContent", noticeContent.getText().toString());
                    Database.NoticeData.insert("NoticeData", null, Database.values);
                    Database.noticeHelper.close();
                    Log.d("MakeNoticeData", "\nClubName : " + clubName.getText().toString() +
                            "\nNoticeName : " + noticeName.getText().toString() +
                            "\nNoticeContents : " + noticeContent.getText().toString()
                    );

                    //NoticereadOnce database에도 넣어주기
                    Database.NoticeReadOnce = Database.noticeReadOnceHelper.getWritableDatabase();
                    Database.values = new ContentValues();
                    Database.values.put("ClubName", ClubName);
                    Database.values.put("NoticeName", noticeName.getText().toString());
                    //처음에 false 주기
                    //체크 안되어있는게 false야.
                    //체크 되어있는게 true
                    Database.values.put("NoticeCheck", false);
                    Database.values.put("UserName", user);
                    Database.NoticeReadOnce.insert("NoticeReadOnce",null,Database.values);
                    Database.noticeReadOnceHelper.close();
                    Log.d("MakeNoticeData", "\nClubName : " + ClubName +
                            "\nNoticeName : " + noticeName.getText().toString() +
                            "\nNoticeCheck : " + false +
                            "\nUserName : " + user
                    );
                    */
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("ClubName", ClubName);
                    returnIntent.putExtra("NoticeName", noticeName.getText().toString());
                    returnIntent.putExtra("NoticeContents", noticeContent.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }else {
                    new SweetAlertDialog(NoticeDetailsActivity.this, SweetAlertDialog.WARNING_TYPE)
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
        });
    }
}
