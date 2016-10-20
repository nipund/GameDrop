package com.example.avanishchandra.googlemaps;

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
import com.google.android.gms.contextmanager.internal.TimeFilterImpl;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private LatLng tempLng;
    private String currentLatitudeText,currentLongitudeText;
    private Location currentLocation;
    private GoogleMap userMap;
    GoogleApiClient userGoogleApiClient;
    LocationRequest lRequest;
    private LocationManager myLocationManger;
    private Bundle bundle;
    private String url = "http://proj-309-gp-06.cs.iastate.edu/markers/create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onMapReady(userMap);
        userGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //sendCoordToVolley();
        onStart();
       Button moveToLocation = (Button)findViewById(R.id.LevelEditorButton);
       moveToLocation.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               Intent intent = new Intent(MainActivity.this, LevelEditor.class);
               startActivity(intent);
           }
       });
    }

    private void initialMap() {
        if (userMap == null) {
            MapFragment tempFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            tempFrag.getMapAsync(this);
        }
    }

    public void onMapReady(GoogleMap map) {
        userMap = map;
        initialMap();
    }
    public void onLocationChanged(Location location){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(userGoogleApiClient);
            if(currentLocation != null){
                currentLatitudeText = String.valueOf(currentLocation.getLatitude());
                currentLongitudeText = String.valueOf(currentLocation.getLongitude());
                double n1 = Double.parseDouble(currentLatitudeText);
                double n2 = Double.parseDouble(currentLongitudeText);
                tempLng = new LatLng(n1,n2);
                userMap.clear();
                Marker temp = userMap.addMarker(new MarkerOptions().position(tempLng).title("Current Location: " + "Lat :"+n1+" "+"Long :"+n2));
                sendCoordToVolley();
                System.out.println("MarkerMade");
                System.out.println(n1 + " " + n2);
            }else{
                System.out.println("No location");
            }
        }
    }
    public void onConnected(Bundle bundle){
        createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(lRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(userGoogleApiClient,builder.build());
        if(lRequest != null) {
            startLocationUpdates();
        }
    }
    protected void startLocationUpdates() {
        System.out.println("Permissions");
        PackageManager packageManager = getApplicationContext().getPackageManager();
       if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("Entered Permissions");
           LocationServices.FusedLocationApi.requestLocationUpdates(userGoogleApiClient,lRequest,this);
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(userGoogleApiClient);
            if(currentLocation != null){
                currentLatitudeText = String.valueOf(currentLocation.getLatitude());
                currentLongitudeText = String.valueOf(currentLocation.getLongitude());
                double n1 = Double.parseDouble(currentLatitudeText);
                double n2 = Double.parseDouble(currentLongitudeText);
                tempLng = new LatLng(n1,n2);
                Marker temp = userMap.addMarker(new MarkerOptions().position(tempLng).title("Current Location"));
                System.out.println("MarkerMade");
                System.out.println(n1 + " " + n2);
            }else{
                System.out.println("No location");
            }

        }
    }
    public void onConnectionSuspended(int myInt){

    }
    public void onConnectionFailed(ConnectionResult connectionResult){

    }
    public void onStart() {
        super.onStart();
        userGoogleApiClient.connect();
        System.out.println("Connected to client");
    }

    @Override
    public void onStop() {
        super.onStop();
        userGoogleApiClient.disconnect();
    }
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                userGoogleApiClient, this);
    }
    public void onResume() {
        super.onResume();
        if (userGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }
    public void createLocationRequest(){
        lRequest = new LocationRequest();
        lRequest.setInterval(200);
        lRequest.setFastestInterval(100);
        lRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    public void sendCoordToVolley() {
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
    }
    public void postToVolley(){
    }
}