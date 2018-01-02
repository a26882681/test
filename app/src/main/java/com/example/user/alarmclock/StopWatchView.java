package com.example.user.alarmclock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by GoroShigeno on 2018/1/2.
 */

public class StopWatchView extends LinearLayout {
    public StopWatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvHour=(TextView) findViewById(R.id.timeHour);
        tvHour.setText("0");
        tvMin=(TextView) findViewById(R.id.timeMin);
        tvMin.setText("0");
        tvSec=(TextView) findViewById(R.id.timeSec);
        tvSec.setText("0");
        tvMSec=(TextView) findViewById(R.id.timeMSec);
        tvMSec.setText("0");

        btnLop=(Button) findViewById(R.id.btnSWLop);
        btnLop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                adapter.insert(String.format("%d:%d:%d:%d",tenMSecs/100/60/60,tenMSecs/100/60%60,tenMSecs/100%60,tenMSecs%100),0);
            }
        });
        btnPause=(Button) findViewById(R.id.btnSWPause);
        btnPause.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                stopTimer();
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
                btnLop.setVisibility(View.GONE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });
        btnReset=(Button) findViewById(R.id.btnSWReset);
        btnReset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                stopTimer();
                tenMSecs=0;
                adapter.clear();
                btnLop.setVisibility(View.GONE);
                btnReset.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });
        btnResume=(Button) findViewById(R.id.btnSWResume);
        btnResume.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startTimer();
                btnPause.setVisibility(View.VISIBLE);
                btnResume.setVisibility(View.GONE);
                btnReset.setVisibility(View.GONE);
                btnLop.setVisibility(View.VISIBLE);
            }
        });
        btnStart=(Button) findViewById(R.id.btnSWStart);
        btnStart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startTimer();
                btnPause.setVisibility(View.VISIBLE);
                btnLop.setVisibility(View.VISIBLE);
                btnStart.setVisibility(View.GONE);
            }
        });
        btnLop.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnStart.setVisibility(View.VISIBLE);
        lvTimeList=(ListView) findViewById(R.id.lvWatchTimeList);
        adapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1);
        lvTimeList.setAdapter(adapter);
        showTimeTask=new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
            }
        };
        timer.schedule(showTimeTask,200,200);
    }

    private void  startTimer(){
        if(timerTask==null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    tenMSecs++;
                }
            };
            timer.schedule(timerTask, 10, 10);



        }

    }
    private  void stopTimer(){
        if(timerTask!=null){
            timerTask.cancel();
            timerTask=null;
        }
    }

    private int tenMSecs=0;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private TimerTask showTimeTask = null;
    private TextView tvHour,tvMin,tvSec,tvMSec;
    private Button btnStart,btnResume,btnReset,btnPause,btnLop;
    private ListView lvTimeList;
    private ArrayAdapter<String> adapter;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case MSG_WHAT_SHOW_TIME:
                    tvHour.setText(tenMSecs/100/60/60+"");
                    tvMin.setText(tenMSecs/100/60%60+"");
                    tvSec.setText(tenMSecs/100%60+"");
                    tvMSec.setText(tenMSecs%100+"");
                    break;
                default:
                    break;
            }
        }
    };
    private  static  final  int MSG_WHAT_SHOW_TIME=1;


    public void onDestory() {
        timer.cancel();
    }
}
