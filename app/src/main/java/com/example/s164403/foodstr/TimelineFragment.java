package com.example.s164403.foodstr;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s164403.foodstr.database.DatabaseRecipe;
import com.example.s164403.foodstr.database.MainDatabaseHelper;
import com.example.s164403.foodstr.database.Model.Recipe;

import java.util.ArrayList;
import java.util.Calendar;

public class TimelineFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_timeline, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageView fridge = (ImageView)getActivity().findViewById(R.id.button_fridge);
        fridge.setVisibility(View.VISIBLE);
        ImageView accept = (ImageView)getActivity().findViewById(R.id.button_accept);
        accept.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();

        ImageView fridge = (ImageView)getActivity().findViewById(R.id.button_fridge);
        fridge.setVisibility(View.GONE);
        ImageView accept = (ImageView)getActivity().findViewById(R.id.button_accept);
        accept.setVisibility(View.GONE);
    }

    private Recipe recipe;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //loadTimeline(Timeline.getTestTimeline());
        MainDatabaseHelper databaseHelper = new MainDatabaseHelper(getActivity());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        DatabaseRecipe databaseRecipe = new DatabaseRecipe(db);
        recipe = databaseRecipe.getRecipe(getArguments().getLong("recipeID"));
        db.close();
        Timeline timeline2 = new Timeline(recipe, getActivity());
        timeline2.loadTimelineFromDatabase();
        timeline2.sort();
        loadTimeline(timeline2);

        ImageView accept = (ImageView) getActivity().findViewById(R.id.button_accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "Starting alarms");
                setAlarms();
            }
        });

        ImageButton newstep = (ImageButton) getActivity().findViewById(R.id.step_add);
        newstep.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                editRecipeStep(null);
            }
        });

        final TextView handsnum = (TextView) getActivity().findViewById(R.id.hands_num);
        if (timeline != null) handsnum.setText(""+timeline.getAvailableHands());

        ImageButton handsup = (ImageButton) getActivity().findViewById(R.id.hands_up);
        handsup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                int num = Integer.parseInt(handsnum.getText().toString());
                if (num < 0){
                    num = 0;
                }
                num += 1;
                handsnum.setText(""+num);
                if (timeline != null){
                    timeline.setAvailableHands(num);
                    loadTimeline(timeline);
                }
                Log.i("Hands", "Added hands: " + num);
            }
        });

        ImageButton handsdown = (ImageButton) getActivity().findViewById(R.id.hands_down);
        handsdown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                int num = Integer.parseInt(handsnum.getText().toString());
                if (num < 2){
                    num = 2;
                }
                num -= 1;
                handsnum.setText(""+num);
                if (timeline != null){
                    timeline.setAvailableHands(num);
                    loadTimeline(timeline);
                }
            }
        });
    }

    private Timeline timeline;
    private TimelineView mTimelineView;
    private LinearLayout mStepListView;

    public void loadTimeline(Timeline timeline){
        this.timeline = timeline;
        mTimelineView = (TimelineView) getActivity().findViewById(R.id.timeline);
        mTimelineView.loadTimeline(timeline);

        mStepListView = (LinearLayout) getActivity().findViewById(R.id.step_list);
        int finish = timeline.getFinishTime();
        LayoutInflater layout = getActivity().getLayoutInflater();
        mStepListView.removeAllViews();

        for (RecipeStep step : timeline.getSteps()){
            //View view = layout.inflate(R.layout.sample_recipe_steps_view, null);
            View stepview = new RecipeStepsView(getActivity());
            View view = stepview.getRootView();

            TextView name = (TextView) view.findViewById(R.id.nametext);
            name.setText(step.getName());

            ((ImageView) view.findViewById(R.id.handsymbol)).setVisibility(step.getNeedsHand() ? View.VISIBLE : View.INVISIBLE);

            int end = finish - step.getStartTime();
            int start = end - step.getTime();
            ((TextView) view.findViewById(R.id.timetext)).setText(""+start+" - " + end);
            ((TextView) view.findViewById(R.id.description)).setText(step.getDescription());
            ((TextView) view.findViewById(R.id.extendtitle)).setText(step.getName());

            final RecipeStep finalStep = step;
            ImageButton settings = (ImageButton)view.findViewById(R.id.optionsbutton);
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editRecipeStep(finalStep);
                }
            });

            mStepListView.addView(view);
        }

    }

    public void setAlarms(){
        if (timeline != null && timeline.getAlarmTimes() != null){
            int finishtime = timeline.getFinishTime();
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);

            AlarmManager manager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
            long millis = SystemClock.elapsedRealtime();

            AlarmNotificationReceiver.alarmids.clear();

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

                Intent intent = new Intent(AlarmNotificationReceiver.INTENT_BROADCAST);
                intent.putExtra("recipe", recipe.name);
                intent.putExtra("start", alarm.getStartingString());
                intent.putExtra("end", alarm.getEndingString());
                intent.putExtra("hour", thour);
                intent.putExtra("min", tmin);

                String strtime = "";
                strtime += (thour < 10 ? "0"+thour : thour);
                strtime += ":"+(tmin < 10 ? "0"+tmin : tmin);

                if (alarm.getTime() > 0) intent.putExtra("sticky", true);

                if (time <= 0){
                    intent.putExtra("novibrate", true);
                    getActivity().sendBroadcast(intent);
                    Log.i("AlarmSent0", strtime);
                }else {
                    //intent.putExtra("novibrate", true);
                    PendingIntent pi = PendingIntent.getBroadcast(getActivity(), time, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, millis + (((long)time) * 60000), pi);
                    Log.i("AlarmSent", strtime);

                    AlarmNotificationReceiver.alarmids.add(time);
                }

                Log.i("Alarmtid", ""+millis + ", " + time + ", " + (millis+time) + ", "+ strtime + ", " + alarm.getTime());

            }
        }
    }

    private static int filter_off = Color.argb(255,100,100,100);
    private static int filter_on = Color.argb(255,255,255,255);

    public void editRecipeStep(RecipeStep step){
        if (timeline != null){
            if (step == null){
                step = new RecipeStep();
                step.setName("");
            }

            final RecipeStep finalStep = step;

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View view = inflater.inflate(R.layout.sample_recipe_step_context_view, null);

            final TextView name = (TextView)view.findViewById(R.id.edit_step_title);
            name.setText(finalStep.getName());
            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    finalStep.setName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            final TextView time = (TextView)view.findViewById(R.id.edit_step_time);
            time.setText(""+finalStep.getTime());
            time.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int num = Integer.parseInt(s.toString());
                    if (num <= 0){
                        num = 10;
                    }

                    finalStep.setTime(num);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            Button time_add = (Button)view.findViewById(R.id.edit_step_time_up);
            time_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(time.getText().toString());
                    if (num < 0){
                        num = 0;
                    }
                    num += 10;
                    time.setText(""+num);
                }
            });

            Button time_reduce = (Button)view.findViewById(R.id.edit_step_time_down);
            time_reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(time.getText().toString());
                    num -= 10;
                    if (num < 0){
                        num = 0;
                    }
                    time.setText(""+num);
                }
            });

            final ImageButton hot = (ImageButton)view.findViewById(R.id.edit_step_hot);
            hot.setColorFilter(finalStep.getHot() ? filter_on : filter_off);
            hot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalStep.setHot(!finalStep.getHot());
                    hot.setColorFilter(finalStep.getHot() ? filter_on : filter_off);
                }
            });

            final ImageButton hand = (ImageButton)view.findViewById(R.id.edit_step_hand);
            hand.setColorFilter(finalStep.getNeedsHand() ? filter_on : filter_off);
            hand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalStep.setNeedsHand(!finalStep.getNeedsHand());
                    hand.setColorFilter(finalStep.getNeedsHand() ? filter_on : filter_off);
                }
            });

            TextView description = (TextView)view.findViewById(R.id.edit_step_description);
            description.setText(finalStep.getDescription());
            description.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    finalStep.setDescription(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            Button prereq = (Button)view.findViewById(R.id.edit_step_prerequisites);
            prereq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<RecipeStep> steps = new ArrayList<RecipeStep>(timeline.getSteps());
                    steps.remove(finalStep);

                    final String[] names = new String[steps.size()];
                    final boolean[] checked = new boolean[steps.size()];
                    for (int i = 0; i < steps.size(); i++){
                        RecipeStep step = steps.get(i);
                        checked[i] = finalStep.isPrerequisite(step);
                        names[i] = step.getName();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Set the dialog title
                    builder.setTitle("Prerequisites")
                            .setMultiChoiceItems(names, checked,
                                    new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which,
                                                            boolean isChecked) {
                                            RecipeStep step = steps.get(which);
                                            if (finalStep.createsDependencyCycle(step)){
                                                Toast.makeText(getActivity(), "Cannot set this as prerequisite as it would create a dependency cycle", Toast.LENGTH_LONG).show();
                                                ListView al = ((AlertDialog)dialog).getListView();
                                                al.setItemChecked(which, false);
                                                checked[which] = false;
                                            }
                                        }
                                    })
                            // Set the action buttons
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    for (int i = 0; i < steps.size(); i++){
                                        if (finalStep.isPrerequisite(steps.get(i)) != checked[i]){
                                            if (checked[i]){
                                                finalStep.addPrerequisite(steps.get(i));
                                            }else{
                                                finalStep.removePrerequisite(steps.get(i));
                                            }
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    builder.create().show();
                }
            });

            builder.setView(view);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface face){
                    updateStep(finalStep);
                }
            });
            builder.setCancelable(true);
            final Dialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.show();
            dialog.getWindow().setAttributes(lp);

            Button delete = (Button)view.findViewById(R.id.edit_step_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteStep(finalStep);
                    dialog.dismiss();
                }
            });
        }
    }

    public void updateStep(RecipeStep step){
        if (timeline != null){
            if (step != null && !timeline.getSteps().contains(step)){
                timeline.addStep(step);
            }
            timeline.sort();
            loadTimeline(timeline);
        }
    }

    public void deleteStep(RecipeStep step){
        if (timeline != null){
            if (timeline.getSteps().contains(step)){
                timeline.removeStep(step);
            }
            timeline.sort();
            loadTimeline(timeline);
        }
    }


}
