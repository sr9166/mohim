package com.example.ydk.ss55.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.ydk.ss55.R;
import com.example.ydk.ss55.activity.ClubStationActivity;

import java.util.Calendar;

public class NoticeMakeActivity extends AppCompatActivity {
    private EditText voteName;
    private EditText detailOne;
    private EditText detailTwo;
    private Button btnSubmit,timeLimit;
    private String mStrDate,mStrTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_make);

        voteName = (EditText)findViewById(R.id.voteName);
        detailOne = (EditText)findViewById(R.id.details_one);
        detailTwo = (EditText)findViewById(R.id.details_two);
        timeLimit = (Button)findViewById(R.id.timeLimit);
        timeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("check","잘 들어왔어요~");
//                //TODO : 구조를 바꿔야 함... fragment가 같은 activity 안에 있어야 함.
//                Bundle bundle = new Bundle();
//                bundle.putString("VoteName",voteName.getText().toString());
//                bundle.putString("VoteName",detailOne.getText().toString());
//                bundle.putString("VoteName",detailTwo.getText().toString());
//
//                ClubVote clubVote = new ClubVote();
//
//                clubVote.setArguments(bundle);
//
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,clubVote).commit();
//
//                finish();
                Intent intent = new Intent(getApplicationContext(),ClubStationActivity.class);
                intent.putExtra("VoteName",voteName.getText().toString());
                intent.putExtra("VoteDetail1",detailOne.getText().toString());
                intent.putExtra("VoteDetail2",detailTwo.getText().toString());
                startActivity(intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            mStrDate = String.format("%d년 %d월 %d일",i,i1+1,i2);
            updateResult();
        }
    };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            mStrTime = String.format("%d시 %d분",i,i1);
            updateResult();
        }
    };
    private void updateResult(){
        TextView textResult = (TextView)findViewById(R.id.textResult);
        textResult.setText("날짜 : " + mStrDate + "\n" + "시간 : " + mStrTime);
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
}

