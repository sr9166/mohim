package com.example.ydk.ss55.model;

public class NoticeData {
    private String ClubName;
    private String NoticeName;
    private String NoticeConent;
    private String Date;
    private String Time;

    public NoticeData(String clubName, String noticeName, String noticeConent, String date, String time) {
        ClubName = clubName;
        NoticeName = noticeName;
        NoticeConent = noticeConent;
        Date = date;
        Time = time;
    }

    public String getClubName() {
        return ClubName;
    }

    public void setClubName(String clubName) {
        ClubName = clubName;
    }

    public String getNoticeName() {
        return NoticeName;
    }

    public void setNoticeName(String noticeName) {
        NoticeName = noticeName;
    }

    public String getNoticeConent() {
        return NoticeConent;
    }

    public void setNoticeConent(String noticeConent) {
        NoticeConent = noticeConent;
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

    public String dateFormat() {
        char[] datearray = Date.toCharArray();
        char[] timearray = Time.toCharArray();
        String temp = "" + datearray[0] + datearray[1] + datearray[2] + datearray[3] + "-"
                + datearray[4] + datearray[5] + "-" + datearray[6] + datearray[7] + "T"
                + timearray[0] + timearray[1] + ":" + timearray[2] + timearray[3];
        return temp;
    }

    @Override
    public String toString() {
        return "NoticeData{" +
                "ClubName='" + ClubName + '\'' +
                ", NoticeName='" + NoticeName + '\'' +
                ", NoticeConent='" + NoticeConent + '\'' +
                ", Date='" + Date + '\'' +
                ", Time='" + Time + '\'' +
                '}';
    }
}
