package com.example.bernard.myfirstgame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity {
    int pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get intent

        Bundle bundle = this.getIntent().getExtras();
        pic = bundle.getInt("image");

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set screen to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GamePanel(this, pic));
    }
}
