package com.example.ydk.ss55.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AttendReadOnceHelper extends SQLiteOpenHelper{

    public AttendReadOnceHelper(Context context) {
        super(context,"AttendReadOnce",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE AttendReadOnce(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ClubName TEXT, UserName TEXT, AttendName TEXT, AttendCheck boolean);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS AttendReadOnce;");
        onCreate(sqLiteDatabase);
    }
}
