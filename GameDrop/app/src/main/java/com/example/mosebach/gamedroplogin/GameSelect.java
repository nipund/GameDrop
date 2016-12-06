package com.example.mosebach.gamedroplogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameSelect extends AppCompatActivity {
    public Button but1;
    public Button but2;
    double latititudeGet = 0;
    double longitudeGet = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            latititudeGet = extras.getDouble("n1");
            longitudeGet = extras.getDouble("n2");
        }else{
            System.out.println("Null extras");

        }init();
    }
    public void init(){

        but2 = (Button)findViewById(R.id.button2);

        but2.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(GameSelect.this, EditorActivity.class);
                intent.putExtra("type","Platformer");
                intent.putExtra("n1",latititudeGet);
                intent.putExtra("n2",longitudeGet);
                startActivity(intent);
            }

        });
    }


}
