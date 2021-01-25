package com.example.caassandra_lee_2939561_exercisetracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class Calculate extends View {

    public List<Location> l = new ArrayList<Location>();
    Button stop;
    TextView speed;
    TextView timeTaken;
    TextView MinimumAltitude;
    TextView MaximumAltitude;
    TextView distance;
    private LocationCallback callback;

    FusedLocationProviderClient locateClient;
    LocationRequest request;


    public Calculate(Context context) {
        super(context);
    }

    public Calculate(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        context = this.getContext();


        timeTaken = findViewById(R.id.Time);
        speed = findViewById(R.id.Speed);
        distance = findViewById(R.id.Distance);
        MinimumAltitude = findViewById(R.id.MinAlt);
        MaximumAltitude = findViewById(R.id.MaxAlt);
    }


    public void calculateDistance(){


        //Location list = (Location) Track.getInstance().loc.lastIndexOf();

       /* for (int i = 0; i < Track.getInstance().loc.size(); i++) {
            Location previous = Track.getInstance().loc.get(i);
            Location current = Track.getInstance().loc.get(i + 1);


            double dist = previous.distanceTo(current);
            distance.setText(": " + dist);
        }*/


    }

    public void calculateSpeed(Location location){

        //utilise our second arraylist from Track activity dedicated to speed
      /*  for (float f: Track.getInstance().spd) {

            float totalSpeed =+ f;
            //Get average then set it to text;
            speed.setText("Speed: " + totalSpeed / Track.getInstance().spd.size());
        }
*/
    }

    public void calculateTime(Location location){

       // long time = location.getTime();

       /* for (long l: Track.getInstance().t) {
            long time = +l;

            //convert to minutes;
            time = time/60000;

            timeTaken.setText("Time Taken: " + time);
        }
*/

    }

    public void calculateAltitude(Location location){

       // double alt = Track.getInstance().alt;

       /* for (double a: Track.getInstance().alt) {
            //Get the minimum value
            double min = Track.getInstance().alt.indexOf(Collections.min(Track.getInstance().alt));
            MinimumAltitude.setText("Minimum Altitude: " + min);

            double max = Track.getInstance().alt.indexOf(Collections.max(Track.getInstance().alt));
            MinimumAltitude.setText("Maximum Altitude: " + max);

        }*/
    }


    public void Map(){


    }

}