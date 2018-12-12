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
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Sign_upActivity extends AppCompatActivity {

    private EditText sign_up_email, sign_up_password,sign_up_password2, sign_up_user_name;
    private Button sign_up_submit, sign_up_back;
    private ServerThread serverThread = ServerThread.getInstacne();
    private String TAG = "Signup_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        sign_up_email = (EditText)findViewById(R.id.sign_up_email);
        sign_up_password = (EditText)findViewById(R.id.sign_up_password);
        sign_up_password2 = (EditText)findViewById(R.id.sign_up_password2);
        sign_up_user_name = (EditText)findViewById(R.id.sign_up_user_name);

        sign_up_submit = (Button)findViewById(R.id.sign_up_submit);
        sign_up_back = (Button)findViewById(R.id.sign_up_back);
    }

    public void sign_up(View v){
        switch (v.getId()){
            case R.id.sign_up_back:
                finish();
                break;
            case R.id.sign_up_submit:
                if(sign_up_email.getText().length()>0 && sign_up_password.getText().length() > 0 && sign_up_password2.getText().length()>0){
                    if(sign_up_password.getText().toString().equals(sign_up_password2.getText().toString())){
                        /*
                        Database.UserInfo = Database.myDBHelper.getWritableDatabase();
                        Database.values = new ContentValues();
                        Database.values.put("email",sign_up_email.getText().toString());
                        Database.values.put("password",sign_up_password.getText().toString());
                        Database.values.put("Money",10000);
                        Database.values.put("Penalty",0);
                        Database.UserInfo.insert("UserInfo",null,Database.values);
                        Database.myDBHelper.close();
                        Log.d("onSuccess",sign_up_password.getText().toString() + 10000);
                        */
                        String email = sign_up_email.getText().toString();
                        String pw = sign_up_password.getText().toString();
                        String name = sign_up_user_name.getText().toString();
                        serverThread.setRegister(email, pw, name);
                        serverThread.setFragmentHandler(mHandler);
                        serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.REGISTER);
                    }else{
                        Log.d("test",sign_up_password.getText().toString()+ "-> " + sign_up_password2.getText().toString());
                        new SweetAlertDialog(Sign_upActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR")
                                .setContentText("Please check your password")
                                .setConfirmText("Confirm")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                        break;
                    }
                }else {
                    new SweetAlertDialog(Sign_upActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText("Please fill in the blanks")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                    break;
                }
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.obj.toString());
            try {
                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                if(jsonObject.getString("code").equals("200")) {
                    new SweetAlertDialog(Sign_upActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("SUCCESS")
                            .setContentText("Your Register is completed!")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
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



