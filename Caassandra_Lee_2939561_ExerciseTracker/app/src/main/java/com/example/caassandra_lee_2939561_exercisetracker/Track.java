package com.example.caassandra_lee_2939561_exercisetracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Track extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Button Track;

    List<Double> alt = new ArrayList<>();
    List<Long> t = new ArrayList<>();
    List<Float> spd = new ArrayList<>();
    int i = 0;
    private static Track tracker = null;
    List<Location> loc = new ArrayList<>();
    private LocationCallback callback;
    FusedLocationProviderClient locateClient;
    LocationRequest request;
    Boolean trackerStarted = false;
    Calculate calculate;


    String folder = "GPStracks" ;
    File GPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Track = findViewById(R.id.track);
        Calculate custom = findViewById(R.id.Graph);

        request = new LocationRequest();
        locateClient = LocationServices.getFusedLocationProviderClient(this);


        request.setInterval(12000);
        request.setPriority(request.PRIORITY_BALANCED_POWER_ACCURACY);
        Track.setOnClickListener(this);
        callback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    //update the file with our callback
                    updateFile(locationResult.getLastLocation());

                    //Add data to each list to be used in Calculate
                    loc.add(i, location);
                    spd.add(i, location.getSpeed());
                    t.add(i, location.getTime());
                    alt.add(i, location.getAltitude());

                }
            }
        };

        calculate.calculateDistance();
        calculate.calculateSpeed();
        calculate.calculateTime();
        calculate.calculateAltitude();
    }

    public Track(){

    }

    @Override
    public void onClick(View v) {
        if (i == 0 && !trackerStarted) {
            startTracker();
            Toast.makeText(Track.this, "Tracker Started", Toast.LENGTH_SHORT).show();
            trackerStarted = true;
            i++;
        } else if (i == 1 && trackerStarted) {
            stopTracker();
            Toast.makeText(Track.this, "Tracker Stopped", Toast.LENGTH_SHORT).show();
            trackerStarted = false;
            i = 0;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTracker();
                } else {
                    Toast.makeText(this,
                            "Permission request denied, this app needs access to work",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }

    public void startTracker() {

        //Get permission from the user to track

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locateClient.requestLocationUpdates(request, callback, null);
            locateClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {

                        updateFile(location);


                    } else {
                        ActivityCompat.requestPermissions(Track.this, new String[]
                                        {Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_LOCATION_PERMISSION);
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(Track.this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    public void stopTracker() {

        try {
            FileWriter fileWriter = new FileWriter(GPS, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //stop getting updates
        locateClient.removeLocationUpdates(callback);
    }

    public void updateFile(Location location) {
        String latitude = location.convert(location.getLatitude(), location.FORMAT_DEGREES);
        String Longitude = location.convert(location.getLongitude(), Location.FORMAT_DEGREES);




        try {
            GPS = new File(this.getExternalFilesDir(null) + " \\GPStracks ", System.currentTimeMillis() +".txt");

            //if path doesn't exist, create it
            //create a new folder which will be our filepath for our GPX file
            if (!GPS.exists()) {
                GPS.mkdirs();
            }
            FileWriter fileWriter = new FileWriter(GPS, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.append("Location" + location);
            //writer.append("Latitude: " + latitude);
            writer.newLine();
            Log.i("File save", "Saved ");

        } catch (IOException e) {
            Log.e("File Save", "Error Writing Path");
        }

    }
    public static Track getInstance()
    {
        if (tracker == null)
            tracker = new Track();

        return tracker;
    }
}


