package com.example.bernard.bananaclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Bernard on 9/17/2016.
 */
public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver.","Yay!");
        //get string from intent
        String get_your_string = intent.getExtras().getString("extra");
        Log.e("What is the key?",get_your_string);
        //create intent to ringtone
        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        //pass the extra string
        service_intent.putExtra("extra",get_your_string);
        //start the ringtone service
        context.startService(service_intent);

    }
}
