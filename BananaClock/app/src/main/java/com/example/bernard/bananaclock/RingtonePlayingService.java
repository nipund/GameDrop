package com.example.bernard.bananaclock;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.security.Provider;

/**
 * Created by Bernard on 9/17/2016.
 */
public class RingtonePlayingService extends Service{
    MediaPlayer media_song;
    int startId;
    boolean isRunning;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.i("LocalService","Received start id"+startId + ": "+ intent);
        //get extra value
        String state= intent.getExtras().getString("extra");
        Log.e("Ringtone state:extra is",state);

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("Start ID is ",state);
                break;
            default:
                startId = 0;
                break;
        }

        //if else
    if(!this.isRunning && startId == 1){
        Log.e("there is no music ","and start");
        media_song = MediaPlayer.create(this,R.raw.bae);
        media_song.start();
        this.isRunning = true;
        this.startId=0;
    }
        else if(this.isRunning && startId == 0){
        Log.e("there is music ","and end");
        media_song.stop();
        media_song.reset();
        this.isRunning=false;
        this.startId=0;

        }else if (!this.isRunning && startId == 0){
        Log.e("there is no  music ","and end");
        this.isRunning=false;
        this.startId=0;

        }else if(this.isRunning && startId == 1){
        Log.e("there is music ","and start");
        this.isRunning=true;
        this.startId=1;

        }else {
        Log.e("else ", " u mad");
    }








        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy(){
        Log.e("on Destroy called ","boom!");
        super.onDestroy();
        this.isRunning = false;

    }
}
