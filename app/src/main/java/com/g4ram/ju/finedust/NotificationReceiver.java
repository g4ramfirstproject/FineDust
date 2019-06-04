package com.g4ram.ju.finedust;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.g4ram.ju.finedust.Item.StationDustreturns;
import com.g4ram.ju.finedust.R;

public class NotificationReceiver extends BroadcastReceiver {

    String mLocation;
    String mDust;
    String mFineDust;
    int image;
    Context mContext;
    Intent mIntent;
    NotificationCompat.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("리시버 체크", "잘왔으요");

        mContext = context;
        mIntent = intent;

        builder = new NotificationCompat.Builder(context, "default");
        builder.setSmallIcon(R.drawable.logo2);

        CurrentLocation currentLocation = new CurrentLocation(context);
        currentLocation.tmLookup(mhandler);

    }

    Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                StationDustreturns mStationDustreturns = (StationDustreturns) msg.obj;
                StationDustreturns.list dustValueList = mStationDustreturns.getList().get(0);
                //이미지랑, 나쁜수준 텍스트 바꿔주는 컨버터
                DustLevelConverter converter = new DustLevelConverter();
                //컨버터에 이미자와, 레벨 맞춰준다.
                converter.setMainImageAndLevelText(dustValueList.getPm10Value(),dustValueList.getPm25Value());

                mLocation = mStationDustreturns.getStationName();
                image = converter.getReturnImage();
                mDust = dustValueList.getPm10Value();
                mFineDust = dustValueList.getPm25Value();

                builder.setContentTitle(mLocation);
                builder.setContentText("미세먼지 : " + mDust + "  초미세먼지 : " + mFineDust);

                PendingIntent operation = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(operation);

                Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(),
                        image);

                builder.setLargeIcon(largeIcon);
                builder.setColor(Color.parseColor("#FF7062"));

                Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(mContext,
                        RingtoneManager.TYPE_NOTIFICATION);

                builder.setSound(ringtoneUri);

                long[] vibrate = {0, 100, 200, 300};
                builder.setVibrate(vibrate);
                builder.setAutoCancel(true);

                NotificationManager manager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                    manager.createNotificationChannel(new NotificationChannel("default","기본 채널",
                            NotificationManager.IMPORTANCE_DEFAULT));
                }
                manager.notify(1,builder.build());
            }
            return false;
        }
    });
}
