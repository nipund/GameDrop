package com.example.mosebach.gamedroplogin;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Class that holds all level details and to be loaded on a play request
 * Created by mosebach on 10/17/2016.
 */
public class Level {

    ArrayList<GameElement> elements;
    String description;
    String name;
    Drawable background;
    //score?time?

    // Set Construct
    public Level(String desc, String name, Drawable bkgr){
        this.elements = new ArrayList<>();
        description = desc;
        this.name = name;
        background = bkgr;
    }

    // Default construct
    public Level(){
        this.elements = null;
        description = "";
        this.name = "";
        background = null;
    }

}
