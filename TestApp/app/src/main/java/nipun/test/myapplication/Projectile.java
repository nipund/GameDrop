package nipun.test.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Projectile {
    private int x;
    private int y;
    private int speed;

    public Projectile(Context context, int x, int y) {
        this.x = x;
        this.y = y;
        speed = 4;
    }

    public void update() {
        x += speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}

