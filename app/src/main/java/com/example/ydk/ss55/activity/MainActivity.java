package com.example.ydk.ss55.activity;

import android.content.Intent;
import android.database.Cursor;
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

import com.example.ydk.ss55.DBHelper.AttendHelper;
import com.example.ydk.ss55.DBHelper.AttendReadOnceHelper;
import com.example.ydk.ss55.DBHelper.ClubInfoHelper;
import com.example.ydk.ss55.DBHelper.ClubMembersHelper;
import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.DBHelper.NoticeReadOnceHelper;
import com.example.ydk.ss55.DBHelper.NoticeHelper;
import com.example.ydk.ss55.DBHelper.MyDBHelper;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {
    String TAG = "Main_Acitivty";
    ServerThread serverThread = ServerThread.getInstacne();
    EditText edit_email,pwww;
    TextView test;
    Button testbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////
        //정말 중요한거..!!!!!
        ////////
        Database.myDBHelper = new MyDBHelper(this);
        Database.clubInfoHelper = new ClubInfoHelper(this);
        Database.attendHelper = new AttendHelper(this);
        Database.noticeHelper = new NoticeHelper(this);
        Database.clubMembersHelper = new ClubMembersHelper(this);
        Database.attendReadOnceHelper = new AttendReadOnceHelper(this);
        Database.noticeReadOnceHelper = new NoticeReadOnceHelper(this);

        ///////
        ///////
        ///////

        edit_email = (EditText) findViewById(R.id.email);
        pwww = (EditText)findViewById(R.id.editText2);

        Button login = (Button)findViewById(R.id.login);
        Button sign_up = (Button)findViewById(R.id.sign_up);
        Button credit = (Button)findViewById(R.id.creditButton);
    }
    public void MainAc(View v){
        switch (v.getId()){
            case R.id.login:
                if(edit_email.getText().length()>0 && pwww.getText().length()>0){
                    /*
                    //데이터베이스에서 이름 검색
                    Database.UserInfo = Database.myDBHelper.getReadableDatabase();
                    String email_ = edit_email.getText().toString();
                    String pw = pwww.getText().toString();
                    Database.cursor = Database.UserInfo.query("UserInfo",Database.user,"email=?",
                            new String[]{ email_ },null,null,null);
                    if(Database.cursor != null){
                        passwordCheck(Database.cursor);
                    }
                    Database.cursor.close();
                    */
                    String user_email = edit_email.getText().toString();
                    String user_pw = pwww.getText().toString();
                    serverThread.setFragmentHandler(mHandler);
                    serverThread.setLogin(user_email, user_pw);
                    serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.LOGIN);
                }
                else {
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
                Database.myDBHelper.close();
                break;
            case R.id.sign_up:
                Intent intent = new Intent(getApplicationContext(),Sign_upActivity.class);
                startActivity(intent);
                break;
            case R.id.creditButton:
                Intent intent1 = new Intent(getApplicationContext(), CreditActivity.class);
                startActivity(intent1);
                Log.d(TAG, "CREDIT BUTTON CLICK!");
                break;
        }
    }

    private void passwordCheck(Cursor cursor){
//        test.setText("");
        int name_col = Database.cursor.getColumnIndex("email");
        int password_col = Database.cursor.getColumnIndex("password");
        while (Database.cursor.moveToNext()){
            String email = Database.cursor.getString(name_col);
            String password = Database.cursor.getString(password_col);
            if(password.equals(pwww.getText().toString())) {
                Intent intent = new Intent(getApplicationContext(),StationActivity.class);
                intent.putExtra("email_To_HelloMain",edit_email.getText().toString());
                intent.putExtra("pw",pwww.getText().toString());
                startActivity(intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP));
            }else
            {
                Toast.makeText(getApplicationContext(),"아이디, 비밀번호를 채워주세요",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == ServerThread.Type.ERROR) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText("Please enter a valid ID and Password")
                        .setConfirmText("Confirm")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                return;
            }
            Log.d(TAG, msg.obj.toString());
            try {
                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                String code = jsonObject.getString("code");
                if(code.equals("200")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    String email = data.getString("email");
                    String uname = data.getString("uname");
                    String pwd = data.getString("pwd");
                    String cyber_money = data.getString("cyber_money");
                    Log.d(TAG, data.toString());
                    User.setEmail(email);
                    User.setPwd(pwd);
                    User.setUname(uname);
                    User.setCyber_money(cyber_money);
                    final Intent intent = new Intent(getApplicationContext(), StationActivity.class);
                    intent.putExtra("email_To_HelloMain", email);
                    intent.putExtra("pw", pwd);
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("SUCCESS")
                            .setContentText("Your Login is completed!")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    startActivity(intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP));
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
