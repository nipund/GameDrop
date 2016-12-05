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
            R.drawable.star
    };

    public static GameElement.ElType types[] = {
            GameElement.ElType.OBJECT,
            GameElement.ElType.OBJECT,
            GameElement.ElType.OBJECT,
            GameElement.ElType.OBJECT,
            GameElement.ElType.OBJECT,
            GameElement.ElType.PLATFORM,
            GameElement.ElType.COIN,
            GameElement.ElType.POWERUP
    };

    public static int sizes[][] = {
            {100, 100},
            {100, 100},
            {100, 100},
            {100, 100},
            {100, 100},
            {400, 100},
            {100, 100},
            {100, 100}
    };
}
