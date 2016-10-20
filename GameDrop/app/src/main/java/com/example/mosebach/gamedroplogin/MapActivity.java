package com.example.mosebach.gamedroplogin;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private LatLng tempLng;
    private Location currentLocation;
    private GoogleMap userMap;
    GoogleApiClient userGoogleApiClient;
    LocationRequest lRequest;
    private LocationManager myLocationManger;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        String text;
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            text = "";
        }else{
            text = extras.getString("Username");
        }

        TextView textView = (TextView) findViewById(R.id.textUsername);
        textView.setText(text);
        onMapReady(userMap);
        userGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        onStart();
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
                double n1 = currentLocation.getLatitude();
                double n2 = currentLocation.getLongitude();
                tempLng = new LatLng(n1,n2);
                userMap.clear();
                Marker temp = userMap.addMarker(new MarkerOptions().position(tempLng).title("Current Location: " + "Lat :"+n1+" "+"Long :"+n2));
                userMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tempLng, 17));
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
                double n1 = currentLocation.getLongitude();
                double n2 = currentLocation.getLongitude();
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
}