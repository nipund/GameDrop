package com.example.mosebach.gamedroplogin;

import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.contextmanager.internal.TimeFilterImpl;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Avanish Chandra on 10/20/2016.
 */
public class LocationPage extends AppCompatActivity  {
    String url = "http://proj-309-gp-06.cs.iastate.edu/markers/create";
    double latitiutdeGet = 0;
    double longitudeGet = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_page);
        System.out.println("Entered locationPage"+ "latitiutdeGet" + " " + longitudeGet);
        Log.i("LocationPage", "entered locationpage " + latitiutdeGet + " " + longitudeGet);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            latitiutdeGet = extras.getDouble("n1");
            longitudeGet = extras.getDouble("n2");
        }else{
            System.out.println("Null extras");
        }
        System.out.println("n1: "+latitiutdeGet+" "+"n2: "+latitiutdeGet);
    }
   /* public void getLocationService(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()){

        };
    }*/

   /* public void dropGame(){
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
                    params.put("lat", currentLatitudeText);
                    params.put("lng", currentLongitudeText);
                    params.put("name", keyName);
                    return params;
                }
            };

            // add the request object to the queue to be executed
            RequestQueue rq = Volley.newRequestQueue(this);
            rq.add(req);
        }*/
}
