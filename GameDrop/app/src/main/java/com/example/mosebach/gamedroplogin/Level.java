package com.example.mosebach.gamedroplogin;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that holds all level details and to be loaded on a play request
 * Created by mosebach on 10/17/2016.
 */
public class Level {

    ArrayList<GameElement> elements;
    String description;
    String name;
    Drawable background;
    ArrayList<GameElement> coins;
    //score?time?

    // Set Construct
    public Level(ArrayList<GameElement> elements, String desc, String name, Drawable bkgr){
        this.elements = elements;
        description = desc;
        this.name = name;
        background = bkgr;
        coins = new ArrayList<GameElement>();
    }

    // Default construct
    public Level(){
        this.elements = null;
        description = "";
        this.name = "";
        background = null;
    }

    public void getCoins(){
        for(int i = 0; i < elements.size(); i++){
            if(elements.get(i).type == GameElement.ElType.COIN){
                coins.add(elements.get(i));
                elements.remove(i);
                i--;
            }
        }
    }

    public void setCoin(){
        Random rand = new Random();

        elements.add(coins.get(rand.nextInt(coins.size())));

    }

}
