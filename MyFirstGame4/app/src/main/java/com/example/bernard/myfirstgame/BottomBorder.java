package com.example.bernard.myfirstgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Bernard on 10/13/2016.
 */

public class BottomBorder extends GameObject{
    private Bitmap image;

    public BottomBorder(Bitmap res ,int x,int y){
        height = 200;
        width = 20;

        this.x = x;
        this.y = y;
        dx= GamePanel.MOVESPEED;
        image = Bitmap.createBitmap(res,0,0,width,height);

    }
    public void update(){
        x+=dx;
    }
    public void draw (Canvas canvas)
    {
        try{canvas.drawBitmap(image,x,y,null);}catch (Exception e){};
    }

}

