package com.example.ydk.ss55.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ydk.ss55.R;
import com.example.ydk.ss55.model.CreditText;
import com.example.ydk.ss55.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import su.levenetc.android.textsurface.TextSurface;

public class CreditActivity extends AppCompatActivity {
    private TextSurface textSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        Log.d("CREDIT", "START");
        textSurface = (TextSurface) findViewById(R.id.text_surface);

        textSurface.postDelayed(new Runnable() {
            @Override public void run() {
                CreditText.play(textSurface);
                new Handler().postDelayed(new creditHandler(),7300);
            }
        }, 1000);
    }

    private class creditHandler implements  Runnable{
        @Override
        public void run() {
            CreditActivity.this.finish();
        }
    }
}