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
        drawGrid(c, 20, 16);
        paint.setARGB(255, 0, 255, 0);

        if(elements != null) {
            for(int i = 0; i < elements.size(); i++) {
                GameElement ge = elements.get(i);
                Drawable d = ge.pic;
                d.setBounds(ge.x, ge.y, ge.getRight(), ge.getBottom());
                d.setAlpha(255);
                d.draw(c);
                if(selected == i) {
                    c.drawLine(ge.x, ge.y, ge.getRight(), ge.y, paint);
                    c.drawLine(ge.getRight(), ge.y, ge.getRight(), ge.getBottom(), paint);
                    c.drawLine(ge.getRight(), ge.getBottom(), ge.x, ge.getBottom(), paint);
                    c.drawLine(ge.x, ge.getBottom(), ge.x, ge.y, paint);
                }
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
        elements.get(selected).move(dx, dy);
        invalidate();
    }

    public void nextElement() {
        if(selected == elements.size() - 1) {
            selected = 0;
        } else {
            selected++;
        }
    }

    public void setToSprite() {
        for(int i = 0; i < elements.size(); i++) {
            GameElement el = elements.get(i);
            if(i == selected) {
                el.setSprite(true);
            } else {
                el.setSprite(false);
            }
        }
    }

    public boolean hasSprite(){
        for(int i = 0; i < elements.size(); i++) {
            GameElement el = elements.get(i);
            if(el.type == GameElement.ElType.SPRITE) {
                return true;
            }
        }
        return false;
    }

    public void removeSelectedElement(){
        for(int i = 0; i < elements.size(); i++){
            if(i == selected){
                elements.remove(i);
            }
        }

        selected = 0;
        this.invalidate();

    }

    public void selectLastElement() {
        selected = elements.size() - 1;
    }
}














































































































