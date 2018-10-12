package com.example.ankurbhadauria.guardianproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pedometer extends Fragment {

    TextView calorie_tv,time_tv,dist_tv,steps_tv,goalSteps_tv,step_count_tv;
    Button startStop_bt;
    FloatingActionButton lock_fb,unlock_fb,pause,play;
    Chronometer mChronometer;

    SharedPreferences sharedPreferences;

    private SensorManager sensorManager;


    private float previousY,currentY;
    private int numSteps=0;

    private  int threshold=10;
    Double step_size,distance=0.0,weight=60.0;;

    long lastPause;

    final double walkingFactor = 0.57;

    String goal,thresh,step_size_str,weight_str;

    double CaloriesBurnedPerMile, stepCountMile, conversationFactor, CaloriesBurned;
    String date_history,time_history,duration_history,distance_history,calories_history,steps_history,goal_history;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_pedometer,container,false);

        sharedPreferences= getActivity().getSharedPreferences("MyData",Context.MODE_PRIVATE);

        goal=sharedPreferences.getString("goal","10000");
        thresh=sharedPreferences.getString("sensitivitity","10");
        step_size_str=sharedPreferences.getString("stepsize","72.21");
        weight_str=sharedPreferences.getString("weight","60");

        step_size=Double.parseDouble(step_size_str);
        threshold=Integer.parseInt(thresh);
        weight=Double.parseDouble(weight_str);

        CaloriesBurnedPerMile=walkingFactor*(weight*2.2);
        stepCountMile=160934.4/step_size;
        conversationFactor = CaloriesBurnedPerMile / stepCountMile;

        calorie_tv=(TextView)view.findViewById(R.id.calories);
        time_tv=(TextView)view.findViewById(R.id.time_count);
        dist_tv=(TextView)view.findViewById(R.id.distance);
        steps_tv=(TextView)view.findViewById(R.id.steps);
        goalSteps_tv=(TextView)view.findViewById(R.id.goal_steps);
        step_count_tv=(TextView)view.findViewById(R.id.steps);

        step_count_tv.setText(Html.fromHtml(  "<font color='#000000'>"+"<big>"+"<big>"+ "<big>" + numSteps  +"</big>"+ "</big>"+"</big>"+"</font>"+"<br/>" +"<small>" +" steps"+"</small>" ));

        goalSteps_tv.setText(Html.fromHtml(  "GOAL: " + "<font color='#000000'>"+ "<big>"+ "<b>" + goal + "</b>" +"</big>"+ "</font>" + " steps" ));

        startStop_bt=(Button)view.findViewById(R.id.start_stop);

        lock_fb=(FloatingActionButton)view.findViewById(R.id.lock_fb);
        unlock_fb=(FloatingActionButton)view.findViewById(R.id.unlock_fb);
        pause=(FloatingActionButton)view.findViewById(R.id.pause);
        play=(FloatingActionButton)view.findViewById(R.id.play);

        mChronometer=(Chronometer)view.findViewById(R.id.time_count);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setClickable(true);
                pause.setClickable(false);
                lastPause= SystemClock.elapsedRealtime();
                mChronometer.stop();
                Toast.makeText(getContext(),"Pause",Toast.LENGTH_SHORT).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setClickable(false);
                pause.setClickable(true);
                Toast.makeText(getContext(),"Play",Toast.LENGTH_SHORT).show();
                if(lastPause!=0)
                {
                    mChronometer.setBase(mChronometer.getBase()+SystemClock.elapsedRealtime()-lastPause);
                }
                else
                {
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                }
                mChronometer.start();
            }
        });

        startStop_bt.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(startStop_bt.getText().equals("Start"))
                {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
                    date_history = df1.format(c.getTime());

                    SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
                    time_history = df2.format(c.getTime());


                    startStop_bt.setText("Stop");
                    pause.setVisibility(view.VISIBLE);
                    play.setVisibility(view.VISIBLE);
                    startStop_bt.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.stopColor)));
                    mChronometer.start();
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                }
                else
                {
                    startStop_bt.setText("Start");
                    pause.setVisibility(view.INVISIBLE);
                    play.setVisibility(view.INVISIBLE);
                    startStop_bt.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                    distance_history=dist_tv.getText().toString();
                    calories_history=calorie_tv.getText().toString();
                    steps_history=numSteps+"";
                    goal_history=goal+"";
                    duration_history=mChronometer.getText().toString();

                    mChronometer.stop();
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    lastPause=0;
                    numSteps=0;
                    distance=0.0;
                    CaloriesBurned=0.0;
                    step_count_tv.setText(Html.fromHtml(  "<font color='#000000'>"+"<big>"+"<big>"+ "<big>" + numSteps  +"</big>"+ "</big>"+"</big>"+"</font>"+"<br/>" +"<small>" +" steps"+"</small>" ));
                    dist_tv.setText(Html.fromHtml(  "<font color='#000000'>"+ distance + "</font>"+"<small>" +" m"+"</small>" ));
                    calorie_tv.setText(Html.fromHtml(  "<font color='#000000'>"+ CaloriesBurned + "</font>"+"<small>" +" cal"+"</small>" ));



                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("date_history",date_history);
                    editor.putString("time_history",time_history);
                    editor.putString("duration_history",duration_history);
                    editor.putString("distance_history",distance_history);
                    editor.putString("calories_history",calories_history);
                    editor.putString("steps_history",steps_history);
                    editor.putString("goal_history",goal_history);
                    editor.commit();
                }
            }
        });

        lock_fb.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                startStop_bt.setEnabled(false);
                pause.setVisibility(view.INVISIBLE);
                play.setVisibility(view.INVISIBLE);
                if(startStop_bt.getText().equals("Start"))
                    startStop_bt.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.disabledColor)));
                else
                    startStop_bt.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.disabledColor2)));

            }
        });

        unlock_fb.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                startStop_bt.setEnabled(true);
                if(startStop_bt.getText().equals("Start"))
                    startStop_bt.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                else {
                    pause.setVisibility(view.VISIBLE);
                    play.setVisibility(view.VISIBLE);
                    startStop_bt.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.stopColor)));
                }
            }
        });
        enableAccelerometerListeniing();

        return view;
    }

    private void enableAccelerometerListeniing() {
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),sensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            if(startStop_bt.getText().equals("Stop"))  {
                float x=sensorEvent.values[0];
                float y=sensorEvent.values[1];
                float z=sensorEvent.values[2];

                currentY=y;

                if(Math.abs(currentY-previousY)>threshold&&pause.isClickable()){
                    numSteps++;
                    distance=(numSteps*step_size)/100;
                    CaloriesBurned=numSteps*conversationFactor;
                    CaloriesBurned=Double.parseDouble(String.format("%.2f", CaloriesBurned));
                    distance=Double.parseDouble(String.format("%.2f", distance));
                    step_count_tv.setText(Html.fromHtml(  "<font color='#000000'>"+"<big>"+"<big>"+ "<big>" + numSteps  +"</big>"+ "</big>"+"</big>"+"</font>"+"<br/>" +"<small>" +" steps"+"</small>" ));
                    dist_tv.setText(Html.fromHtml(  "<font color='#000000'>"+ distance + "</font>"+"<small>" +" m"+"</small>" ));
                    calorie_tv.setText(Html.fromHtml(  "<font color='#000000'>"+ CaloriesBurned + "</font>"+"<small>" +" cal"+"</small>" ));
                }

                previousY=y;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
