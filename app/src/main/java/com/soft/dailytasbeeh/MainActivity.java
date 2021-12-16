package com.soft.dailytasbeeh;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.facebook.stetho.Stetho;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;


import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mButtonCountLayout;
    private TextView mTasbeehName;
    private TextSwitcher mTextSwitcher;
    private TextView mCountShowTextView;
    private String[] tasbeehName = {"Allahu Akbar", "Subhanallah", "La Ilaha illallah", "Bismillahir Rahmanir Rahim", "Alhamdulilloh", "Astagfirullah", "La hawla wa la \nquwwata illa billah", "Subhanallah Owa-bihamdi\nSubhanallahil Azim"};

    int counterValue;
    int stringIndex = 0;
    private String setTasbeehName;

    //showcase view
    ViewTarget target;


    private String countedTasbeehName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        mTextSwitcher = findViewById(R.id.textswitcher_tasbeeh_name);
        mButtonCountLayout = findViewById(R.id.button_linearlayout);
        mCountShowTextView = findViewById(R.id.textview_count);
        target = new ViewTarget(findViewById(R.id.textswitcher_tasbeeh_name));

        final Integer colorAccent = getResources().getColor(R.color.accent);
        counterValue = 0;


        mTextSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stringIndex == tasbeehName.length - 1) {

                    stringIndex = 0;
                    mTextSwitcher.setText(tasbeehName[stringIndex]);
                    getTasbeehNameAndChangeTextViewValue();
                } else {
                    mTextSwitcher.setText(tasbeehName[++stringIndex]);
                    getTasbeehNameAndChangeTextViewValue();
                }


            }
        });

        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @SuppressLint("ResourceAsColor")
            @Override
            public View makeView() {

                mTasbeehName = new TextView(MainActivity.this);
                mTasbeehName.setTextColor(colorAccent);
                mTasbeehName.setTextSize(24);
                mTasbeehName.setGravity(Gravity.CENTER_HORIZONTAL);
                return mTasbeehName;

            }
        });

        //first one indexed tasbeehName Show this
        mTextSwitcher.setText(tasbeehName[stringIndex]);

        mButtonCountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                counterValue++;
                mCountShowTextView.setText(String.valueOf(counterValue));

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("FIRST_START", true);

        if (firstStart) {
            setTourGuide(target);
        }

        setAppRate();

    }

    private void setAppRate() {

        AppRate.with(MainActivity.this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(MainActivity.this);

    }

    private void setTourGuide(ViewTarget target) {

        new ShowcaseView.Builder(this)
                .setContentTitle("Cilck and explore new tasbeeh")
                .setContentText("Click on Tasbeeh Name \"Allahu Akbar\" to set another Tasbeeh")
                .useDecorViewAsParent()
                .setTarget(target)
                .build();

        SharedPreferences preferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("FIRST_START", false);
        editor.apply();

    }

    private void getTasbeehNameAndChangeTextViewValue() {

        //getting selected tasbeeh name from user
        setTasbeehName = tasbeehName[stringIndex];

        //Get Counting Tasbeeh Name
        if (stringIndex == 0) {
            countedTasbeehName = tasbeehName[tasbeehName.length - 1];
        } else {
            countedTasbeehName = tasbeehName[stringIndex - 1];
        }
        Log.d("TASBEEH NAME: ", "clicked: " + countedTasbeehName);

        //change the counter value to zero for shifting new tasbeeh name
        mCountShowTextView.setText(R.string.zero);
        counterValue = 0;

    }

}
