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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Avanish Chandra on 10/20/2016.
 */
public class LocationPage extends AppCompatActivity implements OnMapReadyCallback,LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    String url = "http://proj-309-gp-06.cs.iastate.edu/markers/within/";
    double latititudeGet = 0;
    double longitudeGet = 0;
    private GoogleMap userMap;
    GoogleApiClient userGoogleApiClient;
    LocationRequest lRequest;
    int mapToggle = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_page);
        System.out.println("Entered locationPage"+ "latitiutdeGet" + " " + longitudeGet);
        Log.i("LocationPage", "entered locationpage " + latititudeGet + " " + longitudeGet);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            latititudeGet = extras.getDouble("n1");
            longitudeGet = extras.getDouble("n2");
        }else{
            System.out.println("Null extras");
        }
        System.out.println("n1: "+latititudeGet+" "+"n2: "+latititudeGet);
        onMapReady(userMap);
        userGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        getMarkers();
    }
    private void initialMap() {
        if (userMap == null) {
            MapFragment tempFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            tempFrag.getMapAsync(this);
            mapToggle = 1;
        }
    }

    public void onMapReady(GoogleMap map) {
        userMap = map;
        if(mapToggle != 1) {
            initialMap();
        }
    }
    public void onLocationChanged(Location location){
    }
    public void getMarkers(){

        //"http://proj-309-gp-06.cs.iastate.edu/users/login/" + userName.getText() + "/" + password.getText();
        //pat test     http://proj-309-gp-06.cs.iastate.edu/users/login/pat/test
        //String URL = "http://proj-309-gp-06.cs.iastate.edu/users/login/pat/test";
        final String getMarkerArray = url + latititudeGet + "//" + longitudeGet + "//" + "2000";
        // Post params to be sent to the server
        //HashMap<String, String> params = new HashMap<String, String>();
        //params.put("token", "AbCdEfGh123456");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, getMarkerArray, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            if(response.getBoolean("success")){
                                JSONArray markerArray  = response.getJSONArray("markers");
                                for(int i = 0; i<markerArray.length(); i++ ){
                                    JSONObject object = markerArray.getJSONObject(i);
                                    double tempLat = object.getDouble("lat");
                                    double tempLng = object.getDouble("lng");
                                    String name = object.getString("name");
                                    String ID = object.getString("id");
                                    System.out.println("Lat " + tempLat + " Lng "+ tempLng + " Name " + name + " id" + ID);
                                    LatLng tempCoord = new LatLng(tempLat,tempLng);
                                    Marker temp = userMap.addMarker(new MarkerOptions().position(tempCoord).title(name + " Lat :"+tempLat+" "+"Long :"+tempLng +" ID:" + ID));
                                }
                                System.out.println(getMarkerArray);
                            }else{
                                System.out.println("Access to markers failed");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }
    public void onConnected(Bundle bundle){
        createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(lRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(userGoogleApiClient,builder.build());
        if(lRequest != null) {
            System.out.println("Not Connected");
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
        LocationServices.FusedLocationApi.removeLocationUpdates(userGoogleApiClient, this);
    }
    public void onResume() {
        super.onResume();
        if (userGoogleApiClient.isConnected()) {
            System.out.println("Not Connected");
        }
    }
    public void createLocationRequest(){
        lRequest = new LocationRequest();
        lRequest.setInterval(200);
        lRequest.setFastestInterval(100);
        lRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}
