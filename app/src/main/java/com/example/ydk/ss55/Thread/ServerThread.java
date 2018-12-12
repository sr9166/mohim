package com.example.ydk.ss55.Thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.ydk.ss55.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServerThread extends Thread {
    private String TAG = "ServerThread";
    private String user_email;
    private String user_pw;
    private String user_name;
    private String clubName;
    private String clubInfo;
    private String title;
    private String deadline;
    private String code;
    private String content;
    private int id;

    private static ServerThread instacne = null;

    private ServerThread() {
    } //private 생성자

    public static ServerThread getInstacne() {
        if (instacne == null) {
            instacne = new ServerThread();
            instacne.setDaemon(true);
            instacne.start();
        }
        return instacne;
    }

    private String myResult = "";
    private Handler thHandler;  //쓰레드 핸들러
    private Handler fragmentHandler;    //프래그먼트 핸들러

    public Handler getThHandler() {
        return thHandler;
    }

    public void setFragmentHandler(Handler fragmentHandler) {
        this.fragmentHandler = fragmentHandler;
    }

    public void run() {
        Looper.prepare();
        thHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Message retMsg = new Message();

                switch (msg.what) {
                    case Type.REGISTER:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/users/register");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("POST");

                            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                            StringBuffer buffer = new StringBuffer();
                            buffer.append("email").append("=").append(user_email).append("&");
                            buffer.append("password").append("=").append(user_pw).append("&");
                            buffer.append("name").append("=").append(user_name);

                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(buffer.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.LOGIN:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/users/login");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("POST");

                            http.setRequestProperty("Accept", "application/json");
                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("email", user_email);
                                jsonObject.put("password", user_pw);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.LOGIN;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                            retMsg.what = Type.ERROR;
                            fragmentHandler.sendMessage(retMsg);
                        }
                        break;
                    case Type.ADDCLUB:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/clubs/make");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("POST");

                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user", User.toJSson());
                                jsonObject.put("clubName", clubName);
                                jsonObject.put("clubInfo", clubInfo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            StringBuffer buffer = new StringBuffer();
                            buffer.append(jsonObject.toString());

                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(buffer.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.ADDCLUB;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.SEARCHCLUB:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/clubs/search");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.SEARCHCLUB;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.SEARCHMYCLUB:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/users/clubs");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("POST");

                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user", User.toJSson());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG + "SEARCHMYCLUB", jsonObject.toString());
                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.SEARCHMYCLUB;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.REGISTERCLUB:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/clubmembers/add");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("PUT");

                            http.setRequestProperty("Accept", "application/json");
                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user", User.toJSson());
                                jsonObject.put("clubName", clubName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG,jsonObject.toString());

                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.REGISTERCLUB;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.CYBERMONEY:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/users/cyberMoney");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("PUT");

                            http.setRequestProperty("Accept", "application/json");
                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user", User.toJSson());
                                jsonObject.put("chargeMoney", 10000);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG,jsonObject.toString());

                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.CYBERMONEY;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.MAKEATTEND:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/users/attend/make");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("POST");

                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user", User.toJSson());
                                jsonObject.put("clubName", clubName);
                                jsonObject.put("title", title);
                                jsonObject.put("deadline", deadline);
                                jsonObject.put("code", code);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, jsonObject.toString());
                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.MAKEATTEND;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.SEARCHATTEND:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/attend/search");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("POST");

                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("clubName", clubName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, jsonObject.toString());
                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.SEARCHATTEND;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.MAKENOTICE:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/users/notice/make");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("POST");

                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user", User.toJSson());
                                jsonObject.put("clubName", clubName);
                                jsonObject.put("title", title);
                                jsonObject.put("content", content);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, jsonObject.toString());
                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.MAKENOTICE;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.READATTEND:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/users/attend/read");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("PUT");

                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user", User.toJSson());
                                jsonObject.put("_id", id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, jsonObject.toString());
                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.MAKENOTICE;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.SEARCHNOTICE:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/notice/search");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("POST");

                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("clubName", clubName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, jsonObject.toString());
                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.SEARCHNOTICE;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.FINEATTEND:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/users/attend/fine");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("PUT");

                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user", User.toJSson());
                                jsonObject.put("clubName", clubName);
                                jsonObject.put("_id", id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, jsonObject.toString());
                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.FINEATTEND;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.PENALTYUSER:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/log/penalty/search");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("POST");

                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user", User.toJSson());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, jsonObject.toString());
                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.PENALTYUSER;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.PENALTYCLUB:
                        try {
                            URL url = new URL("http://1.201.139.81:5900/log/penalty/search");
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            http.setDefaultUseCaches(false);
                            http.setDoInput(true);
                            http.setDoOutput(true);
                            http.setRequestMethod("POST");

                            http.setRequestProperty("content-type", "application/json");

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("clubName", clubName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, jsonObject.toString());
                            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                            PrintWriter writer = new PrintWriter(outStream);
                            writer.write(jsonObject.toString());
                            writer.flush();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d(TAG, str);
                            }
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.PENALTYCLUB;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
        Looper.loop();
    }

    public void setRegister(String email, String pw, String name) {
        this.user_email = email;
        this.user_pw = pw;
        this.user_name = name;
    }
    public void setLogin(String email, String pw) {
        this.user_email = email;
        this.user_pw = pw;
    }

    public void setAddClub(String clubName, String clubInfo) {
        this.clubName = clubName;
        this.clubInfo = clubInfo;
    }

    public void setRegisterClub(String ClubName) {
        this.clubName = ClubName;
    }

    public void setMakeAttend(String clubName, String title, String deadline, String code) {
        this.clubName = clubName;
        this.title = title;
        this.deadline = deadline;
        this.code = code;
    }

    public void setMakeNotice(String clubName, String title, String content) {
        this.clubName = clubName;
        this.title = title;
        this.content = content;
    }

    public void setReadAttend(int id) {this.id = id;}

    public void setFineAttend(String clubName, int id) {
        this.clubName = clubName;
        this.id = id;
    }

    public static class Type {
        public static final int ERROR = -1;
        public static final int REGISTER = 1;
        public static final int LOGIN = 2;
        public static final int ADDCLUB = 3;
        public static final int SEARCHCLUB = 4;
        public static final int SEARCHMYCLUB = 5;
        public static final int REGISTERCLUB = 6;
        public static final int CYBERMONEY = 7;
        public static final int MAKEATTEND = 8;
        public static final int SEARCHATTEND = 9;
        public static final int MAKENOTICE = 10;
        public static final int READATTEND = 11;
        public static final int SEARCHNOTICE = 12;
        public static final int FINEATTEND = 13;
        public static final int PENALTYUSER = 14;
        public static final int PENALTYCLUB = 15;
    }
}
