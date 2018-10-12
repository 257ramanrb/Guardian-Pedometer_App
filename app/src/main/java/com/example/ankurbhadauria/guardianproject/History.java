package com.example.ankurbhadauria.guardianproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class History extends AppCompatActivity {

    TextView date_tv,time_tv,duration_tv,distance_tv,calories_tv,steps_tv,goal_tv,goalAchieved_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        date_tv=(TextView)findViewById(R.id.date_tv);
        time_tv=(TextView)findViewById(R.id.time_tv);
        duration_tv=(TextView)findViewById(R.id.duration_tv);
        distance_tv=(TextView)findViewById(R.id.distance_tv);
        calories_tv=(TextView)findViewById(R.id.calories_burned_tv);
        steps_tv=(TextView)findViewById(R.id.steps_tv);
        goal_tv=(TextView)findViewById(R.id.goal_tv);
        goalAchieved_tv=(TextView)findViewById(R.id.goal_achieved__tv);

        SharedPreferences sharedPreferences=getSharedPreferences("MyData", Context.MODE_PRIVATE);
        date_tv.setText(sharedPreferences.getString("date_history","00/00/0000"));
        time_tv.setText(sharedPreferences.getString("time_history","00-00-00"));
        duration_tv.setText(sharedPreferences.getString("duration_history","00-00-00"));

        String temp_dist=sharedPreferences.getString("distance_history","0.0 m");
        temp_dist=temp_dist.substring(0,temp_dist.length()-2);
        distance_tv.setText(temp_dist);

        String temp_cal=sharedPreferences.getString("calories_history","0.0 cal");
        temp_cal=temp_cal.substring(0,temp_cal.length()-4);
        calories_tv.setText(temp_cal);

        steps_tv.setText(sharedPreferences.getString("steps_history","0"));
        goal_tv.setText(sharedPreferences.getString("goal_history","00-00-00"));

        int temp_steps=Integer.parseInt(steps_tv.getText().toString());
        int temp_goal=Integer.parseInt(goal_tv.getText().toString());
        if(temp_steps>=temp_goal)
            goalAchieved_tv.setText("Yes");
        else
            goalAchieved_tv.setText("No");

        //Toast.makeText(this,sharedPreferences.getString("time_history","00-00-00")+" "+sharedPreferences.getString("weight","00"),Toast.LENGTH_SHORT).show();
    }
}
