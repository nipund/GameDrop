package com.example.patrick.touchtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class TouchTrackerActivity extends AppCompatActivity {
    //id   cordView
    TextView view;
    @Override
    public boolean onTouchEvent(MotionEvent event){

        float xaxis = event.getX();
        float yaxis = event.getY();

        view.setText("x: " + xaxis + "\n" + "y: " + yaxis);

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_tracker);
        view = (TextView) findViewById(R.id.cordView);
    }
}
