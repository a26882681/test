package com.example.user.alarmclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        /*tabHost.addTab(tabHost.newTabSpec("tabStopWatch").setIndicator("", getResources().getDrawable(R.drawable.alarmclock,null)).setContent(R.id.tabStopWatch));
        tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("鬧鐘").setContent(R.id.tabAlarm));
        tabHost.addTab(tabHost.newTabSpec("tabTime").setIndicator("計時器", ResourcesCompat.getDrawable(getResources(), R.drawable.clock, null)).setContent(R.id.tabTimer));*/
        addTab("碼表",R.drawable.clock,R.id.tabTimer);
        addTab("鬧鐘",R.drawable.alarmclock,R.id.tabAlarm);
        addTab("計時器",R.drawable.stopwatch,R.id.tabStopWatch);

        stopWahtchView=(StopWatchView) findViewById(R.id.tabTimer);
    }

    @Override
    protected void onDestroy() {
        stopWahtchView.onDestory();
        super.onDestroy();
    }

    private void addTab(String label, int drawableId, int content) {
        TabHost.TabSpec spec = tabHost.newTabSpec(label);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        TextView title =tabIndicator.findViewById(R.id.title);
        title.setText(label);
        ImageView icon =tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        spec.setIndicator(tabIndicator);
        spec.setContent(content);
        tabHost.addTab(spec);
    }

    private  StopWatchView stopWahtchView;


}
