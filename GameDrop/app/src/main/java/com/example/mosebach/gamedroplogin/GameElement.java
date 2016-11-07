package com.example.mosebach.gamedroplogin;

import android.graphics.drawable.Drawable;

/**
 *
 * Created by mosebach on 10/17/2016.
 */
public class GameElement {

    transient Drawable pic;
    int pic_id;
    int x, y;
    int width, height;
    String name;
    //tied to another element?do elements keep score or level?

    // Set construct
    public GameElement(Drawable pic, int pic_id, int x, int y, int width, int height, String name){
        this.pic = pic;
        this.pic_id = pic_id;
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
        return y + height;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    // Default construct
    public GameElement(){
        pic = null;
        x = 0;
        y = 0;
        name = null;
    }

}
