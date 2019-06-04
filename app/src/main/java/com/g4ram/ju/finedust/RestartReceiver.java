package com.g4ram.ju.finedust;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class RestartReceiver extends BroadcastReceiver {

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    String mAlarmCheck;

    int mHour, mMinute;

    AlarmManager mAlarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.e("리스타트", "리스타트");
            mSharedPreferences = context.getSharedPreferences("alarm",MODE_PRIVATE);
            mAlarmCheck = mSharedPreferences.getString("alarmCheck","false");

            if(mAlarmCheck.equals("true")){

                mHour = mSharedPreferences.getInt("alarmHour", 00);
                mMinute = mSharedPreferences.getInt("alarmMinute", 00);

                mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

                Intent i = new Intent(context, NotificationReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();
                //알람시간 calendar에 set해주기
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), mHour, mMinute, 0);

                if(calendar.getTimeInMillis() < System.currentTimeMillis()){
                    calendar.add(Calendar.DAY_OF_MONTH,1);
                }

                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),24 * 60 * 60 * 1000, sender);
            }
        }
    }
}
