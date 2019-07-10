package com.njupt.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.njupt.Adapters.OpenLabBookingAdapter;
import com.njupt.CustomComponents.DialogAutoDismiss;
import com.njupt.CustomComponents.DialogNoDismiss;
import com.njupt.HttpConnection.GetData;
import com.njupt.HttpConnection.PostUtils;
import com.njupt.POJO.LabBooking;
import com.njupt.POJO.OpenLabInfo;
import com.njupt.R;
import com.njupt.Utils.TimeUtil;
import com.njupt.Utils.getPropertiesUtil;

import java.util.ArrayList;
import java.util.List;

public class OpenLabBooking extends Activity {

    private ListView openLabInfoListView;
    private List<OpenLabInfo> openLabInfoList = new ArrayList<OpenLabInfo>();

    boolean isLoading = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏

        setContentView(R.layout.activity_booking);

        viewInit();

        getOpenLabInfo();

    }

    private void viewInit(){
        ImageButton closeButton = (ImageButton) findViewById(R.id.booking_imageButton_close);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getOpenLabInfo() {
        openLabInfoList = new ArrayList<OpenLabInfo>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String SelectBooking = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                            + "SelectBooking");
                    openLabInfoList = JSON.parseArray(SelectBooking, OpenLabInfo.class);
                    Log.i("Laboratory", "返回信息：" + SelectBooking);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        Toast.makeText(getApplicationContext(),
                "加载中...",
                Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openLabInfoListView = (ListView) findViewById(R.id.booking_listview_content);

                openLabInfoListView.setAdapter(new OpenLabBookingAdapter(OpenLabBooking.this, openLabInfoList));

                openLabInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final EditText inputContent = new EditText(OpenLabBooking.this);
                        final OpenLabInfo openLabInfo = openLabInfoList.get(position);
                        try {
                            final JSONObject userJsonObject = JSONObject.parseObject(getIntent().getStringExtra("UserInfo"));
                            TimeUtil timeUtil = new TimeUtil();
                            new AlertDialog.Builder(OpenLabBooking.this)
                                    .setTitle("请确认预约信息")
                                    .setMessage("学号：" + userJsonObject.getString("userId")
                                            + "\n姓名：" + userJsonObject.getString("userName")
                                            + "\n日期：" + timeUtil.FormatData(openLabInfo.getOpenDate())
                                            + "\n时间：" + openLabInfo.getOpenTime()
                                            + "\n周次：" + openLabInfo.getOpenWeek()
                                            + "\n老师：" + openLabInfo.getOpenTeacherName()
                                            + "\n已预约：" + openLabInfo.getBookingNum()
                                            + "\n可预约：" + openLabInfo.getNotBookingNum()
                                            + "\n实验内容：")
                                    .setView(inputContent)
                                    .setPositiveButton("确认预约", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Thread bookingThread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LabBooking labBooking = new LabBooking();
                                                    labBooking.setUserId(userJsonObject.getString("userId"));
                                                    labBooking.setUserName(userJsonObject.getString("userName"));
                                                    labBooking.setBookDate(openLabInfo.getOpenDate());
                                                    labBooking.setBookTime(openLabInfo.getOpenTime());
                                                    labBooking.setBookWeek(openLabInfo.getOpenWeek());
                                                    labBooking.setOpenScheduleId(openLabInfo.getId());
                                                    labBooking.setClassId(userJsonObject.getString("userClass"));
                                                    labBooking.setBookContent(inputContent.getText().toString());
                                                    final String response = PostUtils.PostString(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                                                    + "CreateBookingLog",
                                                            JSONObject.toJSONString(labBooking));
                                                    Log.i("Laboratory", "向CreateBookingLog接口POST数据：" + JSONObject.toJSONString(labBooking));
                                                    Log.i("Laboratory", "POST返回信息：" + response);


                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (response.equals("error_duplicate_book")) {
                                                                DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(OpenLabBooking.this);
                                                                builder.setTitle("重复预约！")
                                                                        .create(3000);
                                                                return;
                                                            }
                                                            if (response.equals("error_book_book") || response.equals("")) {
                                                                DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(OpenLabBooking.this);
                                                                builder.setTitle("预约失败！")
                                                                        .create(3000);
                                                                return;
                                                            }
                                                            final JSONObject responseJSON = JSONObject.parseObject(response);
                                                            DialogNoDismiss.Builder builder = new DialogNoDismiss.Builder(OpenLabBooking.this);
                                                            builder.setTitle("预约成功！"
                                                                    + "\n实验室：" + responseJSON.getString("bookLabName")
                                                                    + "\n桌号：" + responseJSON.getString("bookTableId"))
                                                                    .create()
                                                                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                                        @Override
                                                                        public void onDismiss(DialogInterface dialog) {
                                                                            finish();
                                                                        }
                                                                    });
                                                        }
                                                    });
                                                }
                                            });
                                            bookingThread.start();
                                        }
                                    })
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 1000);
    }
}
