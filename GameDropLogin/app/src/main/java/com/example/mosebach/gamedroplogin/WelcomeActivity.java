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


        Intent i = getIntent();
        String userName = i.getStringExtra("userName");

        TextView text = (TextView) findViewById(R.id.textUsername);

        text.setText(userName.toString());

    }
}
