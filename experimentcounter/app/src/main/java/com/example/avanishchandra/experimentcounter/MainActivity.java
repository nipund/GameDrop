package com.example.avanishchandra.experimentcounter;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.SystemClock;

public class MainActivity extends AppCompatActivity {

    private long mCount = 0;
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private int reset = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView countTextView = (TextView) findViewById(R.id.TextViewCount);
        final Button countButton = (Button) findViewById(R.id.ButtonCount);
        final TextView decreaseTextView = (TextView) findViewById(R.id.TextViewDecrease);
        final Button resetButton = (Button) findViewById(R.id.negButton);

        countButton.setOnClickListener(new OnClickListener() {
            long startTime = System.nanoTime();
                public void onClick (View v){

                    mCount = (System.nanoTime() - startTime) / 1000000;
                    seconds = (int) mCount / 1000;
                    if (seconds > 60) {
                        minutes++;
                        seconds = seconds - 60;
                    }
                    minutes = (int) mCount / 60000;
                    if (minutes > 59) {
                        minutes = minutes - 60;
                        hours++;
                    }
                    hours = (int) mCount / 3600000;

                    countTextView.setText("Time: " + "Hours: " + hours + "Miutes: " + minutes + "Seconds: " + seconds);
            }
        });

        resetButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                reset = 0;
                mCount = 0;
                hours = 0;
                minutes = 0;
                seconds = 0;
                countTextView.setText("Reset");
            }
        });

    }
}
