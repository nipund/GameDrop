

package com.example.mosebach.gamedroplogin;

import android.graphics.drawable.Drawable;

/**
 *
 * Created by mosebach on 10/17/2016.
 */
public class GameElement {

    transient Drawable pic;
    int x, y, pic_id;
    int dx;
    int dy;
    int grav;
    int width, height;
    String name;
    ElType type;

    boolean isSprite;
    //tied to another element?do elements keep score or level?

    public enum ElType {
        OBJECT, SPRITE, PLATFORM, COIN, POWERUP
    }

    // Set construct
    public GameElement(int pic_id, int x, int y, int width, int height, String name){
        this.pic_id = pic_id;
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
        this.grav = 0;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public void setType(ElType type) {
        this.type = type;
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
    public int right(){return this.x + this.width;}

    public void setRight(int x){this.x = x - this.width;}

    public int left(){return this.x;}

    public void setLeft(int x){ this.x = x;}

    public int top(){return this.y;}

    public void setTop(int y) {this.y = y;}

    public int bottom(){return this.y + height;}

    public void setBottom(int y){this.y = y - this.height;}

    public boolean isSprite() {return type == ElType.SPRITE;}

    public void setSprite(boolean sprite) {
        if(sprite) {
            type = ElType.SPRITE;
        }
    }

    public void setGrav(int grav) {
        this.grav = grav;
    }

    //public void setConsumable(boolean consumable) {isConsumable = consumable;}

    //public boolean getConsumable() {return isConsumable;};


    public void move() {

        this.dy += grav;
            if(this.y < 10 || this.y > 1200){
                if(this.y < 10){
                    this.y = 10;
                }else if(this.y > 1200){
                    this.y = 1200;
                }
                this.dy = 0;
            }

        this.y += dy;


        this.x += dx;
    }

    public GameElement clone(){
        return new GameElement(this.pic_id, this.x, this.y, this.width, this.height, this.name);
    }

    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public void collide(){

    }

    // Default construct
    public GameElement(){
        pic = null;
        x = 0;
        y = 0;
        name = null;
    }

}


