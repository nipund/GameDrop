package com.example.bernard.myfirstgame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;

public class MainPage extends AppCompatActivity {

    public ImageButton but1;
    public ImageButton but2;
    public void init(){
        but1 = (ImageButton)findViewById(R.id.imageButton);
        but2 = (ImageButton)findViewById(R.id.imageButton2);
        but1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent toy = new Intent(MainPage.this,Game.class);
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.helicopter); // your bitmap
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 50, bs);
                toy.putExtra("byteArray", bs.toByteArray());
                startActivity(toy);
            }

        });
        but2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent toy = new Intent(MainPage.this,Game.class);
                Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.ball); // your bitmap
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 50, bs);
                toy.putExtra("byteArray", bs.toByteArray());
                startActivity(toy);
            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        init();

    }
}
