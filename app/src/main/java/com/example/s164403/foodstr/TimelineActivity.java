package com.example.s164403.foodstr;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.s164403.foodstr.database.DatabaseRecipe;
import com.example.s164403.foodstr.database.MainDatabaseHelper;
import com.example.s164403.foodstr.database.Model.Recipe;

import java.util.Calendar;

public class TimelineActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    setAlarms();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //loadTimeline(Timeline.getTestTimeline());
        MainDatabaseHelper databaseHelper = new MainDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        DatabaseRecipe databaseRecipe = new DatabaseRecipe(db);
        Recipe recipe = databaseRecipe.getRecipe(1);
        db.close();
        Timeline timeline = new Timeline(recipe, this);
        timeline.loadTimelineFromDatabase();
        loadTimeline(timeline);
    }

    private Timeline timeline;
    private TimelineView mTimelineView;
    public void loadTimeline(Timeline timeline){
        this.timeline = timeline;
        //mTimelineView = (TimelineView) findViewById(R.id.timeline);
       // mTimelineView.loadTimeline(timeline);
    }

    public void setAlarms(){
        if (timeline != null){
            int finishtime = timeline.getFinishTime();
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);

            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            long millis = SystemClock.elapsedRealtime();

            for (StepAlarm alarm : timeline.getAlarmTimes().values()){
                //System.out.println(alarm.toString());
                //Log.i("FoodstrAlarm", alarm.toString());

                int time = (finishtime-alarm.getTime());
                //if (time <= 0) continue;

                int phour = time/60;
                int pmin = time%60;

                int tmin = (min + pmin)%60;
                int ahour = tmin < min ? 1 : 0;
                int thour = (hour + phour + ahour)%24;

                alarm.setParsedTime(thour, tmin);

                /*Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);

                intent.putExtra(AlarmClock.EXTRA_HOUR, thour);
                intent.putExtra(AlarmClock.EXTRA_MINUTES, tmin);

                intent.putExtra(AlarmClock.EXTRA_MESSAGE, alarm.toString());
                intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                startActivityForResult(intent, alarm.getTime());*/

                Intent intent = new Intent();
                intent.putExtra("recipe", "Noget mad");
                intent.putExtra("start", alarm.getStartingString());
                intent.putExtra("end", alarm.getEndingString());
                intent.putExtra("hour", thour);
                intent.putExtra("min", tmin);

                String strtime = "";
                strtime += (thour < 10 ? "0"+thour : thour);
                strtime += ":"+(tmin < 10 ? "0"+tmin : tmin);

                if (time <= 0){
                    intent.putExtra("novibrate", true);
                    sendBroadcast(intent);
                    //Log.i("AlarmSent0", strtime);
                }else {
                    intent.putExtra("novibrate", true);
                    PendingIntent pi = PendingIntent.getBroadcast(this, time, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, millis + time * 500, pi);
                    //Log.i("AlarmSent", strtime);
                }

                //Log.i("Alarmtid", ""+millis + ", " + time + ", " + (millis+time) + ", "+ strtime);

            }
        }
    }


}
