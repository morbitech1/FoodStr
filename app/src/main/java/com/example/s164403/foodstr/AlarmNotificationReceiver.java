package com.example.s164403.foodstr;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    public static final String INTENT_BROADCAST = "com.example.s164403.foodstr.AlarmNotify";
    public static final int NOTIFICATION_ID = 4123;

    private static final long[] veffect = {0, 500, 200, 100, 100, 100, 500};

    @Override
    public void onReceive(Context context, Intent intent) {
        String time = "";
        time += (intent.getIntExtra("hour", 0) < 10 ? "0"+intent.getIntExtra("hour", 0) : intent.getIntExtra("hour", 0));
        time += ":"+(intent.getIntExtra("min", 0) < 10 ? "0"+intent.getIntExtra("min", 0) : intent.getIntExtra("min", 0));

        showAlarmDialog(context, intent.getStringExtra("recipe"), intent.getStringExtra("start"), intent.getStringExtra("end"), time);

        if (!intent.getBooleanExtra("novibrate", false)){
            Vibrator vib = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
            vib.vibrate(veffect, 0);
        }

        Log.i("Alarm", "Received: " + time);
    }

    public void showAlarmDialog(Context context, String recipe, String start, String end, String time){
        RemoteViews contentView = new RemoteViews("com.example.s164403.foodstr", R.layout.foodstr_alarm_notification);
        contentView.setTextViewText(R.id.recipe, recipe);
        contentView.setTextViewText(R.id.time, time);
        contentView.setTextViewText(R.id.starting, start);
        contentView.setTextViewText(R.id.ending, end);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Title")
                .setContentText("[12:03] Foodstr Alarm")
                .setTicker("Foodstr Alarm ["+time+"]")
                .setCustomBigContentView(contentView);

        NotificationManager not = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        not.notify(NOTIFICATION_ID, mBuilder.build());

    }
}

