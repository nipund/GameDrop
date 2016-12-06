package com.example.mosebach.gamedroplogin;

/**
 * Created by Nipun on 11/7/2016.
 */

public class ElementStore {
    public static int elements[] = {
            R.drawable.basketball,
            R.drawable.sample_element_1,
            R.drawable.duck,
            R.drawable.space,
            R.drawable.plane,
            R.drawable.castle,
            R.drawable.coin,
            R.drawable.star,
            R.drawable.zombieelf,
            R.drawable.stary,
            R.drawable.flame
    };

    public static GameElement.ElType types[] = {
            GameElement.ElType.OBJECT,
            GameElement.ElType.OBJECT,
            GameElement.ElType.OBJECT,
            GameElement.ElType.OBJECT,
            GameElement.ElType.OBJECT,
            GameElement.ElType.PLATFORM,
            GameElement.ElType.COIN,
            GameElement.ElType.POWERUP,
            GameElement.ElType.ZOMBIE,
            GameElement.ElType.POWERUP,
            GameElement.ElType.FIRE
    };

    public static int sizes[][] = {
            {100, 100},
            {100, 100},
            {100, 100},
            {100, 100},
            {100, 100},
            {400, 100},
            {100, 100},
            {100, 100},
            {200,200},
            {100, 100},
            {100, 100}
    };
}
