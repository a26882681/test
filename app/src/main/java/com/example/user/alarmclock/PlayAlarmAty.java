package com.example.user.alarmclock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 2017/12/29.
 */

public class PlayAlarmAty extends Activity{

    private SensorManager mSensorManager;   //體感(Sensor)使用管理
    private Sensor mSensor;                 //體感(Sensor)類別
    private float mLastX;                    //x軸體感(Sensor)偏移
    private float mLastY;                    //y軸體感(Sensor)偏移
    private float mLastZ;                    //z軸體感(Sensor)偏移
    private double mSpeed;                 //甩動力道數度
    private long mLastUpdateTime;           //觸發時間

    //甩動力道數度設定值 (數值越大需甩動越大力，數值越小輕輕甩動即會觸發)
    private static final int SPEED_SHRESHOLD = 3000;

    private BroadcastReceiver myBrocasReaceiver;
    TextView textView;
    private AlertDialog mAlertDialog = null;
    //觸發間隔時間
    private static final int UPTATE_INTERVAL_TIME = 70;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        //setContentView(R.layout.playalarm_activity);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_layout, null);
        final AlertDialog mAlertDialog = new AlertDialog.Builder(this).setView(view).create();
        mAlertDialog.setTitle("請搖晃數字達到100已關閉鬧鐘");
        mAlertDialog.show();


        textView = findViewById(R.id.textView);
        mp= MediaPlayer.create(this,R.raw.johncena);
        mp.setLooping(true);
        mp.start();




        Intent i = new Intent();
        i.setClass(PlayAlarmAty.this,MyService.class);
        startService(i);

        myBrocasReaceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle myBundle = intent.getExtras();
                int myInt = myBundle.getInt("background_service");
                TextView tv = (TextView) mAlertDialog.findViewById(R.id.tv_dialog);
                tv.setText("目前搖晃次數:"+myInt);
                //textView.setText("目前"+myInt);
                if(myInt>=100){
                    mp.stop();
                    PlayAlarmAty.this.finish();
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter("MyMessage");

        registerReceiver(myBrocasReaceiver,intentFilter);


        /*mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        //取得手機Sensor狀態設定
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(SensorListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);*/

    }



    /*SensorEventListener SensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent mSensorEvent) {
            //當前觸發時間
            long mCurrentUpdateTime = System.currentTimeMillis();

            //觸發間隔時間 = 當前觸發時間 - 上次觸發時間
            long mTimeInterval = mCurrentUpdateTime - mLastUpdateTime;

            //若觸發間隔時間< 70 則return;
            if (mTimeInterval < UPTATE_INTERVAL_TIME) return;

            mLastUpdateTime = mCurrentUpdateTime;

            //取得xyz體感(Sensor)偏移
            float x = mSensorEvent.values[0];
            float y = mSensorEvent.values[1];
            float z = mSensorEvent.values[2];

            //甩動偏移速度 = xyz體感(Sensor)偏移 - 上次xyz體感(Sensor)偏移
            float mDeltaX = x - mLastX;
            float mDeltaY = y - mLastY;
            float mDeltaZ = z - mLastZ;

            mLastX = x;
            mLastY = y;
            mLastZ = z;

            //體感(Sensor)甩動力道速度公式
            mSpeed = Math.sqrt(mDeltaX * mDeltaX + mDeltaY * mDeltaY + mDeltaZ * mDeltaZ)/ mTimeInterval * 10000;
            int num =0;
            //若體感(Sensor)甩動速度大於等於甩動設定值則進入 (達到甩動力道及速度)
            if (mSpeed >= SPEED_SHRESHOLD){

                //達到搖一搖甩動後要做的事情
                //Log.d("TAG","搖一搖中...");
                mp.stop();
                PlayAlarmAty.this.finish();

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };*/
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mp.stop();
        mp.release();
    }

    private MediaPlayer mp;






}
