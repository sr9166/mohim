package com.example.ydk.ss55.DBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {
    public static SQLiteDatabase UserInfo; //유저 정보를 저장하는 데이터베이스
    public static SQLiteDatabase ClubData; //모임 정보를 저장하는 데이터베이스
    public static SQLiteDatabase AttendData;   //출석 정보를 저장하는 데이터베이스
    public static SQLiteDatabase NoticeData;   //공지사항 정보를 저장하는 데이터베이스
    public static SQLiteDatabase ClubMembersData;  //모임에 해당하는 맴버를 저장하는
    public static SQLiteDatabase AttendReadOnce;     //한번 읽었던 참석에 대해 처리하는 데이터베이스, 이걸로 활동 많이한 사람 list도 뽑을 수 있음
    public static SQLiteDatabase NoticeReadOnce;     //한번 읽었던 참석에 대해 처리하는 데이터베이스, 이걸로 활동 많이한 사람 list도 뽑을 수 있음

    public static MyDBHelper myDBHelper;   //유저 정보 데이터베이스 도와주는 장치
    public static ClubInfoHelper clubInfoHelper;   //모임 정보 데이터베이스 도와주는 장치
    public static AttendHelper attendHelper;   //출첵 정보 데이터베이스 도와주는 장치
    public static NoticeHelper noticeHelper;   //공지사항 정보 데이터베이스 도와주는 장치
    public static ClubMembersHelper clubMembersHelper;     //모임 가입한 사람들 정보 데이터베이스 도와주는 장치
    public static AttendReadOnceHelper attendReadOnceHelper;   //한번 읽었던 출석에 대한 정보, 활동 많이한 사람 정보 데이터베이스 도와주는 장치
    public static NoticeReadOnceHelper noticeReadOnceHelper;   //한번 읽었던 공지에 대한 정보, 활동 많이한 사람 정보 데이터베이스 도와주는 장치

    public static Cursor cursor;
    public static ContentValues values;

    public static String[] user={"_id","email","password","Money","Penalty"};  //유저 스키마
    public static String[] club={"_id","ClubName","ClubInfo","WhoMakeClub","UserCount"};    //모임의 스키마
    public static String[] attend={"_id","ClubName","AttendName","Date","Time","AttendCode"};    //출석체크 스키마
    public static String[] notice={"_id","ClubName","NoticeName","NoticeContent"};     //공지 스키마
    public static String[] clubMembers = {"_id","ClubName","ClubMembers","Penalty"};         //클럽 가입한 사람 스키마
    public static String[] attendReadOnce = {"_id","ClubName","UserName","AttendName","AttendCheck"}; //읽었던 출석에 대한 정보 스키마
    public static String[] noticeReadOnce = {"_id","ClubName","UserName","NoticeName","NoticeCheck"}; //읽었던 공지에 대한 정보 스키마

}