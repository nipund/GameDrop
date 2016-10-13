package com.example.bernard.myfirstgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
    private ArrayList<BottomBorder> bottomborder;
    private Random rand = new Random();
    private int sprite;
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean topDown = true;
    private boolean botDown = true;
    //difficulty progression ,increase to slow
    private int progressDenom = 20;

    public GamePanel(Context context, int pic) {
        super(context);

        //add callback to surface holder to intercept events
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        //make Game Panel so it can handle events
        setFocusable(true);
        sprite = pic;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry&& counter<1000) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //draw
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        player = new Player(BitmapFactory.decodeResource(getResources(), sprite), 65,25,1);
        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        topborder = new ArrayList<TopBorder>();
        bottomborder = new ArrayList<BottomBorder>();

        smokeStartTime= System.nanoTime();
        missileStartTime = System.nanoTime();
        //we can start game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_DOWN){
            if(!player.getPlaying()){
                player.setPlaying(true);
            }else{
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if(player.getPlaying()) {
            bg.update();
            player.update();
            //calculate height to be based on score
            //border direction switch when min or max is reached

            maxBorderHeight= 30+ player.getScore()/progressDenom;
            //cap max border height to 1/2 of screen
            if(maxBorderHeight > HEIGHT /4 )
                maxBorderHeight = HEIGHT /4;

            minBorderHeight = 5+player.getScore()/progressDenom ;
            //update border
            this.updateTopBorder();

            this.updateBottomBorder();
            //add missiles on Timer
            long missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
            if(missileElapsed >(2000-player.getScore()/4)){
                System.out.println("making missiles");
                //first missile in the middle
                if(missiles.size()==0){
                    missiles.add(new Missile (BitmapFactory.decodeResource(getResources(),R.drawable.missile),WIDTH +10, HEIGHT/2,45,15
                            ,player.getScore(),13));
                }
                else{
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.missile),WIDTH+10 ,
                            (int)(rand.nextDouble()*(HEIGHT)),45,15,player.getScore(),13));

                }
                //reset Timer
            missileStartTime = System.nanoTime();
            }
            //loop through every missiles to check collision
            for (int i= 0;i<missiles.size();i++)
            {
                missiles.get(i).update();
                //remove missile and end game if collide
                if(collision(missiles.get(i),player)){
                    missiles.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //remove missiles if off screen
                if(missiles.get(i).getX()<-100)
                {
                    missiles.remove(i);
                    break;
                }
            }

            //add smoke puffs on timer

            long elapsed = (System.nanoTime()- smokeStartTime)/1000000;
            if(elapsed >120){
                smoke.add(new Smokepuff(player.getX(), player.getY()+10));
                smokeStartTime = System.nanoTime();
            }

            for(int i= 0;i <smoke.size(); i++){
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10){
                    smoke.remove(i);
                }
            }
        }

    }
    public boolean collision(GameObject a,GameObject b){
        if (Rect.intersects(a.getRectangle(),b.getRectangle())){
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);
        if (canvas != null) {
            final int savedState= canvas.save();



            canvas.scale(scaleFactorX,scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);
            for(Smokepuff sp:smoke){
                sp.draw(canvas);
            }
            for(Missile m:missiles){
                m.draw(canvas);
            }
            canvas.restoreToCount(savedState);
        }
    }
    public void updateTopBorder(){
        //every 50 points ,insert random blocks
        if(player.getScore()%50 == 0){
            topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),topborder.get
                    (topborder.size()-1).getX()+20,0,(int)((rand.nextDouble()*(maxBorderHeight))+1)));
        }

    }
    public void updateBottomBorder(){
        //every 40 pts, insert random
        if(player.getScore()%50 == 0){
        bottomborder.add(new BottomBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),bottomborder.get
                (bottomborder.size()-1).getX()+20,(int)((rand.nextDouble()*(maxBorderHeight)))));
    }

}
