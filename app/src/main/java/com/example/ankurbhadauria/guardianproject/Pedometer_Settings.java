package com.example.ankurbhadauria.guardianproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IntegerRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Pedometer_Settings extends AppCompatActivity {

    TextView sensitivity_tv;
    EditText height_et,step_size_et,weight_et,goal_et;
    RadioButton male_rb,female_rb;
    SeekBar sensitivity_sb;
    Button save_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer__settings);

        SharedPreferences sharedPreferences=getSharedPreferences("MyData",Context.MODE_PRIVATE);

        height_et=(EditText)findViewById(R.id.height_et);
        step_size_et=(EditText)findViewById(R.id.step_size_et);
        weight_et=(EditText)findViewById(R.id.weight_et);
        sensitivity_sb=(SeekBar)findViewById(R.id.sensitivity_sb);
        goal_et=(EditText)findViewById(R.id.goal_steps);
        male_rb=(RadioButton) findViewById(R.id.male_rb);
        female_rb=(RadioButton) findViewById(R.id.female_rb);
        save_bt=(Button)findViewById(R.id.save_bt) ;
        sensitivity_tv=(TextView)findViewById(R.id.sensitivity_tv);

        String sex=sharedPreferences.getString("sex","Male");
        String threshold=sharedPreferences.getString("sensitivitity","10");
        int thresh=Integer.parseInt(threshold);
        if(sex.equals("Male"))
            male_rb.setChecked(true);
        else
            female_rb.setChecked(true);

        weight_et.setText(sharedPreferences.getString("weight","60"));
        height_et.setText(sharedPreferences.getString("height","174"));

        sensitivity_sb.setProgress(thresh);
        sensitivity_sb.setOnSeekBarChangeListener(seekBarListener);
        sensitivity_tv.setText(threshold);

        step_size_et.setText(sharedPreferences.getString("stepsize","72.21"));
        goal_et.setText(sharedPreferences.getString("goal","1000"));


        step_size_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    long ht=Long.parseLong(String.valueOf(height_et.getText()));

                    step_size_et.setText((ht*0.415)+"");
                }
            }
        });

        save_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msex;
                int seek=sensitivity_sb.getProgress();
                if(female_rb.isChecked())
                    msex="Female";
                else
                    msex="Male";

                SharedPreferences sharedPreferences=getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("sex",msex);
                editor.putString("weight",weight_et.getText().toString());
                editor.putString("height",height_et.getText().toString());
                editor.putString("sensitivitity", Integer.toString(seek));
                editor.putString("stepsize",step_size_et.getText().toString());
                editor.putString("goal",goal_et.getText().toString());
                editor.commit();

                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();



            }
        });




    }
    SeekBar.OnSeekBarChangeListener seekBarListener=new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            int curr_progress=seekBar.getProgress();
            sensitivity_tv.setText(curr_progress+"");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
