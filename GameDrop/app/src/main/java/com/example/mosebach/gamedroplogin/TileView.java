package com.example.mosebach.gamedroplogin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Nipun on 10/20/2016.
 */

public class TileView extends View {

    private Paint paint;

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLACK);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas c) {
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        drawGrid(c, 20, 16);
        c.drawCircle(this.getMeasuredWidth()/2, this.getMeasuredHeight()/2, 20, paint);
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

}
