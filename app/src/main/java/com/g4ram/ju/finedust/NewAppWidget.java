package com.g4ram.ju.finedust;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

import com.g4ram.ju.finedust.Item.StationDustreturns;
import com.g4ram.ju.finedust.R;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String ACTION_REFRESH = "android.appwidget.action.APPWIDGET_UPDATE";

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        Handler mhandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0) {
                    StationDustreturns mStationDustreturns = (StationDustreturns) msg.obj;
                    StationDustreturns.list dustValueList = mStationDustreturns.getList().get(0);
                    DustLevelConverter converter = new DustLevelConverter();
                    converter.setMainImageAndLevelText(dustValueList.getPm10Value(),dustValueList.getPm25Value());
                    views.setTextViewText(R.id.appwidget_location, mStationDustreturns.getStationName());
                    views.setTextViewText(R.id.appwidget_dustleveltext, converter.getReturnAvgDustLevel());
                    views.setImageViewResource(R.id.appwidget_dustlevelimage, converter.getReturnImage());
                    views.setTextViewText(R.id.appwidget_dustleveltextbelow,dustValueList.getPm10Value());
                    views.setTextViewText(R.id.appwidget_finedustleveltextbelow,dustValueList.getPm25Value());
                    String AvgDustLevel = converter.getReturnAvgDustLevel();
                    switch (AvgDustLevel){
                        case "매우나쁨":
                            views.setTextColor(R.id.appwidget_dustleveltext,context.getColor(R.color.colorVeryBad));
                            break;
                        case "나쁨":
                            views.setTextColor(R.id.appwidget_dustleveltext,context.getColor(R.color.colorBad));
                            break;
                        case "보통":
                            views.setTextColor(R.id.appwidget_dustleveltext,context.getColor(R.color.colorNormal));
                            break;
                        case "좋음":
                            views.setTextColor(R.id.appwidget_dustleveltext,context.getColor(R.color.colorGood));
                            break;
                    }

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
                return false;
            }
        });

        CurrentLocation currentLocation = new CurrentLocation(context);
        currentLocation.tmLookup(mhandler);


        views.setImageViewResource(R.id.appwidget_refresh,R.drawable.ic_refresh_black_24dp);

        //업데이트
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_refresh,pendingIntent);

        //어플 띄우기
        Intent intentActivity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntentActivity = PendingIntent.getActivity(context,0,intentActivity,0);
        views.setOnClickPendingIntent(R.id.appwidget_View,pendingIntentActivity);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), NewAppWidget.class.getName());
        int[] appWidgets = appWidgetManager.getAppWidgetIds(thisAppWidget);
        final String action = intent.getAction();

        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            if(appWidgets!=null && appWidgets.length>0)
                this.onUpdate(context,AppWidgetManager.getInstance(context),appWidgets);

        }
    }
}


