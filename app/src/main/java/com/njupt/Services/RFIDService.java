package com.njupt.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.njupt.SerialPort.ControlSerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class RFIDService extends Service {

    protected InputStream mInputStream;
    private ReadThread mReadThread;
    private String SerialPortString = "";
    ControlSerialPort serialPortOpration;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serialPortOpration = new ControlSerialPort();
        mInputStream = serialPortOpration.open_ttyS1();  //打开ttyS1串口,获取串口信息流

        serialPortOpration.open_REID();  //打开RFID读卡器
        mReadThread = new ReadThread();
        mReadThread.start();
        Log.i("Laboratory", "RFID读卡器接收Service已经开启");

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mReadThread.isAlive() && !mReadThread.isInterrupted()) {
                    Log.i("Laboratory", "RFID读卡器接收线程正常");
                }
                if (!mReadThread.isAlive()) {
                    Log.i("Laboratory", "RFID读卡器接收线程已被杀死");
                    mReadThread.start();
                }
                if (mReadThread.isInterrupted()) {
                    Log.i("Laboratory", "RFID读卡器接收线程异常");
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
                        if (s.equals("*") || SerialPortString.substring(0, 1).equals("*"))
                            SerialPortString += s;
                    } catch (Exception e) {
                       // e.printStackTrace();
                    }
                    if (SerialPortString.contains("#")) {
                        //发送广播
                        Intent intent = new Intent();
                        intent.putExtra("SerialPortString", SerialPortString.substring(1, 9));
                        intent.setAction("com.njupt.Services.RFIDService");
                        sendBroadcast(intent);
                        Log.i("RFIDService", SerialPortString);
                        SerialPortString = "";
                        serialPortOpration.close_REID();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
