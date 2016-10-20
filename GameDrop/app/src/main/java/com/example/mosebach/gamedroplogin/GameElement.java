package com.example.mosebach.gamedroplogin;

import android.graphics.drawable.Drawable;

/**
 *
 * Created by mosebach on 10/17/2016.
 */
public abstract class GameElement {

    Drawable pic;
    int x, y;
    String name;
    //tied to another element?do elements keep score or level?

    // Set construct
    public GameElement(Drawable pic, int x, int y, String name){
        this.pic = pic;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    // Default construct
    public GameElement(){
        pic = null;
        x = 0;
        y = 0;
        name = null;
    }

}
