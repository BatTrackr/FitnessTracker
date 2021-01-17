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

public class Track extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Button Track;
    double lat;
    double lon;
    double alt;
    double time;
    TextView speed;
    TextView timeTaken;
    TextView altitude;
    TextView distance;
    int i = 0;
    private LocationCallback callback;
    FusedLocationProviderClient locateClient;
    LocationRequest request;
    Boolean trackerStarted = false;


    BufferedWriter writer;
    String folder = "GPStracks";
    File GPS;
    String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.15.5\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n";
    String name = "Latitude: %1$.4f \n Longitude: %2$.4f\n Timestamp: %3$tr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Track = findViewById(R.id.track);
        timeTaken = findViewById(R.id.Time);
        speed = findViewById(R.id.Speed);
        distance = findViewById(R.id.Distance);
        altitude = findViewById(R.id.Alt);

        request = new LocationRequest();
        locateClient = LocationServices.getFusedLocationProviderClient(this);

        request.setInterval(1000000);
        request.setPriority(request.PRIORITY_BALANCED_POWER_ACCURACY);
        Track.setOnClickListener(this);

        callback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateUI();
                for (Location location : locationResult.getLocations()){
                    //update the file with our callback
                    updateFile(location);
                }
            }
        };

    }

    @Override
    public void onClick(View v) {
        if (i == 0 && !trackerStarted) {
            startTracker();
            Toast.makeText(Track.this, "Tracker Started", Toast.LENGTH_SHORT).show();
            trackerStarted = true;
            i++;
        } else if (i == 1 && trackerStarted) {
            stopTracker(writer);
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

        }
    }

    public void stopTracker(Writer writer) {

        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //stop getting updates
        locateClient.removeLocationUpdates(callback);
    }

    public void updateFile(Location location) {
        String locationInformation = location.toString();

        writer = null;
        //create a new folder which will be our filepath for our GPX fike
        GPS = new File(getExternalFilesDir(null).getAbsolutePath() + folder);
        try {
            if (!GPS.exists()) {
                GPS.mkdirs();
            }
            File f = new File(GPS, "17-01-21 8:43pm.gpx");


            FileWriter fileWriter = new FileWriter(f, true);
            writer = new BufferedWriter(fileWriter);
            while (writer != null) {
                writer.append(header);
                writer.append(locationInformation);
                Log.i("File save", "Saved ");

            }
        } catch (IOException e) {
            Log.e("File Save", "Error Writing Path");
        }

    }

    public void updateUI() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locateClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            alt = location.getAltitude();
                            time = location.getTime();

                            speed.setText(": " + location.getSpeed());
                            timeTaken.setText(": " + time);
                            altitude.setText(": " + alt);
                            distance.setText(": ");



                        } else {
                            ActivityCompat.requestPermissions(Track.this, new String[]
                                            {Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATION_PERMISSION);
                        }
                    }
                });
            }
            return;
        }

}