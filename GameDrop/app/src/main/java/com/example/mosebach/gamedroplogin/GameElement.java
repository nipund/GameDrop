package com.example.mosebach.gamedroplogin;

import android.graphics.drawable.Drawable;

/**
 *
 * Created by mosebach on 10/17/2016.
 */
public class GameElement {

    Drawable pic;
    int x, y;
    int width, height;
    String name;
    //tied to another element?do elements keep score or level?

    // Set construct
    public GameElement(Drawable pic, int x, int y, int width, int height, String name){
        this.pic = pic;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public int getRight() {
        return x + width;
    }

    public int getBottom() {
        return y - height;
    }

    // Default construct
    public GameElement(){
        pic = null;
        x = 0;
        y = 0;
        name = null;
    }

}
