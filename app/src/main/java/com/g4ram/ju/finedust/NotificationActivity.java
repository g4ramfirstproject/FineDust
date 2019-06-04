package com.g4ram.ju.finedust;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.text.format.DateFormat;

import com.g4ram.ju.finedust.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotificationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    Switch mAlarmSwitch;
    Button mTimePicker;
    LinearLayout mSettingLayout;

    TimePickerDialog mTimePickerDialog;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    String mAlarmCheck;
    Calendar mCalendar;
    AlarmManager mAlarmManager;

    int mHour;
    int mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mSharedPreferences = getSharedPreferences("alarm",MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mAlarmCheck = mSharedPreferences.getString("alarmCheck","false");
        mHour = mSharedPreferences.getInt("alarmHour", 00);
        mMinute = mSharedPreferences.getInt("alarmMinute", 00);

        mCalendar = Calendar.getInstance();
        mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        timePickerMake();
        viewSetup();

    }

    private void viewSetup(){
        mSettingLayout = findViewById(R.id.SettingLayout);
        mTimePicker = findViewById(R.id.TimePicker);

        mTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
                mTimePickerDialog.show();
            }
        });

        mAlarmSwitch = findViewById(R.id.AlarmSwitch);
        mAlarmSwitch.setOnCheckedChangeListener(this);
        if(mAlarmCheck.equals("true")){
            mAlarmSwitch.setChecked(true);

            setAlarm(true);
        }else if(mAlarmCheck.equals("false")){
            mAlarmSwitch.setChecked(false);

            setAlarm(false);
        }
    }

    public void timePickerMake(){
        mTimePickerDialog = new TimePickerDialog(NotificationActivity.this, new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    mHour = timePicker.getHour(); // == i
                    mMinute = timePicker.getMinute(); // == i1
                    mEditor.putInt("alarmHour", mHour);
                    mEditor.putInt("alarmMinute", mMinute);
                    mEditor.apply();
                    setAlarmTime();
                    setAlarm(true);

                }else{
                    mHour = timePicker.getCurrentHour();
                    mMinute = timePicker.getCurrentMinute();
                    mEditor.putInt("alarmHour", mHour);
                    mEditor.putInt("alarmMinute", mMinute);
                    mEditor.apply();
                    setAlarmTime();
                    setAlarm(true);

                }
            }
        }, mHour, mMinute, DateFormat.is24HourFormat(this));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked){
            //Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            mEditor.putString("alarmCheck", "true");
            mEditor.apply();
            mSettingLayout.setVisibility(View.VISIBLE);
            setAlarmTime();
            setAlarm(true);
        }
        else {
            //Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
            mEditor.putString("alarmCheck", "false");
            mEditor.apply();
            mSettingLayout.setVisibility(View.INVISIBLE);
            setAlarm(false);
        }
    }

    public void setAlarmTime(){
        mCalendar.set(Calendar.HOUR_OF_DAY,mHour);
        mCalendar.set(Calendar.MINUTE,mMinute);
        Date time = mCalendar.getTime();
        String formattedDate = new SimpleDateFormat("a hh시 mm분").format(time);
        mTimePicker.setText(formattedDate);
    }

    public void setAlarm(boolean check){

        Intent intent = new Intent(NotificationActivity.this, NotificationReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(NotificationActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(check){
            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), mHour, mMinute, 0);

            //알람 예약
            if(calendar.getTimeInMillis() < System.currentTimeMillis()){
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),24 * 60 * 60 * 1000, sender);
            //mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            Log.e("리시버 보내기", "알림예약");
            Log.e("리시버 보내기", String.valueOf(calendar.getTime()));
        }
        else{
            mAlarmManager.cancel(sender);
            Log.e("리시버 취소", "알림취소");
        }
    }
}
