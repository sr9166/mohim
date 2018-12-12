package com.example.ydk.ss55.Thread;

import android.media.MediaExtractor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class KakaoAPI extends Thread {
    private final String adminKey = "KakaoAK de4a1298a10e8e9cfcccb859acb3285f";
    private String tid;
    private String cid = "TC0ONETIME";
    private String partner_order_id = "partner_order_id";
    private String partner_user_id = "partner_user_id";
    private String item_name;
    private String quantity = "1";
    private String total_amount;
    private String vat_amount;
    private String tax_free_amount = "0";
    private String approval_url = "" +
            "http://1.201.139.81:5900/?" +
            "&cid=" + cid +
            "&tid=" + tid +
            "&partner_order_id=" + partner_order_id +
            "&partner_user_id=" + partner_user_id;
    private String fail_url;
    private String cancel_url;


    private static KakaoAPI instacne = null;
    private KakaoAPI() {} //private 생성자
    public static KakaoAPI getInstacne() {
        if(instacne == null) {
            instacne = new KakaoAPI();
            instacne.setDaemon(true);
            instacne.start();
        }
        return instacne;
    }

    private String myResult = "";
    private Handler thHandler;  //쓰레드 핸들러
    private Handler fragmentHandler;    //프래그먼트 핸들러
    public Handler getThHandler() {return thHandler;}
    public void setFragmentHandler(Handler fragmentHandler) {this.fragmentHandler = fragmentHandler;}

    private String xml = "";
    private URL url = null;
    private BufferedReader in = null;
    private String inLine = "";

    public void run() {
        Looper.prepare();
        thHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Message retMsg = new Message();

                switch (msg.what) {
                    case Type.READY:
                        try {
                            URL url = new URL("https://kapi.kakao.com/v1/payment/ready");
                            Map<String, Object> params = new LinkedHashMap<>(); // 파라미터 세팅

                            params.put("cid", "TC0ONETIME");
                            params.put("partner_order_id", "partner_order_id");
                            params.put("partner_user_id", "partner_user_id");
                            params.put("item_name", "사이버머니 10000원");
                            params.put("quantity", "1");
                            params.put("total_amount", "11000");
                            params.put("vat_amount", "1000");
                            params.put("tax_free_amount", "0");
                            params.put("approval_url", String.format("http://163.180.116.251:8080/mohagi/kakaosuccess?time=%s&item_name=%s&total_amount=%s", Calendar.getInstance().getTime().toString(),"사이버머니 만원권", "11000원"));
                            params.put("fail_url", "http://163.180.116.251:8080");
                            params.put("cancel_url", "http://163.180.116.251:8080");
                            StringBuilder postData = new StringBuilder();
                            for (Map.Entry<String, Object> param : params.entrySet()) {
                                if (postData.length() != 0) postData.append('&');
                                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                                postData.append('=');
                                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                            }
                            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                            conn.setRequestProperty("Authorization", adminKey);
                            conn.setRequestProperty("Cache-Control", "no-cache, must-revalidate");
                            conn.setRequestProperty("Pragma", "no-cache");
                            conn.setDoOutput(true);
                            conn.getOutputStream().write(postDataBytes); // POST 호출

                            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                            StringBuilder builder = new StringBuilder();
                            String line = "";
                            while ((line = reader.readLine()) != null) {
                                builder.append(line).append("\n");
                            }
                            reader.close();
                            myResult = builder.toString();
                            retMsg.obj = myResult;
                            retMsg.what = Type.READY;
                            fragmentHandler.sendMessage(retMsg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.SYNC:
                        try {
                            URL url = new URL("http://163.180.116.251:8080/mohagi/settid?str=" + tid);
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();

                            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                            BufferedReader reader = new BufferedReader(tmp);
                            StringBuilder builder = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                builder.append(str);
                                Log.d("KAKAO API", str);
                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Type.APPROVE:
                        break;
                }
            }
        };
        Looper.loop();
    }

    public static class Type{
        public static final int READY = 1;
        public static final int SYNC = 2;
        public static final int APPROVE = 3;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
