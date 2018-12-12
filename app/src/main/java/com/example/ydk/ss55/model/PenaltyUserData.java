package com.example.ydk.ss55.model;

public class PenaltyUserData {
    private String Penalty_User;
    private String Penalty;

    public PenaltyUserData(String penalty_User, String penalty) {
        Penalty_User = penalty_User;
        Penalty = penalty;
    }


    public String getPenalty_User() {
        return Penalty_User;
    }

    public void setPenalty_User(String penalty_User) {
        Penalty_User = penalty_User;
    }

    public String getPenalty() {
        return Penalty;
    }

    public void setPenalty(String penalty) {
        Penalty = penalty;
    }
}
