package com.example.ydk.ss55.fragment;

import android.app.Application;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.KakaoAPI;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.activity.KakaoPayReadyActivity;
import com.example.ydk.ss55.activity.StationActivity;
import com.example.ydk.ss55.model.ClubData;
import com.example.ydk.ss55.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

public class MyInformationActivity extends Fragment {

    private TextView userId, userName, userMoney;
    private Button btn_addmoney;
    private KakaoAPI kakaoAPI = KakaoAPI.getInstacne();
    private ServerThread serverThread = ServerThread.getInstacne();
    private final String TAG = "MYINFORMATION";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_my_information, container, false);
        kakaoAPI.setFragmentHandler(fragmentHandler);
        userId = (TextView)rootView.findViewById(R.id.userId);
        userName = (TextView) rootView.findViewById(R.id.userName);
        userMoney = (TextView) rootView.findViewById(R.id.userMoney);
        userId.setText(User.getEmail());
        userName.setText(User.getUname());
        userMoney.setText(User.getCyber_money());

        btn_addmoney = (Button) rootView.findViewById(R.id.btn_addmoney);
        btn_addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaoAPI.getThHandler().sendEmptyMessage(KakaoAPI.Type.READY);
            }
        });
        /*
        Database.UserInfo = Database.myDBHelper.getReadableDatabase();
        Cursor cursor = Database.UserInfo.query("UserInfo", Database.user, "email=?",
                new String[]{tmp}, null, null, null);
        if (cursor != null) {
            myMoney.setText("");
            int money_col = cursor.getColumnIndex("Money");
            while (cursor.moveToNext()) {
                int money = cursor.getInt(money_col);
                Log.d("Money", "내가 보유한 돈 : " + String.valueOf(money));
                myMoney.setText(" " + String.valueOf(money) + " 원");
            }
            cursor.close();
        }
        Database.myDBHelper.close();
        */
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "get intent success!\t" + RESULT_OK  +"\t" + resultCode);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            int money = Integer.valueOf(userMoney.getText().toString());
            money += 10000;
            userMoney.setText(String.valueOf(money));
            serverThread.setFragmentHandler(fragmentHandler);
            serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.CYBERMONEY);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private Handler fragmentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KakaoAPI.Type.READY:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String url = object.getString("next_redirect_mobile_url");
                        String tid = object.getString("tid");
                        Intent intent = new Intent(getContext(), KakaoPayReadyActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("tid", tid);
                        kakaoAPI.setTid(tid);
                        kakaoAPI.getThHandler().sendEmptyMessage(KakaoAPI.Type.SYNC);
                        startActivityForResult(intent, 0);
                    } catch (JSONException e ) {
                        e.printStackTrace();
                    }
                    break;
                case KakaoAPI.Type.SYNC:
                    break;
                case KakaoAPI.Type.APPROVE:
                    break;
                case ServerThread.Type.CYBERMONEY:
                    Log.d(TAG, msg.obj.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String code = jsonObject.getString("code");
                        if(code.equals("200")) {
                            JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
                            String email = data.getString("email");
                            String uname = data.getString("uname");
                            String pwd = data.getString("pwd");
                            String cyber_money = data.getString("cyber_money");
                            Log.d(TAG, data.toString());
                            User.setEmail(email);
                            User.setPwd(pwd);
                            User.setUname(uname);
                            User.setCyber_money(cyber_money);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
