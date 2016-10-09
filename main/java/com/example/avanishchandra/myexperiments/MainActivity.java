package com.example.avanishchandra.myexperiments;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button initialButton = (Button)findViewById(R.id.InitialButton);
        initalButton.setOnClickLister(startListener);

        Button finishButton = (Button)findViewById(R.id.finishButton);
        finishButton.setOnClickLister(stopListener);

    }
    //Create an anonymous implementation of OnClickListener
    private OnClickListener startListener = new OnClickListener() {
        public void onClick(View v) {
            Log.d(logtag,"onClick() called - start button");
            Toast.makeText(TwoButtonApp.this, "Beginning.", Toast.LENGTH_LONG).show();
            Log.d(logtag,"onClick() ended - start button");
        }
    };

    // Create an anonymous implementation of OnClickListener
    private OnClickListener stopListener = new OnClickListener() {
        public void onClick(View v) {
            Log.d(logtag,"onClick() called - stop button");
            Toast.makeText(TwoButtonApp.this, "Stopped.", Toast.LENGTH_LONG).show();
            Log.d(logtag,"onClick() ended - stop button");
        }
    };
}
