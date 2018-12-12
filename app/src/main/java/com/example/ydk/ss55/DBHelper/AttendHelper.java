package com.example.ydk.ss55.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AttendHelper extends SQLiteOpenHelper {

    //버전 2 : 참불 투포를 참여 코드 입력하는 방식으로 바꿈.
    //버전 3 : 무슨 에러일까.. null값 생김.. 뭐지 이거?
    //버전 4 : null값 이거 이해 못해서 하나 더 올림..
    //버전 5 : null 원인 찾음.. Attend -> AttendData 로 수정함.. 데이터 이름 값 틀렸음.. Database
    //    //          랑 AttendHelper의 관계 잘 살펴보시길..
    //버전 1 : 정말 중요한거.. MainActivity 에 가서 new AttendHelper 해줘야 한다... 이거 아니면 null 에러 뜬다.
    //버전 2: clubName column 추가하기
    //버전 3 : TimeLimit 을 Date와 Time으로 나누어 저장.
    //버전 4 : 데이터 오류 수정으로 싹 갈아엎음
    //버전 5 : 다시한번 엎자.. 중복 거부 AttendName 으로 설정,,, 오 구조 안바꾸는 이상 버전 안올려도 상관 없는듯
    public AttendHelper(Context context){
        super(context,"AttendData.db",null,5);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE AttendData(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"ClubName TEXT, AttendName TEXT,Date TEXT,Time TEXT,AttendCode TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS AttendData;");
        onCreate(sqLiteDatabase);
    }
}
