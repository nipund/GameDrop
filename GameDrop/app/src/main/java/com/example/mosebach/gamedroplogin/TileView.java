package com.example.mosebach.gamedroplogin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Nipun on 10/20/2016.
 */

public class TileView extends View {

    private Paint paint;
    ArrayList<GameElement> elements;
    private int selected;

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLACK);
        paint = new Paint();
        elements = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas c) {
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        drawGrid(c, 20, 16);

        if(elements != null) {
            for(GameElement ge : elements) {
                Drawable d = ge.pic;
                d.setBounds(ge.x, ge.y, ge.getRight(), ge.getBottom());
                //d.setBounds(this.getMeasuredWidth()/2, this.getMeasuredHeight()/2, (this.getMeasuredWidth()/2) + 100, (this.getMeasuredHeight()/2) + 100);
                d.setAlpha(255);
                d.draw(c);
            }
        }
    }

    private void drawGrid(Canvas c, int rows, int columns) {
        int tempColor = paint.getColor();
        paint.setColor(Color.argb(80, 0, 255, 255));
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();

        int htOfRow = height /rows;
        for (int k = 0; k < rows; k++)
            c.drawLine(0, k*htOfRow, width, k*htOfRow, paint);

        int wdOfRow = width /columns;
        for (int k = 0; k < columns; k++)
            c.drawLine(k*wdOfRow, 0, k*wdOfRow, height, paint);
        paint.setColor(tempColor);
    }

    public void testMove(float dx, float dy) {
        GameElement el = elements.get(0);
        el.x += dx;
        el.y += dy;
        elements.remove(0);
        elements.add(0, el);
        invalidate();
    }

    private void cycleElements() {
        if(selected == elements.size() - 1) {
            selected = 0;
        } else {
            selected++;
        }
    }

}
