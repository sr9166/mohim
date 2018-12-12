package com.example.ydk.ss55.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private static String email;
    private static String uname;
    private static String pwd;
    private static String cyber_money;
    static public JSONObject toJSson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", getEmail());
            jsonObject.put("uname", getUname());
            jsonObject.put("pwd", getPwd());
            jsonObject.put("cyber_money", getCyber_money());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public User(String email, String uname, String pwd, String cyber_money) {
        setEmail(email);
        setUname(uname);
        setPwd(pwd);
        setCyber_money(cyber_money);
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getUname() {
        return uname;
    }

    public static void setUname(String uname) {
        User.uname = uname;
    }

    public static String getPwd() {
        return pwd;
    }

    public static void setPwd(String pwd) {
        User.pwd = pwd;
    }

    public static String getCyber_money() {
        return cyber_money;
    }

    public static void setCyber_money(String cyber_money) {
        User.cyber_money = cyber_money;
    }
}
