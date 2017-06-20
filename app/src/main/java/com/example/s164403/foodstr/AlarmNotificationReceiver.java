package com.example.s164403.foodstr;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    public static final String INTENT_BROADCAST = "com.example.s164403.foodstr.AlarmNotify";
    public static final int NOTIFICATION_ID = 4123;

    private static final String MUTE_ACTION = "com.example.s164403.foodstr.FoodstrMute";
    private static final String CANCEL_ACTION = "com.example.s164403.foodstr.FoodstrCancel";
    private static final long[] veffect = {0, 500, 200, 100, 100, 100, 500};
    private static Ringtone alarmsound;

    protected static ArrayList<Integer> alarmids = new ArrayList<Integer>();

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(MUTE_ACTION)) {
            Log.i("Mute", "Mute pressed");
            Vibrator vib = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
            vib.cancel();
            if (alarmsound != null) alarmsound.stop();

        }else if (intent.getAction().equals(CANCEL_ACTION)){

            Intent tocancel = new Intent(INTENT_BROADCAST);
            AlarmManager manager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            for (Integer id : alarmids) {
                PendingIntent picancel = PendingIntent.getBroadcast(context, id, tocancel, PendingIntent.FLAG_UPDATE_CURRENT);
                manager.cancel(picancel);
            }

            NotificationManager not = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            not.cancel(NOTIFICATION_ID);

        }else{
            String time = "";
            time += (intent.getIntExtra("hour", 0) < 10 ? "0" + intent.getIntExtra("hour", 0) : intent.getIntExtra("hour", 0));
            time += ":" + (intent.getIntExtra("min", 0) < 10 ? "0" + intent.getIntExtra("min", 0) : intent.getIntExtra("min", 0));

            showAlarmDialog(context, intent.getStringExtra("recipe"), intent.getStringExtra("start"), intent.getStringExtra("end"), time, intent.getBooleanExtra("sticky", false));

            if (!intent.getBooleanExtra("novibrate", false)) {
                Vibrator vib = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
                vib.vibrate(veffect, 0);

                if (alarmsound == null) {
                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    alarmsound = RingtoneManager.getRingtone(context, sound);
                }
                alarmsound.play();
            }

            Log.i("Alarm", "Received: " + time);
        }
    }

    public void showAlarmDialog(Context context, String recipe, String start, String end, String time, boolean sticky){
        RemoteViews contentView = new RemoteViews("com.example.s164403.foodstr", R.layout.foodstr_alarm_notification);
        contentView.setTextViewText(R.id.recipe, recipe);
        contentView.setTextViewText(R.id.time, time);
        contentView.setTextViewText(R.id.starting, start);
        contentView.setTextViewText(R.id.ending, end);

        Intent intent = new Intent(context, AlarmNotificationReceiver.class);
        intent.setAction(MUTE_ACTION);
        contentView.setOnClickPendingIntent(R.id.mute, PendingIntent.getBroadcast(context, 0, intent, 0));

        Intent intent2 = new Intent(context, AlarmNotificationReceiver.class);
        intent2.setAction(CANCEL_ACTION);
        contentView.setOnClickPendingIntent(R.id.cancelall, PendingIntent.getBroadcast(context, 0, intent2, 0));

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Title")
                .setContentText("[12:03] Foodstr Alarm")
                .setTicker("Foodstr Alarm ["+time+"]")
                .setCustomBigContentView(contentView);
        if (sticky) mBuilder.setOngoing(true);

        Notification built = mBuilder.build();
        //if (sticky) built.flags = Notification.FLAG_NO_CLEAR;
        NotificationManager not = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        not.notify(NOTIFICATION_ID, built);

    }
}

