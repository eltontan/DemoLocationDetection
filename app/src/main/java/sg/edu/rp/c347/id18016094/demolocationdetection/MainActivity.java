package sg.edu.rp.c347.id18016094.demolocationdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button btnGetLastLocation, btnGetLocationUpdate, btnRemoveLocationUpdates;
    FusedLocationProviderClient client;
    LocationCallback mLocationCallback;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetLastLocation = findViewById(R.id.btnGetLastLocation);
        btnGetLocationUpdate = findViewById(R.id.btnGetLocationUpdate);
        btnRemoveLocationUpdates = findViewById(R.id.btnRemoveLocationUpdate);

        client = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                if (locationResult != null){
                    Location locData = locationResult.getLastLocation();
                    String msg = "New Loc Detected\n" +
                            "Lat: " + locData.getLatitude() + ", " +
                            "Lng: " + locData.getLongitude();
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnGetLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission() == true){
                    client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                }
            }
        });
        btnGetLastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission() == true) {
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            //Get last known location.
                            if (location !=null) {
                                String msg = "Lat : " + location.getLatitude() +
                                        "Lng : " + location.getLongitude();
                                Toast.makeText(MainActivity.this,msg, Toast.LENGTH_SHORT).show();
                            } else {
                                String msg = "No last known location found";
                                Toast.makeText(MainActivity.this, msg , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btnGetLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission() == true) {
                    client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                }
            }
        });

        btnRemoveLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.removeLocationUpdates(mLocationCallback);
            }
        });
    }
    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}