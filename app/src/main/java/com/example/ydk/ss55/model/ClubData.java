package com.example.ydk.ss55.model;

import android.widget.ImageView;

public class ClubData {

    private int imageView;
    private String ClubName;
    private String ClubInfo;
    private String u_email;
    private int user_count;
    private int penalty = 0;

    public ClubData(int imageView, String clubName, String ClubInfo, int penalty) {
        this.imageView = imageView;
        ClubName = clubName;
        this.ClubInfo = ClubInfo;
        this.penalty = penalty;
    }

    public ClubData(int imageView, String clubName, String clubInfo, String u_email, int user_count, int penalty) {
        this.imageView = imageView;
        ClubName = clubName;
        ClubInfo = clubInfo;
        this.u_email = u_email;
        this.user_count = user_count;
        this.penalty = penalty;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getClubName() {
        return ClubName;
    }

    public void setClubName(String clubName) {
        ClubName = clubName;
    }

    public String getDuringTime() {
        return ClubInfo;
    }

    public void setDuringTime(String ClubInfo) {
        this.ClubInfo = ClubInfo;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public String getClubInfo() {
        return ClubInfo;
    }

    public void setClubInfo(String clubInfo) {
        ClubInfo = clubInfo;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public int getUser_count() {
        return user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }

    @Override
    public String toString() {
        return "ClubData{" +
                "ClubName='" + ClubName + '\'' +
                ", ClubInfo='" + ClubInfo + '\'' +
                ", u_email='" + u_email + '\'' +
                ", user_count=" + user_count +
                ", penalty=" + penalty +
                '}';
    }
}
