package com.example.s164403.foodstr;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.s164403.foodstr.database.DatabaseRecipe;
import com.example.s164403.foodstr.database.MainDatabaseHelper;
import com.example.s164403.foodstr.database.Model.Recipe;

import java.util.Calendar;

public class TimelineActivity extends AppCompatActivity {

    private TextView mTextMessage;

    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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

    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ImageButton alarms = (ImageButton) findViewById(R.id.button_alarms);
        alarms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                setAlarms();
            }
        });

        ImageButton newstep = (ImageButton) findViewById(R.id.step_add);
        newstep.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                editRecipeStep(null);
            }
        });

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
    private LinearLayout mStepListView;

    public void loadTimeline(Timeline timeline){
        this.timeline = timeline;
        mTimelineView = (TimelineView) findViewById(R.id.timeline);
        mTimelineView.loadTimeline(timeline);

        mStepListView = (LinearLayout) findViewById(R.id.step_list);
        int finish = timeline.getFinishTime();
        LayoutInflater layout = getLayoutInflater();
        mStepListView.removeAllViews();

        for (RecipeStep step : timeline.getSteps()){
            View view = layout.inflate(R.layout.sample_recipe_steps_view, null);

            TextView name = (TextView) view.findViewById(R.id.nametext);
            name.setText(step.getName());
            ((ImageView) view.findViewById(R.id.handsymbol)).setVisibility(step.getNeedsHand() ? View.VISIBLE : View.INVISIBLE);

            int end = finish - step.getStartTime();
            int start = end - step.getTime();
            ((TextView) view.findViewById(R.id.timetext)).setText(""+start+" - " + end);

            mStepListView.addView(view);
        }

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
                    Log.i("AlarmSent0", strtime);
                }else {
                    intent.putExtra("novibrate", true);
                    PendingIntent pi = PendingIntent.getBroadcast(this, time, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, millis + time * 500, pi);
                    Log.i("AlarmSent", strtime);
                }

                Log.i("Alarmtid", ""+millis + ", " + time + ", " + (millis+time) + ", "+ strtime);

            }
        }
    }

    public void editRecipeStep(RecipeStep step){
        if (timeline != null){
            if (step == null){
                step = new RecipeStep();
                step.setName("");
            }

            final RecipeStep finalStep = step;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Get the layout inflater
            LayoutInflater inflater = getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.sample_recipe_step_context_view, null));
            builder.setOnCancelListener(new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface face){
                    updateStep(finalStep);
                }
            });
            builder.setCancelable(true);
            Dialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            FrameLayout frame = (FrameLayout)dialog.findViewById(R.id.edit_step_frame);
            dialog.getWindow().setLayout(frame.getMeasuredWidth(), frame.getMeasuredHeight());
        }
    }

    public void updateStep(RecipeStep step){

    }


}
