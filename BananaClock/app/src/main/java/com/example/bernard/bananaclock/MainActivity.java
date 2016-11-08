package com.example.bernard.bananaclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //make alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this ;
        //init alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize time picker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);
        //init text updatebox
        update_text = (TextView)findViewById(R.id.update_text);
        //calendar instance
        final Calendar calender = Calendar.getInstance();


        //intialize start button
        Button alarm_on = (Button) findViewById(R.id.alarm_on);

        //Create an intent to the Alarm Receiver class
        final Intent my_intent = new Intent(this.context,Alarm_Receiver.class);
        //On click to start alarm
            alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting calendar
                calender.set(Calendar.HOUR_OF_DAY,alarm_timepicker.getHour());
                calender.set(Calendar.MINUTE,alarm_timepicker.getMinute());
               //int values of hour and minute
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();
                //convert int to string
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);
                //Conversion
                if(hour >12 ){
                    hour_string = String.valueOf(hour - 12 );
                }if(minute<10 ){
                    minute_string = "0" + String.valueOf(minute);
                }

                //changes the update text TextBox
                set_alarm_text("Alarm set to :"+hour_string + ":" + minute_string);
                //extra string in my_intent,press on button
                my_intent.putExtra("extra","alarm on");

                //pending intetnt that delays intent
                pending_intent = PendingIntent.getBroadcast(MainActivity.this , 0,my_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarm_manager.set(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),pending_intent);
            }


        });

        //intialize stop button
        Button alarm_off = (Button) findViewById(R.id.alarm_off);

        //On click to stop the alarm
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changes the update text TextBox
                set_alarm_text("Alarm off");
                //cancel alarm
                alarm_manager.cancel(pending_intent);
                //put extra string into my_intent,off button
                my_intent.putExtra("extra", "alarm off");
                //stop ringtone
                sendBroadcast(my_intent);
            }
        });


    }

    private void set_alarm_text(String output) {
        update_text.setText(output);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
