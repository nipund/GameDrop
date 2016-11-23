package com.example.mosebach.gamedroplogin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class EditorActivity extends AppCompatActivity {

    private TileView tv;
    double latititudeGet = 0;
    double longitudeGet = 0;
    //String url = "http://proj-309-gp-06.cs.iastate.edu/levels/create";
    String url = "http://aa6c7767.ngrok.io/levels/create/";
    private String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            latititudeGet = extras.getDouble("n1");
            longitudeGet = extras.getDouble("n2");
        }else{
            System.out.println("Null extras");
        }
        tv = (TileView) findViewById(R.id.tileView);
        tv.setOnTouchListener(new EditorTouchListener(tv, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_element:
                Intent i = new Intent(this, ElementSelectorActivity.class);
                startActivityForResult(i, 1);
                return true;
            case R.id.next_element:
                tv.nextElement();
                tv.invalidate();
                return true;
            case R.id.saveLevel:
                Gson gson = new Gson();
                //Type aryType = new TypeToken<GameElement>(){}.getType();
                json = gson.toJson(tv.elements);
                System.out.println(json.toString());
                sendObjectToVolley();
                System.out.println(json);
                return true;
            case R.id.setSprite:
                tv.setToSprite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int position = data.getIntExtra("position", -1);
                int id = (int) data.getLongExtra("drawable_id", -1);
                Drawable d = ContextCompat.getDrawable(getApplicationContext(), ElementStore.elements[position]);
                GameElement ge = new GameElement(position, 100, 100, 100, 100, "Test");
                ge.setPic(d);
                tv.elements.add(ge);
                tv.selectLastElement();
                tv.invalidate();
            }
        }
    }
    public void sendObjectToVolley() {
        // final String URL = "/volley/resource/12";
        // Post params to be sent to the server
        System.out.println("preparing to post to volley");


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                System.out.println("VolleyResponse" +response);
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String,String> getParams() {
                String keyName = "Current Location";
                HashMap<String, String> params = new HashMap<String, String>();
                //params.put("header", "application/x-www-form-urlencoded");
                params.put("lat", Double.toString(latititudeGet));
                params.put("lng", Double.toString(longitudeGet));
                params.put("Level", json);
                System.out.println(url + latititudeGet + "/" + longitudeGet);
                return params;
            }
        };

        // add the request object to the queue to be executed
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(req);
    }
}
