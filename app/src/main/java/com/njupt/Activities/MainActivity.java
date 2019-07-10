package com.njupt.Activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.njupt.CustomComponents.DialogAutoDismiss;
import com.njupt.CustomComponents.DialogNoDismiss;
import com.njupt.CustomComponents.ImageViewInIndex;
import com.njupt.CustomComponents.VideoViewInIndex;
import com.njupt.HttpConnection.GetData;
import com.njupt.R;
import com.njupt.SerialPort.ControlSerialPort;
import com.njupt.Services.EvnService;
import com.njupt.Services.RFIDService;
import com.njupt.Utils.getPropertiesUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends Activity {

    ImageViewInIndex startClassImageView;
    ImageViewInIndex teachLogImageView;
    ImageViewInIndex equipmentRepairImageView;
    ImageViewInIndex deviceControlImageView;
    ImageViewInIndex openOrderImageView;
    ImageViewInIndex GuideWebImageView;
    VideoViewInIndex indexVideoView;
    TextView indexTextTime;
    TextView indexTextTimeSec;
    TextView indexTextDate;
    TextView indexTextWeek;
    TextView indexTextCourse;
    TextView indexTextTeacher;
    TextView indexTextClass;
    TextView indexTextWeekNum;
    TextView indexTextTerm;
    TextView indexTextTemp;
    TextView indexTextHumidity;
    TextView indexTextVOC;
    TextView indexTextShuijin;
    TextView indexTextPower;
    TextView indexTextFangdao;
    TextView indexTextDoor1;
    TextView indexTextViewFlipper1;
    TextView indexTextViewFlipper2;
    TextView indexTextViewFlipper3;
    TextView indexTextViewFlipper4;
    TextView indexTextViewFlipper5;
    TextView indexTextViewFlipper6;
    TextView indexTextViewFlipper7;
    TextView indexTextViewFlipper8;
    TextView indexTextViewFlipper9;
    TextView indexTextViewFlipper10;
    TextView indexTextViewFlipper11;
    TextView indexTextViewFlipper12;
    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    LinearLayout linearLayout3;
    private ViewFlipper viewFlipper;

    String VIDEO_PATH; //首页视频的地址

    int pos = (int) (1 + Math.random() * 15); //产生一个1到15的随机数

    AttendanceRFIDReceiver attendanceRFIDReceiver;
    TeachLogRFIDReceiver teachLogRFIDReceiver;
    ReportRepairRFIDReceiver reportRepairRFIDReceiver;
    DeviceControlRFIDReceiver deviceControlRFIDReceiver;
    EnvironmentReceiver environmentReceiver;
    OpenBookRFIDReceiver openBookRFIDReceiver;

    public SharedPreferences sharedPreference_ClassroomNum; //记录实验室账号的相关数据

    ControlSerialPort serialPortOperation; //操作串口的工具

    DialogNoDismiss.Builder checkIdDialogNoDismissBuilder; //检查身份弹框

    boolean isTimeout = false; //连接超时标签
    boolean isFullScreen = false; //全屏标签

    LinearLayout.LayoutParams video_params;
    LinearLayout.LayoutParams layoutParams1;
    LinearLayout.LayoutParams layoutParams2;
    LinearLayout.LayoutParams layoutParams3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏

        setContentView(R.layout.activity_main);

        FindView(); //联动view组件

        setLabIdInFirstOpen(); //第一次使用设置实验室ID

        PlayVideoInIndex(); //首页循环播放视频

        ViewOnClick(); //view组件添加事件监听

        TimeInIndex(); //首页动态显示时间


        Intent RFIDIntent = new Intent(MainActivity.this, RFIDService.class);
        startService(RFIDIntent); //开启RFID服务

        Intent EvnIntent = new Intent(MainActivity.this, EvnService.class);
        startService(EvnIntent); //开启环境监控服务

        serialPortOperation = new ControlSerialPort(); //操作串口的工具

        registerAttendanceRFIDReceiver(); //注册考勤广播接收

        registerEnvironmentReceiver(); //注册环境监控广播

    }

    private void FindView() {
        startClassImageView = (ImageViewInIndex) findViewById(R.id.index_imageview_startclass);
        teachLogImageView = (ImageViewInIndex) findViewById(R.id.index_imageview_teachLog);
        equipmentRepairImageView = (ImageViewInIndex) findViewById(R.id.index_imageview_equipment_repair);
        deviceControlImageView = (ImageViewInIndex) findViewById(R.id.index_imageview_devicemanage);
        openOrderImageView = (ImageViewInIndex) findViewById(R.id.index_imageview_openorder);
        GuideWebImageView = (ImageViewInIndex) findViewById(R.id.index_imageview_help);

        indexVideoView = (VideoViewInIndex) findViewById(R.id.videoView);

        indexTextTime = (TextView) findViewById(R.id.index_text_time);
        indexTextDate = (TextView) findViewById(R.id.index_text_date);
        indexTextWeek = (TextView) findViewById(R.id.index_text_week);
        indexTextTimeSec = (TextView) findViewById(R.id.index_text_time_sec);
        indexTextClass = (TextView) findViewById(R.id.index_text_class);
        indexTextCourse = (TextView) findViewById(R.id.index_text_course);
        indexTextTeacher = (TextView) findViewById(R.id.index_text_teacher);
        indexTextTerm = (TextView) findViewById(R.id.index_text_term);
        indexTextWeekNum = (TextView) findViewById(R.id.index_text_weeknum);
        indexTextTemp = (TextView) findViewById(R.id.index_text_temp);
        indexTextHumidity = (TextView) findViewById(R.id.index_text_humidity);
        indexTextVOC = (TextView) findViewById(R.id.index_text_voc);
        indexTextShuijin = (TextView) findViewById(R.id.index_text_shuijin);
        indexTextPower = (TextView) findViewById(R.id.index_text_power);
        indexTextFangdao = (TextView) findViewById(R.id.index_text_fangdao);
        indexTextDoor1 = (TextView) findViewById(R.id.index_text_door1);
        indexTextViewFlipper1 = (TextView) findViewById(R.id.index_text_viewFipper1);
        indexTextViewFlipper2 = (TextView) findViewById(R.id.index_text_viewFipper2);
        indexTextViewFlipper3 = (TextView) findViewById(R.id.index_text_viewFipper3);
        indexTextViewFlipper4 = (TextView) findViewById(R.id.index_text_viewFipper4);
        indexTextViewFlipper5 = (TextView) findViewById(R.id.index_text_viewFipper5);
        indexTextViewFlipper6 = (TextView) findViewById(R.id.index_text_viewFipper6);
        indexTextViewFlipper7 = (TextView) findViewById(R.id.index_text_viewFipper7);
        indexTextViewFlipper8 = (TextView) findViewById(R.id.index_text_viewFipper8);
        indexTextViewFlipper9 = (TextView) findViewById(R.id.index_text_viewFipper9);
        indexTextViewFlipper10 = (TextView) findViewById(R.id.index_text_viewFipper10);
        indexTextViewFlipper11 = (TextView) findViewById(R.id.index_text_viewFipper11);
        indexTextViewFlipper12 = (TextView) findViewById(R.id.index_text_viewFipper12);

        linearLayout1 = (LinearLayout) findViewById(R.id.indexi_layout_video_1);
        linearLayout2 = (LinearLayout) findViewById(R.id.indexi_layout_video_2);
        linearLayout3 = (LinearLayout) findViewById(R.id.indexi_layout_video_3);

        viewFlipper = (ViewFlipper) findViewById(R.id.index_viewFipper);

    }

    private void getIndexInfo(final String LabId) {

        final Thread getCourseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String s = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                + "getIndexInfo?LabId=" + LabId);
                        final JSONObject jsonObject = JSONObject.parseObject(s);
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                if (jsonObject != null) {
                                    indexTextCourse.setText("当前课程：" + jsonObject.getString("courseName"));
                                    indexTextClass.setText("上课班级：" + jsonObject.getString("courseClass"));
                                    indexTextTeacher.setText("上课老师：" + jsonObject.getString("courseTeacherName"));
                                    indexTextTerm.setText(jsonObject.getString("semesteName"));
                                    indexTextWeekNum.setText(jsonObject.getString("weekName"));
                                    isTimeout = false;
                                }
                            }
                        });
                        String LabInfo = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                + "getLabInfoById?LabId=" + LabId);
                        SharedPreferences.Editor editor = sharedPreference_ClassroomNum.edit();
                        editor.putString("LabInfo", LabInfo);
                        editor.apply();
                        Thread.sleep(1000 * 60 * 5); //五分钟更新一次首页信息
                    } catch (Exception e) {
                        Log.e("Laboratory", "网络故障，请求首页信息失败");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isTimeout)
                                    if (!MainActivity.this.isFinishing()) {
                                        DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                        builder.setTitle("请求失败，请检查网络！")
                                                .create(1000 * 10);
                                        isTimeout = true;
                                        indexTextCourse.setText("当前课程：获取中...");
                                        indexTextClass.setText("上课班级：获取中...");
                                        indexTextTeacher.setText("上课老师：获取中...");
                                        return;
                                    }
                            }
                        });
                        try {
                            Thread.sleep(1000 * 10);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        getCourseThread.start();

        final JSONArray jsonArray = new JSONArray();
        Thread viewFlipperThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://www.njupt.edu.cn/72/list.htm";
                Document doc = null;
                while (true) {
                    try {
                        doc = Jsoup.connect(url).get();
                        Elements listDiv = doc.getElementsByAttributeValue("frag", "窗口2");
                        for (Element element : listDiv) {
                            Elements texts = element.getElementsByTag("a");
                            for (Element text : texts) {
                                String resultTitle = text.text();
                                String resultUrl = text.attr("href");
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("title", "● " + resultTitle);
                                jsonObject.put("href", "http://www.njupt.edu.cn" + resultUrl);
                                jsonArray.add(jsonObject);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final JSONObject showJsonObject1 = (JSONObject) jsonArray.get(0);
                                indexTextViewFlipper1.setText(showJsonObject1.getString("title"));
                                indexTextViewFlipper1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject1.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject2 = (JSONObject) jsonArray.get(1);
                                indexTextViewFlipper2.setText(showJsonObject2.getString("title"));
                                indexTextViewFlipper2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject2.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject3 = (JSONObject) jsonArray.get(2);
                                indexTextViewFlipper3.setText(showJsonObject3.getString("title"));
                                indexTextViewFlipper3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject3.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject4 = (JSONObject) jsonArray.get(3);
                                indexTextViewFlipper4.setText(showJsonObject4.getString("title"));
                                indexTextViewFlipper4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject4.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject5 = (JSONObject) jsonArray.get(4);
                                indexTextViewFlipper5.setText(showJsonObject5.getString("title"));
                                indexTextViewFlipper5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject5.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject6 = (JSONObject) jsonArray.get(5);
                                indexTextViewFlipper6.setText(showJsonObject6.getString("title"));
                                indexTextViewFlipper6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject6.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject7 = (JSONObject) jsonArray.get(6);
                                indexTextViewFlipper7.setText(showJsonObject7.getString("title"));
                                indexTextViewFlipper7.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject7.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject8 = (JSONObject) jsonArray.get(7);
                                indexTextViewFlipper8.setText(showJsonObject8.getString("title"));
                                indexTextViewFlipper8.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject8.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject9 = (JSONObject) jsonArray.get(8);
                                indexTextViewFlipper9.setText(showJsonObject9.getString("title"));
                                indexTextViewFlipper9.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject9.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject10 = (JSONObject) jsonArray.get(9);
                                indexTextViewFlipper10.setText(showJsonObject10.getString("title"));
                                indexTextViewFlipper10.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject10.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject11 = (JSONObject) jsonArray.get(10);
                                indexTextViewFlipper11.setText(showJsonObject11.getString("title"));
                                indexTextViewFlipper11.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject11.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                final JSONObject showJsonObject12 = (JSONObject) jsonArray.get(11);
                                indexTextViewFlipper12.setText(showJsonObject12.getString("title"));
                                indexTextViewFlipper12.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                                        intent.putExtra("URL", showJsonObject12.getString("href"));
                                        startActivity(intent);
                                    }
                                });
                                viewFlipper.startFlipping();
                            }
                        });
                        Log.i("Laboratory", "请求到：" + jsonArray.toJSONString());
                        Thread.sleep(1000 * 60 * 5);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(1000 * 10);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }

                }
            }
        });
        viewFlipperThread.start();

    }

    private void ViewOnClick() {
        //教学日志事件
        teachLogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (indexTextCourse.getText().toString().equals("当前课程：无")
                        || indexTextCourse.getText().toString().equals("当前课程：获取中...")) {
                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                    builder.setTitle("当前此教室无课！")
                            .create(3000);
                    return;
                }*/
                unregisterAttendanceRFIDReceiver();
                registerTeachLogRFIDReceiver();
                checkIdDialogNoDismissBuilder = new DialogNoDismiss.Builder(MainActivity.this);
                checkIdDialogNoDismissBuilder.setTitle("请老师刷卡验证身份")
                        .create()
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                registerAttendanceRFIDReceiver();
                                unregisterTeachLogRFIDReceiver();
                            }
                        });
            }
        });
        //设备报修事件
        equipmentRepairImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterAttendanceRFIDReceiver();
                registerReportRepairRFIDReceiver();
                checkIdDialogNoDismissBuilder = new DialogNoDismiss.Builder(MainActivity.this);
                checkIdDialogNoDismissBuilder.setTitle("请老师刷卡验证身份")
                        .create()
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                registerAttendanceRFIDReceiver();
                                unregisterReportRepairRFIDReceiver();
                            }
                        });
            }
        });
        //教师上课事件
        startClassImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexTextCourse.getText().toString().equals("当前课程：无")) {
                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                    builder.setTitle("当前此教室无课！")
                            .create(3000);
                    return;
                }
            }
        });
        //设备控制事件
        deviceControlImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unregisterAttendanceRFIDReceiver();
                registerDeviceControlRFIDReceiver();
                checkIdDialogNoDismissBuilder = new DialogNoDismiss.Builder(MainActivity.this);
                checkIdDialogNoDismissBuilder.setTitle("请老师刷卡验证身份")
                        .create()
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                registerAttendanceRFIDReceiver();
                                unregisterDeviceControlRFIDReceiver();
                            }
                        });
            }
        });
        //开放预约事件
        openOrderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterAttendanceRFIDReceiver();
                registerOpenBookRFIDReceiver();
                checkIdDialogNoDismissBuilder = new DialogNoDismiss.Builder(MainActivity.this);
                checkIdDialogNoDismissBuilder.setTitle("请刷卡验证身份")
                        .create()
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                registerAttendanceRFIDReceiver();
                                unregisterOpenBookRFIDReceiver();
                            }
                        });
            }
        });
        //使用指南事件
        GuideWebImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuideWeb.class);
                intent.putExtra("URL", "https://www.ilearning5.top/ClassroomServer/");
                startActivity(intent);
            }
        });
    }

    private void TimeInIndex() {
        @SuppressLint("HandlerLeak") final Handler timeHandler = new Handler() {
            String dateMessage = "";

            public void handleMessage(Message msg) {
                dateMessage = (String) msg.obj;
                indexTextTime.setText(dateMessage.split("&")[1]);
                indexTextTimeSec.setText(dateMessage.split("&")[2]);
                indexTextDate.setText(dateMessage.split("&")[0]);
                indexTextWeek.setText(dateMessage.split("&")[3]);
            }
        };
        Thread timeThread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日&HH:mm& ss&EEEE", Locale.getDefault());
                        String str = sdf.format(new Date());
                        timeHandler.sendMessage(timeHandler.obtainMessage(100, str));
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timeThread.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void PlayVideoInIndex() {
        VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() + "/LabManager/Videos/" + 1 + ".mp4";
        indexVideoView.setVideoPath(VIDEO_PATH);
        //indexVideoView.setVideoURI(Uri.parse("http://223.110.242.130:6610/gitv/live1/G_CCTV-6-HQ/1.m3u8")); //HD
        // indexVideoView.setVideoURI(Uri.parse("http://111.20.33.71/PLTV/88888888/224/3221225617/index.m3u8")); //CCTV-13 !CMCC
        indexVideoView.start();
        indexVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        indexVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                pos--;
                VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() + "/LabManager/Videos/" + pos + ".mp4";
                File video = new File(VIDEO_PATH);
                if (video.exists()) {
                    indexVideoView.setVideoPath(VIDEO_PATH);
                    indexVideoView.start();
                } else {
                    pos = 15;
                    VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() + "/LabManager/Videos/" + pos + ".mp4";
                    indexVideoView.setVideoPath(VIDEO_PATH);
                    indexVideoView.start();
                }

            }
        });
        //视频窗口监听
        indexVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isFullScreen) {
                    video_params = (LinearLayout.LayoutParams) indexVideoView.getLayoutParams();
                    layoutParams1 = (LinearLayout.LayoutParams) linearLayout1.getLayoutParams();
                    layoutParams2 = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
                    layoutParams3 = (LinearLayout.LayoutParams) linearLayout3.getLayoutParams();
                    Log.i("info", "全屏布局已设置");
                    LinearLayout.LayoutParams paramsesFull_FullScreen = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
                    LinearLayout.LayoutParams paramses_Disappear = new LinearLayout.LayoutParams(0, 0);
                    linearLayout1.setLayoutParams(paramsesFull_FullScreen);
                    linearLayout2.setLayoutParams(paramsesFull_FullScreen);
                    linearLayout3.setLayoutParams(paramses_Disappear);
                    indexVideoView.setLayoutParams(paramsesFull_FullScreen);
                    isFullScreen = true;
                } else {
                    linearLayout1.setLayoutParams(layoutParams1);
                    linearLayout2.setLayoutParams(layoutParams2);
                    linearLayout3.setLayoutParams(layoutParams3);
                    indexVideoView.setLayoutParams(video_params);
                    isFullScreen = false;
                }
                return false;
            }
        });
    }

    private void setLabIdInFirstOpen() {
        //初始化SharedPreferences
        sharedPreference_ClassroomNum = this.getSharedPreferences("classroomNum", Context.MODE_PRIVATE);
        //定义是否为第一次打开，若不存在“is_first_open”则创建一个并设置默认值为true
        final Boolean isFirstIn = sharedPreference_ClassroomNum.getBoolean("is_first_open", true);
        final EditText inputServer = new EditText(this);
        if (isFirstIn) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("请输入实验室ID")
                    .setMessage("检测到您初次使用，请输入实验室ID：")
                    .setView(inputServer)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreference_ClassroomNum.edit();
                            editor.putString("LabId", inputServer.getText().toString());
                            editor.putBoolean("is_first_open", false);
                            editor.apply();
                            dialog.dismiss();
                            getIndexInfo(sharedPreference_ClassroomNum.getString("LabId", "")); //请求服务器首页信息
                        }
                    })
                    .show();
        } else {
            getIndexInfo(sharedPreference_ClassroomNum.getString("LabId", "")); //请求服务器首页信息
            Log.i("Laboratory", "非初次使用，实验室信息为：" + sharedPreference_ClassroomNum.getString("LabInfo", ""));
        }
    }

    private void registerAttendanceRFIDReceiver() {
        Log.i("Laboratory", "√ 注册广播接收器attendanceRFIDReceiver");
        attendanceRFIDReceiver = new AttendanceRFIDReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.njupt.Services.RFIDService");
        this.registerReceiver(attendanceRFIDReceiver, filter);
    }

    private void unregisterAttendanceRFIDReceiver() {
        Log.i("Laboratory", "X 注销广播接收器attendanceRFIDReceiver");
        this.unregisterReceiver(attendanceRFIDReceiver);
    }

    private void registerTeachLogRFIDReceiver() {
        Log.i("Laboratory", "√ 注册广播接收器teachLogRFIDReceiver");
        teachLogRFIDReceiver = new TeachLogRFIDReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.njupt.Services.RFIDService");
        this.registerReceiver(teachLogRFIDReceiver, filter);
    }

    private void unregisterTeachLogRFIDReceiver() {
        Log.i("Laboratory", "X 注销广播接收器teachLogRFIDReceiver");
        this.unregisterReceiver(teachLogRFIDReceiver);
    }

    private void registerReportRepairRFIDReceiver() {
        Log.i("Laboratory", "√ 注册广播接收器reportRepairRFIDReceiver");
        reportRepairRFIDReceiver = new ReportRepairRFIDReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.njupt.Services.RFIDService");
        this.registerReceiver(reportRepairRFIDReceiver, filter);
    }

    private void unregisterReportRepairRFIDReceiver() {
        Log.i("Laboratory", "X 注销广播接收器reportRepairRFIDReceiver");
        this.unregisterReceiver(reportRepairRFIDReceiver);
    }

    private void registerDeviceControlRFIDReceiver() {
        Log.i("Laboratory", "√ 注册广播接收器deviceControlRFIDReceiver");
        deviceControlRFIDReceiver = new DeviceControlRFIDReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.njupt.Services.RFIDService");
        this.registerReceiver(deviceControlRFIDReceiver, filter);
    }

    private void unregisterDeviceControlRFIDReceiver() {
        Log.i("Laboratory", "X 注销广播接收器deviceControlRFIDReceiver");
        this.unregisterReceiver(deviceControlRFIDReceiver);
    }

    private void registerEnvironmentReceiver() {
        Log.i("Laboratory", "√ 注册广播接收器environmentReceiver");
        environmentReceiver = new EnvironmentReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.njupt.Services.EvnService");
        this.registerReceiver(environmentReceiver, filter);
    }

    private void registerOpenBookRFIDReceiver() {
        Log.i("Laboratory", "√ 注册广播接收器openBookRFIDReceiver");
        openBookRFIDReceiver = new OpenBookRFIDReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.njupt.Services.RFIDService");
        this.registerReceiver(openBookRFIDReceiver, filter);
    }

    private void unregisterOpenBookRFIDReceiver() {
        Log.i("Laboratory", "X 注销广播接收器openBookRFIDReceiver");
        this.unregisterReceiver(openBookRFIDReceiver);
    }

    public class AttendanceRFIDReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            final String RFID = bundle.getString("SerialPortString");
            checkIdDialogNoDismissBuilder = new DialogNoDismiss.Builder(MainActivity.this);
            checkIdDialogNoDismissBuilder.setTitle("请稍后...");
            checkIdDialogNoDismissBuilder.create();
            final String LabId = sharedPreference_ClassroomNum.getString("LabId", "");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String userAttendanceInfo = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                + "AttendanceByRFID?RFID=" + RFID + "&LabId=" + LabId);
                        Log.i("Laboratory", "请求服务器AttendanceByRFID接口，卡号:" + RFID + "  LabId:" + LabId);
                        Log.i("Laboratory", "请求到：" + userAttendanceInfo);
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                if (userAttendanceInfo.equals("error_calss_not_found")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("当前此教室无课！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                if (userAttendanceInfo.equals("error_user_not_found")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("用户不存在！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                if (userAttendanceInfo.equals("error_duplicate_attendance")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("重复考勤！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                if (userAttendanceInfo.equals("error_lab")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("此实验室当前课程没有此用户！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                if (userAttendanceInfo.equals("request_error") || userAttendanceInfo.equals("server_error_500")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("服务器错误！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                JSONObject resultJsonObject = JSONObject.parseObject(userAttendanceInfo);
                                if (resultJsonObject != null) {
                                    if (resultJsonObject.getString("type").equals("normalCourse")) {
                                        JSONObject jsonObject = JSONObject.parseObject(resultJsonObject.getString("data"));
                                        DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                        builder.setTitle(jsonObject.getString("userName") + "考勤成功\n\n        "
                                                + jsonObject.getString("attendanceType"))
                                                .create(5000);
                                        checkIdDialogNoDismissBuilder.cancel();
                                        serialPortOperation.open_REID();
                                    }
                                    if (resultJsonObject.getString("type").equals("openBookingCome")) {
                                        JSONObject jsonObject = JSONObject.parseObject(resultJsonObject.getString("data"));
                                        DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                        builder.setTitle(jsonObject.getString("userName") + " 签到成功\n\n你预约的桌号是："
                                                + jsonObject.getString("bookTableId"))
                                                .create(5000);
                                        checkIdDialogNoDismissBuilder.cancel();
                                        serialPortOperation.open_REID();
                                        serialPortOperation.open_Desk_Power();
                                    }
                                    if (resultJsonObject.getString("type").equals("openBookingLeave")) {
                                        JSONObject jsonObject = JSONObject.parseObject(resultJsonObject.getString("data"));
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH小时mm分ss秒", Locale.getDefault());
                                        DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                        builder.setTitle(jsonObject.getString("userName") + "签离成功 \n\n实验时长："
                                                + sdf.format(new Date(Long.parseLong(jsonObject.getString("totalTime")))))
                                                .create(5000);
                                        checkIdDialogNoDismissBuilder.cancel();
                                        serialPortOperation.open_REID();
                                        serialPortOperation.close_Desk_Power();
                                    }
                                }

                            }
                        });
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!MainActivity.this.isFinishing()) {
                                    checkIdDialogNoDismissBuilder.cancel();
                                    DialogNoDismiss.Builder builder = new DialogNoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("请求失败，请检查网络！")
                                            .create();
                                    serialPortOperation.open_REID();
                                }
                            }
                        });
                    }
                }
            });
            thread.start();
            Log.i("Laboratory", "接收到串口信息======" + RFID);
        }
    }

    public class TeachLogRFIDReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            final String RFID = bundle.getString("SerialPortString");
            checkIdDialogNoDismissBuilder.cancel();
            checkIdDialogNoDismissBuilder.setTitle("请稍后...");
            checkIdDialogNoDismissBuilder.create();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String CheckCardIdentityInfo = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                + "CheckCardIdentity?RFID=" + RFID);
                        Log.i("Laboratory", "请求服务器CheckCardIdentity接口，卡号:" + RFID);
                        Log.i("Laboratory", "请求到" + CheckCardIdentityInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (CheckCardIdentityInfo.equals("error_card_id") || CheckCardIdentityInfo.equals("server_error_500")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("服务器错误！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                if (CheckCardIdentityInfo.equals("error_user_not_found")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("用户不存在！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                JSONObject jsonObject = JSONObject.parseObject(CheckCardIdentityInfo);
                                if (jsonObject != null) {
                                    if (jsonObject.getString("userIdentity").equals("1")) {
                                        serialPortOperation.open_REID();
                                        checkIdDialogNoDismissBuilder.cancel();
                                        Intent intent = new Intent(MainActivity.this, TeachingLog.class);
                                        startActivity(intent);
                                    }
                                    if (!jsonObject.getString("userIdentity").equals("1")) {
                                        DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                        builder.setTitle("请刷教师卡！")
                                                .create(3000);
                                        checkIdDialogNoDismissBuilder.cancel();
                                        serialPortOperation.open_REID();
                                    }

                                }

                            }
                        });
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!MainActivity.this.isFinishing()) {
                                    checkIdDialogNoDismissBuilder.cancel();
                                    DialogNoDismiss.Builder builder = new DialogNoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("请求失败，请检查网络！")
                                            .create();
                                    serialPortOperation.open_REID();
                                }
                            }
                        });
                    }
                }
            });
            thread.start();
        }
    }

    public class ReportRepairRFIDReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            final String RFID = bundle.getString("SerialPortString");
            checkIdDialogNoDismissBuilder.cancel();
            checkIdDialogNoDismissBuilder.setTitle("请稍后...");
            checkIdDialogNoDismissBuilder.create();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String CheckCardIdentityInfo = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                + "CheckCardIdentity?RFID=" + RFID);
                        Log.i("Laboratory", "请求服务器CheckCardIdentity接口，卡号:" + RFID);
                        Log.i("Laboratory", "请求到" + CheckCardIdentityInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (CheckCardIdentityInfo.equals("error_card_id") || CheckCardIdentityInfo.equals("server_error_500")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("服务器错误！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                if (CheckCardIdentityInfo.equals("error_user_not_found")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("用户不存在！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                JSONObject jsonObject = JSONObject.parseObject(CheckCardIdentityInfo);
                                if (jsonObject != null) {
                                    if (jsonObject.getString("userIdentity").equals("1")) {
                                        serialPortOperation.open_REID();
                                        checkIdDialogNoDismissBuilder.cancel();
                                        Intent intent = new Intent(MainActivity.this, ReportRepair.class);
                                        intent.putExtra("UserInfo", CheckCardIdentityInfo);
                                        intent.putExtra("LabInfo", sharedPreference_ClassroomNum.getString("LabInfo", ""));
                                        Log.i("Laboratory", "界面跳出，传递参数有：" + CheckCardIdentityInfo + sharedPreference_ClassroomNum.getString("LabInfo", ""));
                                        startActivity(intent);
                                    }
                                    if (!jsonObject.getString("userIdentity").equals("1")) {
                                        DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                        builder.setTitle("请刷教师卡！")
                                                .create(3000);
                                        checkIdDialogNoDismissBuilder.cancel();
                                        serialPortOperation.open_REID();
                                    }

                                }

                            }
                        });
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!MainActivity.this.isFinishing()) {
                                    checkIdDialogNoDismissBuilder.cancel();
                                    DialogNoDismiss.Builder builder = new DialogNoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("请求失败，请检查网络！")
                                            .create();
                                    serialPortOperation.open_REID();
                                }
                            }
                        });
                    }
                }
            });
            thread.start();
        }
    }

    public class DeviceControlRFIDReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            final String RFID = bundle.getString("SerialPortString");
            checkIdDialogNoDismissBuilder.cancel();
            checkIdDialogNoDismissBuilder.setTitle("请稍后...");
            checkIdDialogNoDismissBuilder.create();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String CheckCardIdentityInfo = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                + "CheckCardIdentity?RFID=" + RFID);
                        final String LabInfo = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                + "getLabInfoById?LabId=" + sharedPreference_ClassroomNum.getString("LabId", ""));
                        Log.i("Laboratory", "请求服务器CheckCardIdentity接口，卡号:" + RFID);
                        Log.i("Laboratory", "请求到" + CheckCardIdentityInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (CheckCardIdentityInfo.equals("error_card_id") || CheckCardIdentityInfo.equals("server_error_500")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("服务器错误！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                if (CheckCardIdentityInfo.equals("error_user_not_found")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("用户不存在！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                JSONObject jsonObject = JSONObject.parseObject(CheckCardIdentityInfo);
                                if (jsonObject != null) {
                                    if (jsonObject.getString("userIdentity").equals("1")) {
                                        serialPortOperation.open_REID();
                                        checkIdDialogNoDismissBuilder.cancel();
                                        Intent intent = new Intent(MainActivity.this, DeviceControl.class);
                                        intent.putExtra("UserInfo", CheckCardIdentityInfo);
                                        intent.putExtra("LabInfo", LabInfo);
                                        Log.i("Laboratory", "界面跳出，传递参数有：" + CheckCardIdentityInfo + LabInfo);
                                        startActivity(intent);
                                    }
                                    if (!jsonObject.getString("userIdentity").equals("1")) {
                                        DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                        builder.setTitle("请刷教师卡！")
                                                .create(3000);
                                        checkIdDialogNoDismissBuilder.cancel();
                                        serialPortOperation.open_REID();
                                    }

                                }

                            }
                        });
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!MainActivity.this.isFinishing()) {
                                    checkIdDialogNoDismissBuilder.cancel();
                                    DialogNoDismiss.Builder builder = new DialogNoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("请求失败，请检查网络！")
                                            .create();
                                    serialPortOperation.open_REID();
                                }
                            }
                        });
                    }
                }
            });
            thread.start();
        }
    }

    public class EnvironmentReceiver extends BroadcastReceiver {

        DialogNoDismiss.Builder VOCDialogNoDismissBuilder; //有机气体过高警告弹框
        DialogNoDismiss.Builder ShuijinDialogNoDismissBuilder; //水浸异常极高弹框

        boolean isVOCDialog = false;
        boolean isShuijinDialog = false;

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            final String EnvironmentInfo = bundle.getString("SerialPortString");
            indexTextTemp.setText("温度：" + EnvironmentInfo.split(",")[0] + " ℃");
            indexTextHumidity.setText("湿度：" + EnvironmentInfo.split(",")[1] + " %");
            indexTextVOC.setText("VOC：" + EnvironmentInfo.split(",")[2] + " ppm");
            if (Integer.valueOf(EnvironmentInfo.split(",")[2]) > 30) {
                if (!isVOCDialog) {
                    VOCDialogNoDismissBuilder = new DialogNoDismiss.Builder(MainActivity.this);
                    VOCDialogNoDismissBuilder.setTitle("检测到有机气体浓度过高！")
                            .create();
                    isVOCDialog = true;
                }
            } else {
                if (isVOCDialog) VOCDialogNoDismissBuilder.cancel();
                isVOCDialog = false;
            }
            if (EnvironmentInfo.split(",")[3].equals("0")) {
                indexTextShuijin.setText("水浸：正常");
                if (isShuijinDialog) ShuijinDialogNoDismissBuilder.cancel();
                isShuijinDialog = false;
            }
            if (EnvironmentInfo.split(",")[3].equals("1")) {
                if (!isShuijinDialog) {
                    indexTextShuijin.setText("水浸：异常");
                    ShuijinDialogNoDismissBuilder = new DialogNoDismiss.Builder(MainActivity.this);
                    ShuijinDialogNoDismissBuilder.setTitle("检测到水浸异常！")
                            .create();
                    isShuijinDialog = true;
                }
            }
            if (EnvironmentInfo.split(",")[4].equals("0")) indexTextDoor1.setText("前门：关");
            if (EnvironmentInfo.split(",")[4].equals("1")) indexTextDoor1.setText("前门：开");
            if (EnvironmentInfo.split(",")[5].equals("0")) indexTextPower.setText("电源：关");
            if (EnvironmentInfo.split(",")[5].equals("1")) indexTextPower.setText("电源：开");
            if (EnvironmentInfo.split(",")[6].equals("0")) indexTextFangdao.setText("防盗：无人");
            if (EnvironmentInfo.split(",")[6].equals("1")) indexTextFangdao.setText("防盗：有人");
        }
    }

    public class OpenBookRFIDReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            final String RFID = bundle.getString("SerialPortString");
            checkIdDialogNoDismissBuilder.cancel();
            checkIdDialogNoDismissBuilder.setTitle("请稍后...");
            checkIdDialogNoDismissBuilder.create();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String CheckCardIdentityInfo = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                + "CheckCardIdentity?RFID=" + RFID);
                        Log.i("Laboratory", "请求服务器CheckCardIdentity接口，卡号:" + RFID);
                        Log.i("Laboratory", "请求到" + CheckCardIdentityInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (CheckCardIdentityInfo.equals("error_card_id") || CheckCardIdentityInfo.equals("server_error_500")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("服务器错误！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                if (CheckCardIdentityInfo.equals("error_user_not_found")) {
                                    DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("用户不存在！")
                                            .create(3000);
                                    checkIdDialogNoDismissBuilder.cancel();
                                    serialPortOperation.open_REID();
                                    return;
                                }
                                JSONObject jsonObject = JSONObject.parseObject(CheckCardIdentityInfo);
                                if (jsonObject != null) {
                                    if (jsonObject.getString("userIdentity").equals("2")) {
                                        serialPortOperation.open_REID();
                                        checkIdDialogNoDismissBuilder.cancel();
                                        Intent intent = new Intent(MainActivity.this, OpenLabBooking.class);
                                        intent.putExtra("UserInfo", CheckCardIdentityInfo);
                                        intent.putExtra("LabInfo", sharedPreference_ClassroomNum.getString("LabInfo", ""));
                                        Log.i("Laboratory", "界面跳出，传递参数有：" + CheckCardIdentityInfo + sharedPreference_ClassroomNum.getString("LabInfo", ""));
                                        startActivity(intent);
                                    }
                                    if (!jsonObject.getString("userIdentity").equals("2")) {
                                        DialogAutoDismiss.Builder builder = new DialogAutoDismiss.Builder(MainActivity.this);
                                        builder.setTitle("请刷学生卡！")
                                                .create(3000);
                                        checkIdDialogNoDismissBuilder.cancel();
                                        serialPortOperation.open_REID();
                                    }

                                }

                            }
                        });
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!MainActivity.this.isFinishing()) {
                                    checkIdDialogNoDismissBuilder.cancel();
                                    DialogNoDismiss.Builder builder = new DialogNoDismiss.Builder(MainActivity.this);
                                    builder.setTitle("请求失败，请检查网络！")
                                            .create();
                                    serialPortOperation.open_REID();
                                }
                            }
                        });
                    }
                }
            });
            thread.start();
        }
    }
}