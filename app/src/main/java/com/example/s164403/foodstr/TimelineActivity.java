package com.example.s164403.foodstr;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

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

        loadTimeline(Timeline.getTestTimeline());
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

            for (StepAlarm alarm : timeline.getAlarmTimes().values()){
                //System.out.println(alarm.toString());
                Log.i("FoodstrAlarm", alarm.toString());

                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);

                int time = (finishtime-alarm.getTime());
                if (time <= 0) continue;

                int phour = time/60;
                int pmin = time%60;

                int tmin = (min + pmin)%60;
                int ahour = tmin < min ? 1 : 0;
                int thour = (hour + phour + ahour)%24;



                intent.putExtra(AlarmClock.EXTRA_HOUR, thour);
                intent.putExtra(AlarmClock.EXTRA_MINUTES, tmin);

                intent.putExtra(AlarmClock.EXTRA_MESSAGE, alarm.toString());
                intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                startActivityForResult(intent, alarm.getTime());
            }
        }
    }


}
