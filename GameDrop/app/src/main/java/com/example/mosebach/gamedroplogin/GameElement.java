package com.example.mosebach.gamedroplogin;

import android.graphics.drawable.Drawable;

/**
 *
 * Created by mosebach on 10/17/2016.
 */
public class GameElement {

    transient Drawable pic;
    int x, y, pic_id;
    int dx, dy;
    int width, height;
    String name;
    //tied to another element?do elements keep score or level?

    // Set construct
    public GameElement(int pic_id, int x, int y, int width, int height, String name){
        this.pic_id = pic_id;
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public void setPic(Drawable pic) {this.pic = pic;}

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

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void move() {
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
