package com.njupt.SerialPort;

import android.util.Log;
import android.view.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

public class ControlSerialPort {

    protected SerialPort mSerialPort1;
    protected OutputStream mOutputStream1;
    protected InputStream mInputStream1;
    protected SerialPort mSerialPort4;
    protected OutputStream mOutputStream4;
    protected InputStream mInputStream4;

    public ControlSerialPort(){
        try {
            mSerialPort1 = new SerialPort(new File("/dev/ttyS1"), 1200, 0);
            mInputStream1 = mSerialPort1.getInputStream();
            mSerialPort4 = new SerialPort(new File("/dev/ttyS4"), 115200, 0);
            mInputStream4 = mSerialPort4.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream open_ttyS1() {
        try {
            mSerialPort1 = new SerialPort(new File("/dev/ttyS1"), 1200, 0);
            mInputStream1 = mSerialPort1.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mInputStream1;
    }

    public InputStream open_ttyS4() {
        try {
            mSerialPort4 = new SerialPort(new File("/dev/ttyS4"), 115200, 0);
            mInputStream4 = mSerialPort4.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mInputStream4;
    }

    public void open_REID() {
        mOutputStream1 = mSerialPort1.getOutputStream();
        byte[] openRFID = {0x55, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x7F};
        try {
            mOutputStream1.write(openRFID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Laboratory", "打开RFID");
    }

    public void close_REID() {
        mOutputStream1 = mSerialPort1.getOutputStream();
        byte[] openRFID = {0x55, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
        try {
            mOutputStream1.write(openRFID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Laboratory", "关闭RFID");
    }

    public void open_Computer() {
        mOutputStream1 = mSerialPort1.getOutputStream();
        byte[] openRFID = {0x55, 0x02, 0x01, 0x00, 0x00, 0x00, 0x00, 0x7F};
        try {
            mOutputStream1.write(openRFID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Laboratory", "打开Computer");
    }

    public void close_Computer() {
        mOutputStream1 = mSerialPort1.getOutputStream();
        byte[] openRFID = {0x55, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
        try {
            mOutputStream1.write(openRFID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Laboratory", "关闭Computer");
    }

    public void open_Projector() {
        mOutputStream1 = mSerialPort1.getOutputStream();
        byte[] openRFID = {0x55, 0x03, 0x01, 0x00, 0x00, 0x00, 0x00, 0x7F};
        try {
            mOutputStream1.write(openRFID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Laboratory", "打开Projector");
    }

    public void close_Projector() {
        mOutputStream1 = mSerialPort1.getOutputStream();
        byte[] openRFID = {0x55, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
        try {
            mOutputStream1.write(openRFID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Laboratory", "关闭Projector");
    }

    public void open_Desk_Power() {
        mOutputStream1 = mSerialPort1.getOutputStream();
        byte[] openRFID = {0x55, 0x04, 0x01, 0x00, 0x00, 0x00, 0x00, 0x7F};
        try {
            mOutputStream1.write(openRFID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Laboratory", "打开所有Desk_Power");
    }

    public void close_Desk_Power() {
        mOutputStream1 = mSerialPort1.getOutputStream();
        byte[] openRFID = {0x55, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
        try {
            mOutputStream1.write(openRFID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Laboratory", "关闭所有Desk_Power");
    }

        public void open_Curtain() {
        mOutputStream1 = mSerialPort1.getOutputStream();
        byte[] openRFID = {0x55, 0x05, 0x01, 0x00, 0x00, 0x00, 0x00, 0x7F};
        try {
            mOutputStream1.write(openRFID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Laboratory", "打开Curtain");
    }

    public void close_Curtain() {
        mOutputStream1 = mSerialPort1.getOutputStream();
        byte[] openRFID = {0x55, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
        try {
            mOutputStream1.write(openRFID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Laboratory", "关闭Curtain");
    }


}
