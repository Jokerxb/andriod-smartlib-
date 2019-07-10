package com.njupt.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.njupt.CustomComponents.DialogNoDismiss;
import com.njupt.HttpConnection.PostUtils;
import com.njupt.POJO.ReportRepairInfo;
import com.njupt.R;
import com.njupt.Utils.getPropertiesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ReportRepair extends Activity {

    EditText TeacherId;
    EditText TeacherName;
    EditText ReportDate;
    EditText ReportLab;
    Spinner ReportTableNum;
    Spinner ReportInstrumentName;
    EditText ReportInstrumentId;
    EditText ReportDamageContent;
    EditText ReportRemarks;
    Button Submit;
    TextView ReportLog;
    TextView repairDeal;
    ImageButton closeButton;

    Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏

        setContentView(R.layout.activity_report_repair);

        FindView();

        setFormInfo();

        ViewOnClick();
    }

    private void FindView() {
        TeacherId = (EditText) findViewById(R.id.repair_edit_teacherid);
        TeacherName = (EditText) findViewById(R.id.repair_edit_teachername);
        ReportDate = (EditText) findViewById(R.id.repair_edit_report_date);
        ReportLab = (EditText) findViewById(R.id.repair_edit_lab);
        ReportTableNum = (Spinner) findViewById(R.id.repair_spinner_table);
        ReportInstrumentName = (Spinner) findViewById(R.id.repair_spinner_instrumentName);
        ReportRemarks = (EditText) findViewById(R.id.repair_edit_remarks);
        ReportInstrumentId = (EditText) findViewById(R.id.repair_edit_report_instrumentId);
        ReportDamageContent = (EditText) findViewById(R.id.repair_edit_damageInfo);
        Submit = (Button) findViewById(R.id.repair_button_submit);
        ReportLog = (TextView) findViewById(R.id.repair_text_reportLog);
        repairDeal = (TextView) findViewById(R.id.repair_text_repairDeal);
        closeButton = (ImageButton) findViewById(R.id.repair_imageButton_close);
    }

    private void setFormInfo() {
        Intent intent = getIntent();
        String UserInfo = intent.getStringExtra("UserInfo");
        JSONObject userJsonObject = JSONObject.parseObject(UserInfo);
        TeacherId.setText(userJsonObject.getString("userId"));
        TeacherName.setText(userJsonObject.getString("userName"));
        String LabInfo = intent.getStringExtra("LabInfo");
        JSONObject labJsonObject = JSONObject.parseObject(LabInfo);
        ReportLab.setText(labJsonObject.getString("labName"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        date = new Date();
        ReportDate.setText(sdf.format(date));
    }

    private void ViewOnClick() {
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ReportRepairInfo reportRepairInfo = new ReportRepairInfo();
                        reportRepairInfo.setReporterId(TeacherId.getText().toString());
                        reportRepairInfo.setReporterName(TeacherName.getText().toString());
                        reportRepairInfo.setInstrumentLabName(ReportLab.getText().toString());
                        reportRepairInfo.setInstrumentLabId(Integer.valueOf(JSONObject.parseObject(getIntent()
                                .getStringExtra("LabInfo")).getString("labId")));
                        reportRepairInfo.setReportTime(date);
                        reportRepairInfo.setInstrumentTableId(Integer.valueOf(ReportTableNum.getSelectedItem().toString()));
                        reportRepairInfo.setInstrumentName(ReportInstrumentName.getSelectedItem().toString());
                        reportRepairInfo.setInstrumentId(ReportInstrumentId.getText().toString());
                        reportRepairInfo.setDamageInfo(ReportDamageContent.getText().toString());
                        reportRepairInfo.setRemarks(ReportRemarks.getText().toString());
                        String s = PostUtils.PostString(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                        + "ReportRepair",
                                JSONObject.toJSONString(reportRepairInfo));
                        Log.i("Laboratory", "向ReportRepair接口POST数据：" + JSONObject.toJSONString(reportRepairInfo));
                        Log.i("Laboratory", "POST返回信息：" + s);
                        if (s.equals("report_success")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DialogNoDismiss.Builder builder = new DialogNoDismiss.Builder(ReportRepair.this);
                                    builder.setTitle("提交成功，工作人员讲尽快处理")
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

        ReportLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportRepair.this, "开发中", Toast.LENGTH_SHORT).show();

            }
        });

        repairDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportRepair.this, "开发中", Toast.LENGTH_SHORT).show();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
