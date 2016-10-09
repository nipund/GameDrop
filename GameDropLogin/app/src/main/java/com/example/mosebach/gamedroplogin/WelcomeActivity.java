package com.example.mosebach.gamedroplogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        String text;

        //Intent i = getIntent();
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            text = "";
        }else{
            text = extras.getString("Username");
        }
        //String userName = i.getStringExtra("Username");

        TextView textView = (TextView) findViewById(R.id.textUsername);

        textView.setText(text);

    }
}
