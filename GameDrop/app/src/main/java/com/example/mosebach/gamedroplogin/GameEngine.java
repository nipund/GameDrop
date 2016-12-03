

package com.example.mosebach.gamedroplogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;

import static java.lang.Thread.sleep;

/**
 * Created by mosebach on 11/6/2016.
 */

public class GameEngine extends Activity {

    // gameView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    GameView gameView;
    private int signal = 0;
    private long start = System.nanoTime();
    private int score;
    private int collisionPenalty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize gameView and set it as the view
        gameView = new GameView(this);
        setContentView(gameView);

    }

    // GameView class will go here

    // Here is our implementation of GameView
    // It is an inner class.
    // Note how the final closing curly brace }
    // is inside SimpleGameEngine

    // Notice we implement runnable so we have
    // A thread and can override the run method.
    class GameView extends SurfaceView implements Runnable {

        private final float xdpi, ydpi;
        //TEST VALUE
        float touchLocation = 0;

        // This is our thread
        Thread gameThread = null;

        // This is new. We need a SurfaceHolder
        // When we use Paint and Canvas in a thread
        // We will see it in action in the draw method soon.
        SurfaceHolder ourHolder;

        // A boolean which we will set and unset
        // when the game is running- or not.
        volatile boolean playing;

        //Level to be loaded from server and run
        Level level;

        //Static Server URL Begining
        String url = "http://proj-309-gp-06.cs.iastate.edu/levels/get/";

        // A Canvas and a Paint object
        Canvas canvas;
        Paint paint;

        // This variable tracks the game frame rate
        int fps;

        // This is used to help calculate the fps
        private long timeThisFrame;

        // Declare an object of type Bitmap
        Bitmap bitmapSprite;

        /**
         * Game Element that tracks sprite
         */
        GameElement sprite;
        /**
         * Game Element that tracks last updated sprite. Used for update differences like hitboxes.
         */
        GameElement oldSprite;

        String serializedLevelString;

        // Bob starts off not moving
        boolean isMovingRight = false;

        boolean collision = false;

        // He can walk at 150 pixels per second
        int walkSpeedPerSecond;

        int gravSpeed;

        // He starts 10 pixels from the left
        int spriteXPos;

        // When the we initialize (call new()) on gameView
        // This special constructor method runs
        public GameView(Context context) {
            // The next line of code asks the
            // SurfaceView class to set up our object.
            // How kind.
            super(context);

            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            Intent i = getIntent();

            serializedLevelString = i.getStringExtra("level");

            level = deserialize(serializedLevelString);

            sprite = getSprite();


            // Set our boolean to true - game on!
            playing = true;

            // Get DPI of screen to scale game for different screen sizes/resolutions.
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            xdpi = metrics.xdpi;
            ydpi = metrics.ydpi;

            gravSpeed = yScale(150);
            walkSpeedPerSecond = xScale(150);
            spriteXPos = xScale(10);
        }

        @Override
        public void run() {
            while (playing) {

                // Capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();

                // Update the frame
                update();

                // Draw the frame
                draw();

                // Calculate the fps this frame
                // We can then use the result to
                // time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame > 0) {
                    fps = (int) ((int) 1000 / timeThisFrame);
                }
                if(fps > 20 && fps < 100) {
                    sprite.setGrav(gravSpeed / fps / 2);
                }

            }

        }

        // Everything that needs to be updated goes in here
        // In later projects we will have dozens (arrays) of objects.
        // We will also do other things like collision detection.
        public void update() {
            if (signal == 0) {
                if (sprite != null) {
                    oldSprite = sprite;
                }
                sprite.move();
                collision = checkHitboxes();
                // If bob is moving (the player is touching the screen)
                // then move him to the right based on his target speed and the current fps.
            /*if(isMoving){
                spriteXPos = spriteXPos + (walkSpeedPerSecond / fps);
            }*/
            long timer = elapsedTime();
            score =  (int)((timer/89200) - collisionPenalty*(1380));
            System.out.println("Collision penalty"+ collisionPenalty);
            }else{
                try{
                    wait(100);
                }catch(InterruptedException ex){
                    System.out.println("Can not wait for activity");
                }
                finish();
                System.out.println("FINISH!");
            }
        }

        // Draw the newly updated scene
        public void draw() {

            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();

                // Draw the background color
                canvas.drawColor(Color.argb(255,  26, 128, 182));

                // Choose the brush color for drawing
                paint.setColor(Color.argb(255,  249, 129, 0));

                // Make the text a bit bigger
                paint.setTextSize(45);

                // Display the current fps on the screen
                canvas.drawText("FPS:" + fps + "\nTouch:" + touchLocation + "\nX:" + sprite.getX()
                        + "\nY:" + sprite.getY() + "\nCollision:" + collision + " DPI x,y:" + xdpi
                        + "," + ydpi +"score: "+score,
                        20, 40, paint);

                drawElements(level.elements);

                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        // If SimpleGameEngine Activity is started then
        // start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                //JUMP
                case MotionEvent.ACTION_POINTER_DOWN:

                    sprite.setDx(0);
                    sprite.setDy(yScale(-30));

                    break;

                case MotionEvent.ACTION_POINTER_UP:

                    touchLocation = motionEvent.getX();

                    if(touchLocation > xScale(950)){
                        sprite.setDx( walkSpeedPerSecond / fps);
                        //sprite.setGrav(walkSpeedPerSecond / fps / 2);
                    }else{
                        sprite.setDx( -walkSpeedPerSecond / fps );
                        //sprite.setGrav(-walkSpeedPerSecond / fps / 2);
                    }

                    break;
                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:

                    touchLocation = motionEvent.getX();

                    if(touchLocation > xScale(950)){
                        sprite.setDx( walkSpeedPerSecond / fps);
                        //sprite.setGrav(walkSpeedPerSecond / fps / 2);
                    }else{
                        sprite.setDx( -walkSpeedPerSecond / fps );
                        //sprite.setGrav(-walkSpeedPerSecond / fps / 2);
                    }

                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:

                    sprite.setDx(0);
                    //sprite.setGrav(0);

                    break;
            }
            return true;
        }

        public Level deserialize(String serializedLevelString){
            Type listType = new TypeToken<ArrayList<GameElement>>(){}.getType();
            ArrayList<GameElement> youClassList = new Gson().fromJson(serializedLevelString, listType);
            System.out.println("This is the class gotten" + youClassList.get(1));
            level = new Level(youClassList, null, null, null);
            for(GameElement ge : level.elements) {
                Drawable d = ContextCompat.getDrawable(getApplicationContext(), ElementStore.elements[ge.pic_id]);
                ge.setPic(d);
            }
            return level;
        }

        public GameElement getSprite(){
            for(int i = 0; i < level.elements.size(); i++){
                if(level.elements.get(i).isSprite){
                    System.out.println("The sprite is" + level.elements.get(i));
                    return level.elements.get(i);
                }
            }
            return null;
        }

        public void drawElements(ArrayList<GameElement> elements){

            for(int i = 0; i < elements.size(); i++){
                GameElement ge = elements.get(i);
                Drawable d = ge.pic;
                d.setBounds(xScale(ge.x), yScale(ge.y), xScale(ge.getRight()), yScale(ge.getBottom()));
                //System.out.println(""+xScale(ge.x)+","+yScale(ge.y)+","+xScale(ge.getRight())+","+yScale(ge.getBottom()));
                d.setAlpha(255);
                d.draw(canvas);

            }
        }

        private boolean checkHitboxes(){

            //check every element with sprite
            for(int i = 0; i < level.elements.size(); i++){

                //dont check the sprite to itself
                if(level.elements.get(i).isSprite != true){
                    GameElement ge = level.elements.get(i);
                    if(sprite.left() < ge.right() &&
                            sprite.right() > ge.left() &&
                            sprite.bottom() < ge.top() &&
                            sprite.top() > ge.bottom()){
                        collisionPenalty++;
                        fixHitboxes(ge);
                        return true;
                    }
                }
            }
            return false;
        }

        private void fixHitboxes(GameElement ge) {
            if (ge.top() > sprite.bottom() && ge.top() > oldSprite.bottom()) {
                sprite.setBottom(ge.top());
            } else if (ge.right() > sprite.left() && ge.right() < oldSprite.left()) {
                sprite.setLeft(ge.right());
            } else if (ge.left() > sprite.right() && ge.left() < oldSprite.right()) {
                sprite.setRight(ge.left());
            } else if (ge.bottom() < sprite.top() && ge.bottom() > oldSprite.top()) {
                sprite.setTop(ge.bottom());
            }
        }
        int xScale(int x) {
            float temp = x/480F;
            temp *= ydpi;
            return Math.round(temp);
        }

        int yScale(int y) {
            float temp = y/480F;
            temp *= xdpi;
            return Math.round(temp);
        }

    }

    // This is the end of our GameView inner class

    // More SimpleGameEngine methods will go here

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        gameView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        gameView.pause();
    }
    private long elapsedTime(){
        if((System.nanoTime() - start) >= 60000000000.0){
            signal = 1;
        }else{
            System.out.println("elapsed time: "+(System.nanoTime() - start));
        }
        return (System.nanoTime() - start);
    }
}

