package com.example.ydk.ss55.model;

import java.util.Calendar;

public class AttendData {
    private String ClubName;
    private String AttendName;
    private String AttendCode;
    private String Date;
    private String Time;
    private String Unread_email_list;
    private int id;


    public AttendData(String clubName, String attendName, String attendCode, String date, String time) {
        ClubName = clubName;
        AttendName = attendName;
        AttendCode = attendCode;
        Date = date;
        Time = time;
    }

    public AttendData(String clubName, String attendName, String attendCode, String date, String time, String unread_email_list, int id) {
        ClubName = clubName;
        AttendName = attendName;
        AttendCode = attendCode;
        Date = date;
        Time = time;
        Unread_email_list = unread_email_list;
        this.id = id;
    }

    public String getClubName() {
        return ClubName;
    }

    public void setClubName(String clubName) {
        ClubName = clubName;
    }

    public String getAttendName() {
        return AttendName;
    }

    public void setAttendName(String attendName) {
        AttendName = attendName;
    }

    public String getAttendCode() {
        return AttendCode;
    }

    public void setAttendCode(String attendCode) {
        AttendCode = attendCode;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUnread_email_list() {
        return Unread_email_list;
    }

    public void setUnread_email_list(String unread_email_list) {
        Unread_email_list = unread_email_list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String dateFormat() {
        char[] datearray = Date.toCharArray();
        char[] timearray = Time.toCharArray();
        String temp = "" + datearray[0] + datearray[1] + datearray[2] + datearray[3] + "-"
                + datearray[4] + datearray[5] + "-" + datearray[6] + datearray[7] + "T"
                + timearray[0] + timearray[1] + ":" + timearray[2] + timearray[3];
        return temp;
    }

    public Calendar getCalendar() {
        char[] datearray = Date.toCharArray();
        char[] timearray = Time.toCharArray();
        int year = Integer.valueOf("" + datearray[0] + datearray[1] + datearray[2] + datearray[3]);
        int month = Integer.valueOf("" + datearray[4] + datearray[5]);
        int day = Integer.valueOf("" + datearray[6] + datearray[7]);
        int hour = Integer.valueOf("" + timearray[0] + timearray[1]);
        int minute = Integer.valueOf("" + timearray[2] + timearray[3]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute,0);
        return calendar;
    }
}
