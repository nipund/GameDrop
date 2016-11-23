package com.example.mosebach.gamedroplogin;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Bernard on 10/3/2016.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private long smokeStartTime;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private ArrayList<TopBorder> topborder;
    private ArrayList<BotBorder> bottomborder;
    private Random rand = new Random();
    private int sprite;
    private int PlayerHeight;
    private int PlayerWidth;
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated;
    //difficulty progression ,increase to slow
    private int progressDenom = 20;
    private Explosion explosion;
    private long startReset ;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private int best;
    private int startY;
    public Level level;



    public GamePanel(Context context, int pic) {
        super(context);

        //add callback to surface holder to intercept events
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        //make Game Panel so it can handle events
        setFocusable(true);
        sprite = pic;
        //TO BE CHANGED ACCORDING TO SPRITE USED
        PlayerHeight = 65 ;
        PlayerWidth = 25;
        startY = GamePanel.HEIGHT/5;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread=null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //draw
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        player = new Player(BitmapFactory.decodeResource(getResources(), sprite),PlayerHeight,PlayerWidth, 1);
        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        topborder = new ArrayList<TopBorder>();
        bottomborder = new ArrayList<BotBorder>();
        smokeStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();
        thread = new MainThread(getHolder(), this);
        //we can start game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!player.getPlaying() && newGameCreated&&reset) {
                player.setPlaying(true);
                player.setUp(true);
            } if(player.getPlaying()){
                if(!started)started = true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if (player.getPlaying()) {
            if(bottomborder.isEmpty()){
                player.setPlaying(false);
                return;
            }
            if (topborder.isEmpty()) {

                player.setPlaying(false);
                return;
            }
            bg.update();
            player.update();
            //calculate height to be based on score
            //border direction switch when min or max is reached

            maxBorderHeight = 30 + player.getScore() / progressDenom;
            //cap max border height to 1/2 of screen
            if (maxBorderHeight > HEIGHT / 4)maxBorderHeight = HEIGHT / 4;
            minBorderHeight = 5 + player.getScore() / progressDenom;
            //check bottom border for collision


            for (int i = 0; i < bottomborder.size(); i++) {
                if (collision(bottomborder.get(i), player))
                    player.setPlaying(false);
            }

            //check top border collision
            for (int i = 0; i < topborder.size(); i++) {
                if (collision(topborder.get(i), player))
                    player.setPlaying(false);
            }
            //update border
            this.updateTopBorder();

            this.updateBottomBorder();

            //add missiles on Timer
            long missileElapsed = (System.nanoTime() - missileStartTime) / 1000000;
            if (missileElapsed > (2000 - player.getScore() / 4)) {
                System.out.println("making missiles");
                //first missile in the middle
                if (missiles.size() == 0) {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile), WIDTH + 10, HEIGHT / 2, 45, 15
                            , player.getScore(), 13));
                } else {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile), WIDTH + 10,
                            (int) (rand.nextDouble() * (HEIGHT -(maxBorderHeight * 2))+maxBorderHeight), 45, 15, player.getScore(), 13));

                }
                //reset Timer
                missileStartTime = System.nanoTime();
            }
            //loop through every missiles to check collision
            for (int i = 0; i < missiles.size(); i++) {
                missiles.get(i).update();
                //remove missile and end game if collide
                if (collision(missiles.get(i), player)) {
                    missiles.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //remove missiles if off screen
                if (missiles.get(i).getX() < -100) {
                    missiles.remove(i);
                    break;
                }
            }

            //add smoke puffs on timer

            long elapsed = (System.nanoTime() - smokeStartTime) / 1000000;
            if (elapsed > 120) {
                smoke.add(new Smokepuff(player.getX(), player.getY() + 10));
                smokeStartTime = System.nanoTime();
            }

            for (int i = 0; i < smoke.size(); i++) {
                smoke.get(i).update();
                if (smoke.get(i).getX() < -10) {
                    smoke.remove(i);
                }
            }
        }
        else {
            player.resetDY();
            if(!reset){
                newGameCreated= false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;
                explosion = new Explosion (BitmapFactory.decodeResource(getResources(),R.drawable.explosion),player.getX(),player.getY()-30,100,100,25);
            }
            explosion.update();;
            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if (resetElapsed >2500&&!newGameCreated){
                newGame();

            }
            newGameCreated = false;
            if (!newGameCreated) {
                newGame();
            }
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
        if (canvas != null) {
            final int savedState = canvas.save();


            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            if(!dissapear) {
                player.draw(canvas);
            }
            //draw smoke puffs
            for (Smokepuff sp : smoke) {
                sp.draw(canvas);
            }
            //draw missiles
            for (Missile m : missiles) {
                m.draw(canvas);
            }

            //draw topborder
            for (TopBorder tb : topborder) {
                tb.draw(canvas);
            }

            //draw botborder
            for (BotBorder bb : bottomborder) {
                bb.draw(canvas);
            }
            //draw explosion
            if(started){
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);

        }
    }

    public void updateTopBorder() {
        //every 50 points, insert randomly placed top blocks that break the pattern
        if (player.getScore() % 50 == 0) {
            topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
            ), topborder.get(topborder.size() - 1).getX() + 20, 0, (int) ((rand.nextDouble() * (maxBorderHeight
            )) + 1)));
        }
        for (int i = 0; i < topborder.size(); i++) {
            topborder.get(i).update();
            if (topborder.get(i).getX() < -20) {
                topborder.remove(i);
                //remove element of arraylist, replace it by adding a new one

                //calculate topdown which determines the direction the border is moving (up or down)
                if (topborder.get(topborder.size() - 1).getHeight() >= maxBorderHeight) {
                    topDown = false;
                }
                if (topborder.get(topborder.size() - 1).getHeight() <= minBorderHeight) {
                    topDown = true;
                }
                //new border added will have larger height
                if (topDown) {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick), topborder.get(topborder.size() - 1).getX() + 20,
                            0, topborder.get(topborder.size() - 1).getHeight() + 1));
                }
                //new border added wil have smaller height
                else {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick), topborder.get(topborder.size() - 1).getX() + 20,
                            0, topborder.get(topborder.size() - 1).getHeight() - 1));
                }

            }
        }

    }

    public void updateBottomBorder() {  //every 40 points, insert randomly placed bottom blocks that break pattern
        if (player.getScore() % 40 == 0) {
            bottomborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                    bottomborder.get(bottomborder.size() - 1).getX() + 20, (int) ((rand.nextDouble()
                    * maxBorderHeight) + (HEIGHT - maxBorderHeight))));
        }

        //update bottom border
        for (int i = 0; i < bottomborder.size(); i++) {
            bottomborder.get(i).update();

            //if border is moving off screen, remove it and add a corresponding new one
            if (bottomborder.get(i).getX() < -20) {
                bottomborder.remove(i);


                //determine if border will be moving up or down
                if (bottomborder.get(bottomborder.size() - 1).getY() <= HEIGHT - maxBorderHeight) {
                    botDown = true;
                }
                if (bottomborder.get(bottomborder.size() - 1).getY() >= HEIGHT - minBorderHeight) {
                    botDown = false;
                }

                if (botDown) {
                    bottomborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                    ), bottomborder.get(bottomborder.size() - 1).getX() + 20, bottomborder.get(bottomborder.size() - 1
                    ).getY() + 1));
                } else {
                    bottomborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                    ), bottomborder.get(bottomborder.size() - 1).getX() + 20, bottomborder.get(bottomborder.size() - 1
                    ).getY() - 1));
                }
            }
        }
    } public void newGame()
    {

        dissapear = false;
        bottomborder.clear();
        topborder.clear();
        missiles.clear();
        smoke.clear();

        minBorderHeight = 5;
        maxBorderHeight = 30;

        player.resetDY();
        player.resetScore();
        //resets starting position of Player
        player.setY(startY);
        if(player.getScore()>best){
            best = player.getScore();
        }

        //create initial borders

        //initial top border
        for(int i = 0; i*20<WIDTH+40;i++)
        {
            //first top border create
            if(i==0)
            {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick
                ),i*20,0, 10));
            }
            else
            {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick
                ),i*20,0, topborder.get(i-1).getHeight()+1));
            }
        }
        //initial bottom border
        for(int i = 0; i*20<WIDTH+40; i++)
        {
            //first border ever created
            if(i==0)
            {
                bottomborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick)
                        ,i*20,HEIGHT - minBorderHeight));
            }
            //adding borders until the initial screen is filed
            else
            {
                bottomborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                        i * 20, bottomborder.get(i - 1).getY() - 1));
            }
        }

        newGameCreated = true;


    }
    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        canvas.drawText("DISTANCE: "+ (player.getScore()*3),10,HEIGHT -10 , paint);
        canvas.drawText("BEST:"+ best, WIDTH -215,HEIGHT-10,paint);
        if(!player.getPlaying()&&newGameCreated&&reset){
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
            canvas.drawText("PRESS TO START ",WIDTH/2-50,HEIGHT/2 ,paint1);
            paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP",WIDTH/2-50,HEIGHT/2+20 ,paint1);
            canvas.drawText("RELEASE TO GO DOWN",WIDTH/2-50,HEIGHT/2+40 ,paint1);
        }
    }
    public Level deserialize(String serializedLevelString){

        Type listType = new TypeToken<ArrayList<GameElement>>(){}.getType();

        ArrayList<GameElement> youClassList = new Gson().fromJson(serializedLevelString, listType);

        System.out.println(youClassList.get(1));

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
                return level.elements.get(i);
            }
        }
        return null;
    }



}

