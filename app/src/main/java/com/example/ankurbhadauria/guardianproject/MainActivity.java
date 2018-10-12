package com.example.ankurbhadauria.guardianproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private  SectionsPageAdapter mSectionsPageAdapter;
    private  ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Guardian");
        setSupportActionBar(toolbar);

        mSectionsPageAdapter=new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager=(ViewPager)findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.pd:
                Intent intent1=new Intent(MainActivity.this, Pedometer_Settings.class);
                startActivity(intent1);
                break;
            case R.id.pf:
                Intent intent2=new Intent(MainActivity.this, History.class);
                startActivity(intent2);
                break;
            case R.id.td:
                Intent intent3=new Intent(MainActivity.this, Help.class);
                startActivity(intent3);
                break;
            case R.id.ab:
                final AlertDialog.Builder a_builder=new AlertDialog.Builder(this);
                a_builder.setMessage("\nPedometer 2.0\n\nSensor used: Built-in Accelerometer\n\nMade by: Raman Bhadauria\n")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert=a_builder.create();
                alert.setTitle("About");
                alert.show();
                break;
        }
        return true;
    }

    private void setupViewPager(ViewPager viewPager)
    {
        SectionsPageAdapter adapter=new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Pedometer(),"Pedometer");
       /* adapter.addFragment(new PersonFall(),"PersonFall");
        adapter.addFragment(new ToDo(),"ToDo");*/
        viewPager.setAdapter(adapter);
    }
}
