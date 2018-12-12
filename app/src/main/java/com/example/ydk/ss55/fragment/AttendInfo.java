package com.example.ydk.ss55.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ydk.ss55.DBHelper.Database;
import com.example.ydk.ss55.R;
import com.example.ydk.ss55.Thread.ServerThread;
import com.example.ydk.ss55.activity.AttendDetailsActivity;
import com.example.ydk.ss55.activity.ClubStationActivity;
import com.example.ydk.ss55.adapter.Attend_List_Adapter;
import com.example.ydk.ss55.adapter.Club_List_Adapter;
import com.example.ydk.ss55.model.AttendData;
import com.example.ydk.ss55.model.ClubData;
import com.example.ydk.ss55.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;
import static com.example.ydk.ss55.DBHelper.Database.clubMembersHelper;
import static com.example.ydk.ss55.DBHelper.Database.cursor;
import static com.example.ydk.ss55.DBHelper.Database.myDBHelper;

public class AttendInfo extends Fragment {

    private ArrayList<AttendData> mData;
    private String attendName, attendCode, clubName, user;
    private String limit_Date, limit_Time;
    private Button makeAttendBtn;
    private ListView mList;

    private Attend_List_Adapter attend_list_adapter;

    private final String TAG = "ATTENDINFO";
    ServerThread serverThread = ServerThread.getInstacne();

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.attend_info, container, false);

        TextView clubName_ = (TextView) rootView.findViewById(R.id.clubName);

        Intent intent = getActivity().getIntent();

        clubName = intent.getStringExtra("ClubName");
        clubName_.setText(clubName + " 모임의 출석체크");
        user = intent.getStringExtra("UserName");
        TextView textView = (TextView) rootView.findViewById(R.id.textView);

        mList = (ListView) rootView.findViewById(R.id.lvData);
        mData = new ArrayList<>();
        serverThread.setFragmentHandler(mHandler);
        serverThread.setRegisterClub(clubName);
        serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.SEARCHATTEND);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AttendData data = mData.get(i);
                if(isBoss())
                    bossDialog(data);
                else if(!data.getUnread_email_list().contains(User.getEmail())) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("READ ATTEND SUCCESS!")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
                else if(checkDeadline(data))
                    memberDialog(data);
                else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Deadline goes by ... ")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
                /*
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                if (belongToOurClub()) {
                    final EditText input = new EditText(getContext());
                    builder.setTitle(mData.get(i).getAttendName());
                    builder.setView(input);
                    builder.setPositiveButton(getString(android.R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    checkPositive(i, input.getText().toString());
                                }
                            });
                    builder.setNegativeButton(getString(android.R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(getContext(), "동아리 회원만 보실수 있습니다!!", Toast.LENGTH_SHORT).show();
                }
                */
            }
        });

        makeAttendBtn = (Button) rootView.findViewById(R.id.makeAttend);
        makeAttendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBoss()) {
                    Intent intent = new Intent(getContext(), AttendDetailsActivity.class);
                    intent.putExtra("ClubName", clubName);
                    intent.putExtra("UserName", user);
                    startActivityForResult(intent, 0);
                } else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText("Do not Permitted!")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String ClubName = data.getStringExtra("ClubName");
            String AttendName = data.getStringExtra("AttendName");
            String AttendCode = data.getStringExtra("AttendCode");
            String Date = data.getStringExtra("Date");
            String Time = data.getStringExtra("Time");
            AttendData attendData = new AttendData(ClubName, AttendName, AttendCode, Date, Time);
            attend_list_adapter.add(attendData);
            attend_list_adapter.notifyDataSetChanged();
            serverThread.setFragmentHandler(mHandler);
            serverThread.setMakeAttend(ClubName, AttendName, attendData.dateFormat(), AttendCode);
            serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.MAKEATTEND);
            serverThread.setRegisterClub(clubName);
            serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.SEARCHATTEND);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isBoss() {
        if(User.getEmail().equals(ClubStationActivity.clubBoss))
            return true;
        else
            return false;
    }

    public boolean checkDeadline(AttendData data) {
        if(Calendar.getInstance().compareTo(data.getCalendar()) == -1)   //아직 데드라인 안지나서 출석가능한 경우
            return true;
        else
            return false;
    }

    public void bossDialog(final AttendData data) {
        final SweetAlertDialog sweetAlertDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("안 읽은 사람 명단")
                .setContentText(data.getUnread_email_list().replaceAll("\\|\\|","\n"))
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelButton("Penalty", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        serverThread.setFragmentHandler(mHandler);
                        serverThread.setFineAttend(clubName, data.getId());
                        serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.FINEATTEND);
                        sDialog.dismissWithAnimation();
                    }
                });
        sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if(checkDeadline(data))
                    sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setEnabled(false);
            }
        });
        sweetAlertDialog.show();
    }

    public void memberDialog(final AttendData data) {
        final EditText editText = new EditText(getContext());
        final SweetAlertDialog sweetAlertDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(data.getAttendName())
                .setCustomView(editText)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if(checkPositive(data, editText.getText().toString()))
                            sDialog.dismissWithAnimation();
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                });
        sweetAlertDialog.show();
    }

    //내 동아리 회원이 아닐경우 false 반환
    private boolean belongToOurClub() {
        boolean pandan = false;
        Database.ClubMembersData = Database.clubMembersHelper.getReadableDatabase();
        Cursor cursor = Database.ClubMembersData.query("ClubMembersData", Database.clubMembers, "ClubName=?",
                new String[]{clubName}, null, null, null);
        if (cursor != null) {
            int members_col = cursor.getColumnIndex("ClubMembers");
            while (cursor.moveToNext()) {
                String tmpName = cursor.getString(members_col);
                if (tmpName.equals(user)) {
                    pandan = true;
                }
            }
        }
        return pandan;
    }

    //전체 출석체킹 요약본
    private boolean checkPositive(AttendData data, String input) {
        if (data.getAttendCode().equals(input)) {
            serverThread.setReadAttend(data.getId());
            serverThread.getThHandler().sendEmptyMessage(ServerThread.Type.READATTEND);
            return true;
            /*
            //TODO : 한번 읽었던 공지로 다시 점수 획득 못하게 하기.
            if (noReadBefore(i)) {
                //TODO : 만약 마감기한 지났으면 획득 못함. 여기서 Date type에 대해 공부좀 해야될듯 -> 구현 완료
                if (overTime(mData.get(i).getAttendName())) {
                    Earn_300(i);
                } else {
                    //TODO : 반 or 1/3 or 0 점수 획득 -> 구현 완료
                    //TODO : 벌금 300원 올리고 update할 수 있는 시스템 구현하기 -> 구현 완료
                    Penalty_300();
                }
            } else {
                Toast.makeText(getContext(), "이미 출석처리 되었습니다.", Toast.LENGTH_SHORT).show();
            }
            */
        } else {
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("Please enter a valid password")
                    .setConfirmText("Confirm")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
            return false;
        }
    }

    private void showListView1() {
        mData = new ArrayList<AttendData>();
        attend_list_adapter = new Attend_List_Adapter(getContext(), mData);
        Database.AttendData = Database.attendHelper.getReadableDatabase();
        Database.cursor = Database.AttendData.query("AttendData", Database.attend, "ClubName=?",
                new String[]{clubName}, null, null, null);
        if (Database.cursor != null) {
            int id_col = cursor.getColumnIndex("_id");
            int name_col = cursor.getColumnIndex("AttendName");
            int pw_col = cursor.getColumnIndex("AttendCode");
            int date_col = cursor.getColumnIndex("Date");
            int time_col = cursor.getColumnIndex("Time");
            while (Database.cursor.moveToNext()) {
                attendName = Database.cursor.getString(name_col);
                limit_Date = Database.cursor.getString(date_col);
                limit_Time = Database.cursor.getString(time_col);
                attendCode = Database.cursor.getString(pw_col);
                AttendData tmp = new AttendData(clubName, attendName, attendCode, limit_Date, limit_Time);
                mData.add(tmp);
                Log.d("database", "info : " + attendName + attendCode + limit_Date + limit_Time);
                attend_list_adapter.notifyDataSetChanged();
            }
            Database.cursor.close();
        }
    }

    //    static String[] attendReadOnce = {"_id","ClubName","UserName","AttendName","AttendCheck"}; //읽었던 출석에 대한 정보 스키마
    //이름, 공지 검색 후 안읽었을 경우에 true 반환, 읽었으면 false반환
    private boolean noReadBefore(int position) {
        boolean pandan = false;
        Database.AttendReadOnce = Database.attendReadOnceHelper.getReadableDatabase();
        Cursor cursor = Database.AttendReadOnce.rawQuery("select UserName, AttendCheck from AttendReadOnce where AttendName =?",
                new String[]{mData.get(position).getAttendName()});
        if (cursor != null) {
            int userName_col = cursor.getColumnIndex("UserName");
            while (cursor.moveToNext()) {
                String user_name = cursor.getString(userName_col);
                Cursor find = Database.AttendReadOnce.rawQuery("select AttendCheck from AttendReadOnce where UserName=?",
                        new String[]{user_name});
                if (find != null) {
                    int check_col = cursor.getColumnIndex("AttendCheck");
                    if (check_col == 0) {
                        pandan = true;
                    } else
                        pandan = false;
                }
            }
        }
        return pandan;
    }

    //attendName을 통해 데이터베이스에서 이거랑 맞는 출석체크 이름을 찾는다.
    private boolean overTime(String attendName) {
        Database.AttendData = Database.attendHelper.getReadableDatabase();
        Database.ClubData = Database.clubInfoHelper.getReadableDatabase();
        Cursor cursor = Database.AttendData.query("AttendData", Database.attend, "AttendName=?",
                new String[]{attendName}, null, null, null);
        if (cursor != null) {
            int date_col = cursor.getColumnIndex("Date");
            int time_col = cursor.getColumnIndex("Time");
            while (cursor.moveToNext()) {
                //마감시간을 가져왔음.
                limit_Date = cursor.getString(date_col);
                limit_Time = cursor.getString(time_col);

                //현재 시간을 구했다.
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String tmpY = DateFormat.format("yyyy", date).toString();
                String tmpMonth = DateFormat.format("MM", date).toString();
                String tmpD = DateFormat.format("dd", date).toString();

                String nowDate = tmpY + tmpMonth + tmpD;
                String tmpH = DateFormat.format("hh", date).toString();
                String tmpMinute = DateFormat.format("mm", date).toString();
                String tmpTime = tmpH + tmpMinute;
                Log.d("finalResult", tmpY + " 년 " + tmpMonth + " 월 " + tmpD + " 일 " + tmpH + " 시 " + tmpMinute + " 분 "
                        + "\n" + attendName
                        + "\n 지금 시간 : " + nowDate + " 시간 : " + tmpTime
                        + "\n 마감 날짜 : " + limit_Date + " 시간 : " + limit_Time + "!!!");

                //현재 시각이랑 마감 시각을 구했다. 현재 시각 20181107인데 마감시각 20181109이면 300원 버는거임
                //현재시각 20181107인데 마감시각 20181101이면 지각한거임.
                //즉 마감시각보다 현재시각이 더 크면 지각임.
                if (Integer.valueOf(limit_Date) < Integer.valueOf(nowDate)) {
                    return false;
                }

                /*
                 * Calendar now = Calendar.getInstance();
                 * Calendar limit = Calendar.getInstance();
                 * limit.set(limit_Date, limit_Time);        // 이부분 Date와 Time 어떻게 설정 되어져 있는지 체크하고 split한다음 set 하기
                 * if(limit.compareTo(now) == -1) {
                 *       return false
                 *  } else {
                 *       return true
                 *  }
                 */
            }
            Database.cursor.close();
        }
        return true;
    }

    //해당 유저가 보유하고 있는 돈을 가져온다. 업데이트를 위해!!
    private int takeUserMoney() {
        int money = 0;
        Database.UserInfo = Database.myDBHelper.getReadableDatabase();
        Cursor cursor = Database.UserInfo.query("UserInfo", Database.user, "email=?",
                new String[]{user}, null, null, null);
        if (cursor != null) {
            int money_col = cursor.getColumnIndex("Money");
            while (cursor.moveToNext()) {
                money = cursor.getInt(money_col);
            }
            cursor.close();
        }
        return money;
    }

    private boolean compareAttendName(int position) {
        boolean pandan = false;
        Database.AttendReadOnce = Database.attendReadOnceHelper.getReadableDatabase();
        Cursor cursor = Database.AttendReadOnce.query("AttendReadOnce", Database.attendReadOnce, "AttendName=?",
                new String[]{mData.get(position).getAttendName()}, null, null, null);
        if (cursor != null) {
            //모임 이름이 있다면,,,, 칼럼에서 유저이름을 다 뽑는다. 같은 모임을 가진 유저이름들 다 뽑히겠지?00
            int name_col = cursor.getColumnIndex("UserName");
            while (cursor.moveToNext()) {
                String tmpName = cursor.getString(name_col);
                //지금 현재 접속해있는 유저 이름이랑 똑같아? 그럼 true 반영한다.
                if (tmpName.equals(user)) {
                    pandan = true;
                }
            }
        }
        return pandan;
    }

    private int takeUserPenalty() {
        int money = 0;
        Database.UserInfo = Database.myDBHelper.getReadableDatabase();
        Cursor cursor = Database.UserInfo.query("UserInfo", Database.user, "email=?",
                new String[]{user}, null, null, null);
        if (cursor != null) {
            int penalty_col = cursor.getColumnIndex("Penalty");
            while (cursor.moveToNext()) {
                money = cursor.getInt(penalty_col);
            }
            cursor.close();
        }
        return money;
    }

    private void Earn_300(int position) {
        //점수 오름 및 돈 획득
        Toast.makeText(getContext(), "출석 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show();
        Database.UserInfo = Database.myDBHelper.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        values.put("Money", takeUserMoney() + 300);
        Database.UserInfo.update("UserInfo", values, "email=?", new String[]{user});
        myDBHelper.close();

        //출석체크 확인표시해주기.
        //유저의 이름, 모임 일치해야함.
        Database.AttendReadOnce = Database.attendReadOnceHelper.getWritableDatabase();
        ContentValues values1;
        values1 = new ContentValues();
        values1.put("AttendCheck", takeUserReadCheck(position));
        //모임이름 일치하는거 구현함.
        if (compareAttendName(position)) {
            Database.AttendReadOnce.update("AttendReadOnce", values1, "AttendName=?", new String[]{mData.get(position).getAttendName()});
        } else {
            Toast.makeText(getContext(), "Attend이름 불일치", Toast.LENGTH_LONG).show();
        }
    }

    //앞으로 이 유저가 이 글을 읽었을 때 읽었다고 할지, 안읽었다고 할지 정해주는 함수.
    private boolean takeUserReadCheck(int position) {
        Database.AttendReadOnce = Database.attendReadOnceHelper.getReadableDatabase();
        Cursor cursor = Database.AttendReadOnce.query("AttendReadOnce", Database.attendReadOnce, "UserName=?",
                new String[]{user}, null, null, null);
        if (cursor != null) {
            int name_col = cursor.getColumnIndex("AttendName");
            while (cursor.moveToNext()) {
                String name = cursor.getString(name_col);
                if (name == null) {
                    continue;
                }
                if (name.equals(mData.get(position).getAttendName())) {
                    return true;    //글을 읽었다.
                }
            }
            cursor.close();
        }
        return false;   //글을 읽지 않았다.
    }

    private void Penalty_300() {
        //점수 오름 및 돈 획득
        Toast.makeText(getContext(), "+100(결석)", Toast.LENGTH_SHORT).show();
        Database.UserInfo = Database.myDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Money", takeUserMoney() - 300);
        Database.UserInfo.update("UserInfo", values, "email=?", new String[]{user});

        //Todo : 벌금항목 올려주기
        ContentValues values1 = new ContentValues();
        values1.put("Penalty", takeUserPenalty() + 100);
        Database.UserInfo.update("UserInfo", values1, "email=?", new String[]{user});

        //clubmembers에서 벌금 항목 올려주기
        Database.ClubMembersData = Database.clubMembersHelper.getWritableDatabase();
        ContentValues values2 = new ContentValues();
        values2.put("Penalty", takeUserPenalty() + 100);
        Database.ClubMembersData.update("ClubMembersData", values2, "Penalty=?", new String[]{user});

        clubMembersHelper.close();
        myDBHelper.close();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.obj.toString());
            switch (msg.what) {
                case ServerThread.Type.MAKEATTEND:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String code = jsonObject.getString("code");
                        if (code.equals("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("SUCCESS")
                                    .setContentText("Make Attend Success!")
                                    .setConfirmText("Confirm")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else if (code.equals("500")) {
                            Toast.makeText(getContext(), "You are not Permitted!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ServerThread.Type.SEARCHATTEND:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String code = jsonObject.getString("code");
                        if (code.equals("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            mData.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String c_club_name = jsonArray.getJSONObject(i).getString("c_club_name");
                                String title = jsonArray.getJSONObject(i).getString("title");
                                String deadline = jsonArray.getJSONObject(i).getString("deadline");
                                String c_code = jsonArray.getJSONObject(i).getString("code");
                                String[] deadlineArray = deadline.split("T");
                                String date = deadlineArray[0].replaceAll("-","");
                                String[] timeArray = deadlineArray[1].split(":");
                                String time = timeArray[0] + timeArray[1];
                                int id = jsonArray.getJSONObject(i).getInt("_id");
                                String unread_email_list = jsonArray.getJSONObject(i).getString("unread_email_list");

                                mData.add(new AttendData(c_club_name, title, c_code, date, time, unread_email_list, id));
                            }
                            attend_list_adapter = new Attend_List_Adapter(getContext(), mData);
                            mList.setAdapter(attend_list_adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ServerThread.Type.READATTEND:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String code = jsonObject.getString("code");
                        if (code.equals("200")) {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("READ ATTEND SUCCESS!")
                                    .setConfirmText("Confirm")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ServerThread.Type.FINEATTEND:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String code = jsonObject.getString("code");
                        if (code.equals("200"))
                            Toast.makeText(getContext(), "Fine Attend Success!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
