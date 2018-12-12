package com.example.ydk.ss55.model;

public class PenaltyInfoData {
    private String clubName;
    private int Penalty;


    public PenaltyInfoData(String clubName, int penalty) {
        this.clubName = clubName;
        Penalty = penalty;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public int getPenalty() {
        return Penalty;
    }

    public void setPenalty(int penalty) {
        Penalty = penalty;
    }
}