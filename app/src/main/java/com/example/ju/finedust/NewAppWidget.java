package com.example.ju.finedust;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

import com.example.ju.finedust.Item.StationDustreturns;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {


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


                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
                return false;
            }
        });

        CurrentLocation currentLocation = new CurrentLocation(context);
        currentLocation.tmLookup(mhandler);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.appwidget_View,pendingIntent);


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

}


