package com.example.ydk.ss55.model;

public class ActiveUserData {
    private String active_user;
    private int attendCount;

    public ActiveUserData(String active_user, int attendCount) {
        this.active_user = active_user;
        this.attendCount = attendCount;
    }

    public String getActive_user() {
        return active_user;
    }

    public void setActive_user(String active_user) {
        this.active_user = active_user;
    }

    public int getAttendCount() {
        return attendCount;
    }

    public void setAttendCount(int attendCount) {
        this.attendCount = attendCount;
    }
}
