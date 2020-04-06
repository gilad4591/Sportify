package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final int REQUEST_PERMISSION_LOCATION = 255;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();
    private LocationManager lm;
    private Location myLocation;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double myLatitude = myLocation.getLatitude();
                double myLongitude = myLocation.getLongitude();

                LatLng latLng = new LatLng(myLatitude, myLongitude);
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title(latLng.toString()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18.0f));
            } else {
                // do request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 8);
            }
        }
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //private Fused
                Double lat=0.0;
                Double lon=0.0;
//                Object a = dataSnapshot.getValue();
                ArrayList<Map<String, String>> locations = (ArrayList<Map<String, String>>) dataSnapshot.getValue();
                ArrayList<LatLng>latLngs =new ArrayList<>();
                ArrayList<String>names = new ArrayList<>();
                String name="";
                for (Map<String, String> entry : locations) {
                    for (String key : entry.keySet()) {
                        String val = entry.get(key);
                        if(key.equals("lat")){
                             lat = Double.parseDouble(val);
                        }
                        else if(key.equals("lon")){
                             lon = Double.parseDouble(val);
                        }
                        else if(key.equals("Name")){
                            name = val;
                        }
                    }
                    names.add(name);
                    latLngs.add(new LatLng(lat,lon));
                }
                int i =0;
                for(LatLng latLng:latLngs){
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(names.get(i)));
                    i++;
                }
                //fusedLocationClient.getLastLocation();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void askLocationPermission() {
    }


    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}