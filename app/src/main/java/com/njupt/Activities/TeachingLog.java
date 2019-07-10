package com.njupt.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.alibaba.fastjson.JSONObject;
import com.njupt.CustomComponents.DialogNoDismiss;
import com.njupt.HttpConnection.GetData;
import com.njupt.HttpConnection.PostUtils;
import com.njupt.POJO.TeachingLogInfo;
import com.njupt.R;
import com.njupt.Utils.TimeUtil;
import com.njupt.Utils.getPropertiesUtil;

import java.util.Date;

public class TeachingLog extends Activity {

    EditText TeacherId;
    EditText TeacherName;
    EditText Week;
    EditText CourseDate;
    EditText CourseWeek;
    EditText CourseTime;
    EditText CourseLab;
    EditText CourseName;
    EditText CourseClass;
    EditText CourseStudentNum;
    EditText CourseClassHour;
    Spinner CourseSafeInfo;
    Spinner CourseEvnInfo;
    EditText CourseContent;
    EditText CourseRemarks;
    Button Submit;
    ImageButton closeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏

        setContentView(R.layout.activity_teaching_log);

        FindView();

        getCurrentCourseByLabId();

        ViewOnClick();
    }

    private void FindView() {
        TeacherId = (EditText) findViewById(R.id.teachlog_edit_teacherid);
        TeacherName = (EditText) findViewById(R.id.teachlog_edit_teachername);
        Week = (EditText) findViewById(R.id.teachlog_edit_week);
        CourseDate = (EditText) findViewById(R.id.teachlog_edit_date);
        CourseWeek = (EditText) findViewById(R.id.teachlog_edit_dayofweek);
        CourseTime = (EditText) findViewById(R.id.teachlog_edit_time);
        CourseLab = (EditText) findViewById(R.id.teachlog_edit_lab);
        CourseName = (EditText) findViewById(R.id.teachlog_edit_coursename);
        CourseClass = (EditText) findViewById(R.id.teachlog_edit_classid);
        CourseStudentNum = (EditText) findViewById(R.id.teachlog_edit_studentnum);
        CourseClassHour = (EditText) findViewById(R.id.teachlog_edit_classhour);
        CourseSafeInfo = (Spinner) findViewById(R.id.teachlog_spinner_safeinfo);
        CourseEvnInfo = (Spinner) findViewById(R.id.teachlog_spinner_evninfo);
        CourseContent = (EditText) findViewById(R.id.teachlog_edit_coursecontent);
        CourseRemarks = (EditText) findViewById(R.id.teachlog_edit_remarks);
        Submit = (Button) findViewById(R.id.teachlog_button_submit);
        closeButton = (ImageButton) findViewById(R.id.teachlog_imageButton_close);
    }

    private void ViewOnClick() {
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TeachingLogInfo teachingLogInfo = new TeachingLogInfo();
                        teachingLogInfo.setTeacherId(TeacherId.getText().toString());
                        teachingLogInfo.setTeacherName(TeacherName.getText().toString());
                        teachingLogInfo.setCourseWeek(Week.getText().toString());
                        teachingLogInfo.setCourseDate(new Date());
                        teachingLogInfo.setCourseDayOfWeek(CourseWeek.getText().toString());
                        teachingLogInfo.setCourseTimeOfDay(CourseTime.getText().toString());
                        teachingLogInfo.setCourseLabName(CourseLab.getText().toString());
                        teachingLogInfo.setCourseName(CourseName.getText().toString());
                        teachingLogInfo.setClassId(CourseClass.getText().toString());
                        teachingLogInfo.setClassUserNum(Integer.parseInt(CourseStudentNum.getText().toString()));
                        teachingLogInfo.setCourseClassHour(Integer.parseInt(CourseClassHour.getText().toString()));
                        teachingLogInfo.setCourseSafeInfo(CourseSafeInfo.getSelectedItem().toString());
                        teachingLogInfo.setCourseEnvInfo(CourseEvnInfo.getSelectedItem().toString());
                        teachingLogInfo.setCourseContent(CourseContent.getText().toString());
                        teachingLogInfo.setRemarks(CourseRemarks.getText().toString());
                        String s = PostUtils.PostString(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                        + "TeachingLog",
                                JSONObject.toJSONString(teachingLogInfo));
                        Log.i("Laboratory", "向TeachingLog接口POST数据：" + JSONObject.toJSONString(teachingLogInfo));
                        Log.i("Laboratory", "POST返回信息：" + s);
                        if (s.equals("add_success")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DialogNoDismiss.Builder builder = new DialogNoDismiss.Builder(TeachingLog.this);
                                    builder.setTitle("保存成功，若修改请移步至官网")
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
                    }
                });
                thread.start();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getCurrentCourseByLabId() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String receiveJSON = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                            + "getCurrentCourseByLabId?LabId=1");
                    if (receiveJSON.equals("server_error_500")) { //避免本地调试时因连接云数据库超时服出现500，重连一次
                        receiveJSON = GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                + "getCurrentCourseByLabId?LabId=1");
                    }

                    final String finalReceiveJSON = receiveJSON;
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            if (finalReceiveJSON != null) {
                                final JSONObject jsonObject = JSONObject.parseObject(finalReceiveJSON);
                                TimeUtil timeUtil = new TimeUtil();
                                TeacherId.setText(jsonObject.getString("teacherId"));
                                TeacherName.setText(jsonObject.getString("teacherName"));
                                Week.setText("第" + jsonObject.getString("week") + "周");
                                CourseDate.setText(timeUtil.getDateByTimestamp(Long.parseLong(jsonObject.getString("date"))));
                                CourseWeek.setText(jsonObject.getString("time").substring(0, 3));
                                CourseTime.setText(jsonObject.getString("time").substring(3));
                                CourseLab.setText(jsonObject.getString("labName"));
                                CourseName.setText(jsonObject.getString("courseName"));
                                CourseClass.setText(jsonObject.getString("classId"));
                                CourseStudentNum.setText("32");
                                CourseClassHour.setText(jsonObject.getString("courseTime"));
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        });
        thread.start();
    }
}
