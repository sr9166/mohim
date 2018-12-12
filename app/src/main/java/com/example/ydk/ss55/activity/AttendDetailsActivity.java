package com.example.ydk.ss55.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AttendDetailsActivity extends AppCompatActivity {

    private EditText attendName;
    private EditText attendCode;
    private Button btnSubmit,timeLimit;
    private String mStrDate,mStrTime,StringDate,StringTime,finalDate,finalTime,ClubName,user;
    private TextView Date,Time;
    private String year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_details);

        Intent intent = getIntent();
        ClubName = intent.getStringExtra("ClubName");
        user = intent.getStringExtra("UserName");
        attendName = (EditText)findViewById(R.id.voteName);
        attendCode = (EditText)findViewById(R.id.attendPw);
        Date = (TextView)findViewById(R.id.Date);
        Time = (TextView)findViewById(R.id.Time);
        timeLimit = (Button)findViewById(R.id.timeLimit);
        timeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        btnSubmit = (Button)findViewById(R.id.btnSubmit);


        //제출하게 되면 AttendData에 모임 이름, 해당 모임 참석 이름, 날자, 시간, 입장 비밀번호가 쌓이게 된다.
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if(nameDoubleCheck()) {
                    if (attendName.getText().length() > 0) {
                        Database.ClubData = Database.clubInfoHelper.getWritableDatabase();
                        Database.values = new ContentValues();
                        Database.values.put("ClubName", ClubName);
                        Database.values.put("AttendName", attendName.getText().toString());
                        Database.values.put("Date", finalDate);
                        Database.values.put("Time", finalTime);
                        Database.values.put("AttendCode", attendCode.getText().toString());
                        Database.AttendData.insert("AttendData", null, Database.values);
                        Database.clubInfoHelper.close();
                        Log.d("MakeAttendData", "\nAttendName : " + attendName.getText().toString() +
                                "\nDate : " + finalDate +
                                "\nTime : " + finalTime +
                                "\n입장 비밀번호 : " + attendCode.getText().toString()
                        );

                        //AttendreadOnce database에도 넣어주기
                        Database.AttendReadOnce = Database.attendReadOnceHelper.getWritableDatabase();
                        Database.values = new ContentValues();
                        Database.values.put("ClubName", ClubName);
                        Database.values.put("UserName", user);
                        Database.values.put("AttendName", attendName.getText().toString());
                        //처음에 false 주기
                        //체크 안되어있는게 false야.
                        //체크 되어있는게 true
                        Database.values.put("AttendCheck", false);
                        Database.AttendReadOnce.insert("AttendReadOnce",null,Database.values);
                        Database.attendReadOnceHelper.close();

//                        Calender calender = new Calendar();

                        Log.d("MakeAttendData", "\nClubName : " + ClubName +
                                "\nAttendName : " + attendName.getText().toString() +
                                "\nAttendCheck : " + false +
                                "\nUserName : " + user
                        );
                        Intent intent = new Intent();
                        intent.putExtra("ClubName", ClubName);
                        intent.putExtra("AttendName", attendName.getText().toString());
                        intent.putExtra("AttendCode", attendCode.getText().toString());
                        intent.putExtra("Date", finalDate);
                        intent.putExtra("Time", finalTime);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "빈 곳을 다 채워주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                */
                if(attendName.getText().length() > 0 && attendCode.getText().length() > 0 && StringDate.length() > 0 && StringTime.length() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("ClubName", ClubName);
                    intent.putExtra("AttendName", attendName.getText().toString());
                    intent.putExtra("AttendCode", attendCode.getText().toString());
                    intent.putExtra("Date", StringDate);
                    intent.putExtra("Time", StringTime);
                    setResult(RESULT_OK, intent);
                    Log.d("ATTENDDETAIL", "" + StringDate +  "\t" + StringTime);
                    finish();
                }
                else {
                    new SweetAlertDialog(AttendDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
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
                }
            }
        });
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            finalDate = String.valueOf(i);

            if(i1==9){
                finalDate += "10";
            }
            if(i1<9){
                finalDate += "0"+String.valueOf(i1+1);
            }
            else {
                finalDate += String.valueOf(i1+1);
            }

            if(i2<10){
                finalDate += "0"+String.valueOf(i2);
            }
            else{
                finalDate += String.valueOf(i2);
            }


            mStrDate = String.valueOf(i) + String.valueOf(i1) + String.valueOf(i2);

            mStrDate = String.format("%d년 %d월 %d일",i,i1+1,i2);
            StringDate = String.format("%d%s%s",i,i1+1 < 10 ? "0" + i1+1 : ""+i1+1,i2 < 10 ? "0" + i2 : "" + i2); // SimpleDateFormat 생성자에 전달되는 형식과 일치해야 함
            year = String.valueOf(i); month = String.valueOf(i1+1); day = String.valueOf(i2);
            System.out.println("STR DATE : " + mStrDate + "\tStringDate : " + StringDate);
            updateResult();
        }
    };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

            finalTime="";
            if(i<10){
                finalTime += "0"+i;
            }else{
                finalTime +=i;
            }
            if(i1<10){
                finalTime +="0"+i1;
            }else{
                finalTime +=i1;
            }
            mStrTime = String.format("%d시 %d분",i,i1);
            StringTime = String.format("%s%s",i < 10 ? "0"+i : ""+i,i1 < 10 ? "0"+i1 : i1);
            hour = String.valueOf(i); minute = String.valueOf(i1);
            updateResult();
        }
    };
    private void updateResult(){
        TextView Date = (TextView)findViewById(R.id.Date);
        TextView Time = (TextView)findViewById(R.id.Time);
        Date.setText("날짜 : " + mStrDate + "\n" + "시간 : " + mStrTime);
    }
    private void setDate(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(this,mTimeSetListener,hour,minute,true).show();
        new DatePickerDialog(this,mDateSetListener,year,month,day).show();
    }

    public String getmStrDate() {
        return mStrDate;
    }

    public void setmStrDate(String mStrDate) {
        this.mStrDate = mStrDate;
    }

    public String getmStrTime() {
        return mStrTime;
    }

    public void setmStrTime(String mStrTime) {
        this.mStrTime = mStrTime;
    }

    private boolean nameDoubleCheck() {
        String tmp = attendName.getText().toString();
        Database.AttendData = Database.attendHelper.getReadableDatabase();
        Cursor cursor = Database.AttendData.query("AttendData", Database.attend, "ClubName=?",
                new String[]{ClubName}, null, null, null);
        if (cursor != null) {
            int name_col = cursor.getColumnIndex("AttendName");
            while (cursor.moveToNext()) {
                String name = cursor.getString(name_col);
                if (tmp.equals(name)) {
                    Toast.makeText(getApplicationContext(), "중복된 이름의 공지가 존재합니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            cursor.close();
            return true;
        }
        return false;
    }
}
