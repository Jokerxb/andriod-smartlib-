package com.njupt.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.njupt.R;
import com.njupt.SerialPort.ControlSerialPort;


public class DeviceControl extends Activity {
    TextView teacherName;
    TextView openallTextView;
    ImageButton openAll;
    ImageButton computer;
    ImageButton projector;
    ImageButton deskPower;
    ImageButton curtain;
    ImageButton closeButton;

    ControlSerialPort controlSerialPort; //串口操作工具

    public SharedPreferences sharedPreference_DeviceStatus; //记录设备状态的相关数据

    boolean computerIsOpen;
    boolean projectorIsOpen;
    boolean deskPowerIsOpen;
    boolean curtainIsOpen;
    boolean allIsOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏

        setContentView(R.layout.activity_device_control);

        FindView();

        ViewOnClick();

        setPageInfo();

        controlSerialPort = new ControlSerialPort();

    }

    private void FindView() {
        teacherName = (TextView) findViewById(R.id.device_text_title_teacherName);
        openallTextView = (TextView)findViewById(R.id.device_text_openall);
        openAll = (ImageButton) findViewById(R.id.device_button_openall);
        computer = (ImageButton) findViewById(R.id.device_button_computer);
        projector = (ImageButton) findViewById(R.id.device_button_projector);
        deskPower = (ImageButton) findViewById(R.id.device_button_power);
        curtain = (ImageButton) findViewById(R.id.device_button_curtain);
        closeButton = (ImageButton) findViewById(R.id.device_imageButton_close);
    }

    private void ViewOnClick() {
        openAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!allIsOpen) {
                    try {
                        controlSerialPort.open_Computer();
                        Thread.sleep(300);
                        controlSerialPort.open_Curtain();
                        Thread.sleep(300);
                        controlSerialPort.open_Desk_Power();
                        Thread.sleep(300);
                        controlSerialPort.open_Projector();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    openAll.setImageResource(R.drawable.yijian);
                    computer.setImageResource(R.drawable.diannao);
                    projector.setImageResource(R.drawable.touyingyi);
                    deskPower.setImageResource(R.drawable.power);
                    curtain.setImageResource(R.drawable.chuanglian);
                    openallTextView.setText("一键全关");
                    SharedPreferences.Editor editor = sharedPreference_DeviceStatus.edit();
                    editor.putBoolean("computerIsOpen", true);
                    editor.putBoolean("projectorIsOpen", true);
                    editor.putBoolean("deskPowerIsOpen", true);
                    editor.putBoolean("curtainIsOpen", true);
                    editor.putBoolean("allIsOpen", true);
                    editor.apply();
                    computerIsOpen = true;
                    projectorIsOpen = true;
                    deskPowerIsOpen = true;
                    curtainIsOpen = true;
                    allIsOpen = true;
                    return;
                }
                try {
                    controlSerialPort.close_Computer();
                    Thread.sleep(300);
                    controlSerialPort.close_Curtain();
                    Thread.sleep(300);
                    controlSerialPort.close_Desk_Power();
                    Thread.sleep(300);
                    controlSerialPort.close_Projector();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                openAll.setImageResource(R.drawable.yijian_2);
                computer.setImageResource(R.drawable.diannao_2);
                projector.setImageResource(R.drawable.touyingyi_2);
                deskPower.setImageResource(R.drawable.power_2);
                curtain.setImageResource(R.drawable.chuanglian_2);
                openallTextView.setText("一键全开");
                SharedPreferences.Editor editor = sharedPreference_DeviceStatus.edit();
                editor.putBoolean("computerIsOpen", false);
                editor.putBoolean("projectorIsOpen", false);
                editor.putBoolean("deskPowerIsOpen", false);
                editor.putBoolean("curtainIsOpen", false);
                editor.putBoolean("allIsOpen", false);
                editor.apply();
                computerIsOpen = false;
                projectorIsOpen = false;
                deskPowerIsOpen = false;
                curtainIsOpen = false;
                allIsOpen = false;
            }
        });
        computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!computerIsOpen) {
                    controlSerialPort.open_Computer();
                    computer.setImageResource(R.drawable.diannao);
                    SharedPreferences.Editor editor = sharedPreference_DeviceStatus.edit();
                    editor.putBoolean("computerIsOpen", true);
                    editor.apply();
                    computerIsOpen = true;
                    return;
                }
                controlSerialPort.close_Computer();
                computer.setImageResource(R.drawable.diannao_2);
                SharedPreferences.Editor editor = sharedPreference_DeviceStatus.edit();
                editor.putBoolean("computerIsOpen", false);
                editor.apply();
                computerIsOpen = false;

            }
        });
        projector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!projectorIsOpen) {
                    controlSerialPort.open_Projector();
                    projector.setImageResource(R.drawable.touyingyi);
                    SharedPreferences.Editor editor = sharedPreference_DeviceStatus.edit();
                    editor.putBoolean("projectorIsOpen", true);
                    editor.apply();
                    projectorIsOpen = true;
                    return;
                }
                controlSerialPort.close_Projector();
                projector.setImageResource(R.drawable.touyingyi_2);
                SharedPreferences.Editor editor = sharedPreference_DeviceStatus.edit();
                editor.putBoolean("projectorIsOpen", false);
                editor.apply();
                projectorIsOpen = false;
            }
        });
        deskPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!deskPowerIsOpen) {
                    controlSerialPort.open_Desk_Power();
                    deskPower.setImageResource(R.drawable.power);
                    SharedPreferences.Editor editor = sharedPreference_DeviceStatus.edit();
                    editor.putBoolean("deskPowerIsOpen", true);
                    editor.apply();
                    deskPowerIsOpen = true;
                    return;
                }
                controlSerialPort.close_Desk_Power();
                deskPower.setImageResource(R.drawable.power_2);
                SharedPreferences.Editor editor = sharedPreference_DeviceStatus.edit();
                editor.putBoolean("deskPowerIsOpen", false);
                editor.apply();
                deskPowerIsOpen = false;
            }
        });
        curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!curtainIsOpen) {
                    controlSerialPort.open_Curtain();
                    curtain.setImageResource(R.drawable.chuanglian);
                    SharedPreferences.Editor editor = sharedPreference_DeviceStatus.edit();
                    editor.putBoolean("curtainIsOpen", true);
                    editor.apply();
                    curtainIsOpen = true;
                    return;
                }
                controlSerialPort.close_Curtain();
                curtain.setImageResource(R.drawable.chuanglian_2);
                SharedPreferences.Editor editor = sharedPreference_DeviceStatus.edit();
                editor.putBoolean("curtainIsOpen", false);
                editor.apply();
                curtainIsOpen = false;
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setPageInfo() {
        Intent intent = getIntent();
        String UserInfo = intent.getStringExtra("UserInfo");
        JSONObject userInfoJSON = JSONObject.parseObject(UserInfo);
        teacherName.setText(userInfoJSON.getString("userName"));

        sharedPreference_DeviceStatus = this.getSharedPreferences("DeviceStatus", Context.MODE_PRIVATE);
        computerIsOpen = sharedPreference_DeviceStatus.getBoolean("computerIsOpen", false);
        projectorIsOpen = sharedPreference_DeviceStatus.getBoolean("projectorIsOpen", false);
        deskPowerIsOpen = sharedPreference_DeviceStatus.getBoolean("deskPowerIsOpen", false);
        curtainIsOpen = sharedPreference_DeviceStatus.getBoolean("curtainIsOpen", false);
        allIsOpen = sharedPreference_DeviceStatus.getBoolean("allIsOpen", false);
        if (computerIsOpen) computer.setImageResource(R.drawable.diannao);
        if (projectorIsOpen) projector.setImageResource(R.drawable.touyingyi);
        if (deskPowerIsOpen) deskPower.setImageResource(R.drawable.power);
        if (curtainIsOpen) curtain.setImageResource(R.drawable.chuanglian);
        if (allIsOpen){
            openallTextView.setText("一键全关");
            openAll.setImageResource(R.drawable.yijian);
        }else  openallTextView.setText("一键全开");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            controlSerialPort.open_ttyS1();
            controlSerialPort.open_REID();
            finish();
        }
        return true;
    }
}
