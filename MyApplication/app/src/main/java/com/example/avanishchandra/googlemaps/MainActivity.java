package com.example.avanishchandra.googlemaps;

import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.contextmanager.internal.TimeFilterImpl;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onMapReady(userMap);
        lRequest = new LocationRequest();
        lRequest.setInterval(3000);
        lRequest.setInterval(1000);
        lRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
    currentLocation = location;
    }
    public void onConnected(Bundle bundle){
        startLocationUpdates();
    }
    protected void startLocationUpdates() {
        System.out.println("Permissions");
        PackageManager packageManager = getApplicationContext().getPackageManager();
       if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("Entered Permissions");
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
}