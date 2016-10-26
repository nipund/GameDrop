package com.example.mosebach.gamedroplogin;

import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Nipun on 10/26/2016.
 */
public class EditorTouchListener implements View.OnTouchListener {

    private final TileView root;
    private final EditorActivity act;
    private float prevX, prevY;

    public EditorTouchListener(TileView root, EditorActivity act) {
        super();
        this.root = root;
        this.act = act;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Toast.makeText(act, "Down", Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_UP:
                //Toast.makeText(act, "Up", Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //Toast.makeText(act, "P Down", Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //Toast.makeText(act, "P Up", Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - prevX;
                float dy = y - prevY;
                root.testMove(dx, dy);
                //Toast.makeText(act, "dx = "+dx+", dy = "+dy, Toast.LENGTH_SHORT).show();
                break;
        }
        //root.invalidate();
        prevX = x;
        prevY = y;
        return true;
    }
}
