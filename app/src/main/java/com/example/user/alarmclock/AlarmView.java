package com.example.user.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.lang.Object;


public class AlarmView extends LinearLayout {
    public AlarmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AlarmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public AlarmView(Context context) {
        super(context);
        init();
    }
    private void init(){
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        adapter = new ArrayAdapter<AlarmDate>(getContext(),android.R.layout.simple_list_item_1);
        btnAddAlarm = findViewById(R.id.btnAddAlarm);
        lvAlarmList = findViewById(R.id.lvAlarmList);
        readSavedAlarmList();
        lvAlarmList.setAdapter(adapter);
        /*adapter = new ArrayAdapter<AlarmDate>(getContext(),android.R.layout.simple_list_item_1);
        lvAlarmList.setAdapter(adapter);
        adapter.add(new AlarmDate(System.currentTimeMillis()));*/

        btnAddAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                addAlarm();
            }
        });

        lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final String[] list_item = {"刪除"};
                AlertDialog.Builder dialog_list = new AlertDialog.Builder(getContext());
                dialog_list.setTitle("操作選項");
                dialog_list.setItems(list_item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                deleteAlarm(position);
                                break;
                            default:
                                break;

                        }
                    }
                }).setNegativeButton("取消",null).show();
                return true;
            }
        });
    }
    private void deleteAlarm(int position){
        AlarmDate ad = adapter.getItem(position);
        adapter.remove(ad);
        saveAlarmList();
        try {
            alarmManager.cancel(PendingIntent.getBroadcast(getContext(),ad.getId(),new Intent(getContext(),AlarmReceiver.class),0));
        }catch (NullPointerException e){

        }

    }
    public  void addAlarm(){

        Calendar c = Calendar.getInstance();
        new TimePickerDialog(getContext(),new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                Calendar currentTime = Calendar.getInstance();
               if(calendar.getTimeInMillis()<=currentTime.getTimeInMillis()){
                    calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);
                }
                AlarmDate ad = new AlarmDate(calendar.getTimeInMillis());
                adapter.add(ad);

                lvAlarmList.setAdapter(adapter);
                //adapter.add(new AlarmDate(System.currentTimeMillis()));
                alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        ad.getTime(), 5*60*1000,
                        PendingIntent.getBroadcast(getContext(),ad.getId(),new Intent(getContext(),AlarmReceiver.class),0));
                saveAlarmList();

            }
        } ,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
    }

    private void saveAlarmList(){
        SharedPreferences.Editor editor =getContext().getSharedPreferences(AlarmView.class.getName(),Context.MODE_PRIVATE).edit();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<adapter.getCount();i++)
        {
            sb.append(adapter.getItem(i).getTime()).append(",");
        }
        if(sb.length()>1){
            String content = sb.toString().substring(0,sb.length()-1);
            editor.putString(KEY_ALARM_LIST,content);
            System.out.println(content);
        }
        else{
            editor.putString(KEY_ALARM_LIST,null);
        }

        editor.commit();
    }
    public void readSavedAlarmList(){
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(),Context.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST,null);
        if(content !=null)
        {
            String[] timeStrings = content.split(",");
            for(String string : timeStrings){
                adapter.add(new AlarmDate(Long.parseLong(string)));         }

        }
    }
    public Button btnAddAlarm;
    public ListView lvAlarmList;
    private static final String KEY_ALARM_LIST = "alarmList";
    public ArrayAdapter<AlarmDate> adapter;
    public AlarmManager alarmManager;

        public static class AlarmDate{
        public  AlarmDate(long time){
            this.time= time;
            date = Calendar.getInstance();
            date.setTimeInMillis(time);
            timeLable = String.format("%d月%d日 %d:%d",
                    date.get(Calendar.MONTH)+1,
                    date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE));

        }
        public long getTime(){
            return time;
        }
        public String getTimeLable(){
            return timeLable;
        }

        public String toString(){
            return getTimeLable();
        }

        public int getId(){
            return (int) (getTime()/1000/60);
        }

        public String timeLable="";
        public long time;
        public Calendar date ;
    }

}
