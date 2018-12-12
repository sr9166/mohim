package com.example.ydk.ss55.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClubMembersHelper extends SQLiteOpenHelper {

    // ver2. 페널티 항목 추가

    public ClubMembersHelper(Context context){
        super(context,"ClubMembersData.db",null,2);
    }

//    static String[] clubMembers = {"_id","ClubName","ClubMembers"};         //클럽 가입한 사람 스키마

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE ClubMembersData(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ClubName TEXT,ClubMembers TEXT,Penalty INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ClubMembersData;");
        onCreate(sqLiteDatabase);
    }
}
