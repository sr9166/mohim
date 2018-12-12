package com.example.ydk.ss55.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClubInfoHelper extends SQLiteOpenHelper{

    public ClubInfoHelper(Context context) {
        //버전이 달라지면 버전 업 해줘야한다.
        //버전 1 : 동아리 이름과 정보만 표시해놓음.
        //버전 2 : 동아리 인원이 몇명인지 UserCount 추가함.
        //버전 3 : 동아리 만든 사람이 누구인지 WhoMakeClub 추가함.
        //버전 4 : 그냥 업데이트 새로고침 함
        //버전 5 : 다운그레이드 함부로 할 수 없다는걸 알았음.. 화이팅
        //버전 6 : null 값이 생기는 에러가 발생한느데.. 왜그럴까   MainActivity에 new Helper 해줘야 합니다.. 해결띠
        super(context,"ClubData",null,6);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE ClubData(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ClubName TEXT, ClubInfo TEXT, WhoMakeClub TEXT, UserCount INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ClubData;");
        onCreate(sqLiteDatabase);
    }
}
