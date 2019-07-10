package com.njupt.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.njupt.HttpConnection.GetData;
import com.njupt.SerialPort.ControlSerialPort;
import com.njupt.Utils.getPropertiesUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class EvnService extends Service {

    protected InputStream mInputStream;
    private EvnService.ReadThread mReadThread;
    private String SerialPortString = "";
    ControlSerialPort controlSerialPort;
    SharedPreferences sharedPreference_ClassroomNum;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreference_ClassroomNum = getApplicationContext().getSharedPreferences("classroomNum", Context.MODE_PRIVATE);

        controlSerialPort = new ControlSerialPort();
        mInputStream = controlSerialPort.open_ttyS4();  //打开ttyS4串口,获取串口信息流

        mReadThread = new EvnService.ReadThread();
        mReadThread.start();
        Log.i("Laboratory", "环境信息接收Service已经开启");

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mReadThread.isAlive() && !mReadThread.isInterrupted()) {
                    Log.i("Laboratory", "环境信息接收线程正常");
                }
                if (!mReadThread.isAlive()) {
                    Log.i("Laboratory", "环境信息接收线程已被杀死");
                    mReadThread.start();
                }
                if (mReadThread.isInterrupted()) {
                    Log.i("Laboratory", "环境信息接收线程异常");
                    mReadThread.run();
                }
            }
        };
        timer.schedule(timerTask, 0, 5 * 60 * 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private class ReadThread extends Thread {
        int sendTime = 0;

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    String s = new String(buffer, 0, size);
                    try {
                        if (s.substring(0, 1).equals("#") || SerialPortString.substring(0, 1).equals("#"))
                            SerialPortString += s;
                    } catch (Exception e) { }
                    if (SerialPortString.contains("*")) {
                        //发送广播
                        Intent intent = new Intent();
                        intent.putExtra("SerialPortString", SerialPortString.substring(1, 18));
                        intent.setAction("com.njupt.Services.EvnService");
                        sendBroadcast(intent);
                        sendTime++;
                        if (sendTime == 120) { //一分钟向服务器发送一次环境信息
                            try {
                                GetData.getHtml(getPropertiesUtil.getNetConfigProperties().getProperty("SERVER_URL")
                                        + "EnvInfoUpload?EnvInfo=" + SerialPortString.substring(1, 18)
                                        + "&LabInfo=" + sharedPreference_ClassroomNum.getString("LabInfo",""));
                                Log.i("Laboratory", "向服务器发送环境信息:EnvInfo="
                                        + SerialPortString.substring(1, 18) + "&LabInfo="
                                        + sharedPreference_ClassroomNum.getString("LabInfo",""));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            sendTime = 0;
                        }
                        SerialPortString = "";
                    }
                } catch (IOException e) {
                    return;
                }
            }
        }
    }
}
