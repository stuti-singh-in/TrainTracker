package com.example.traintracker;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String trainNumber;
    private String date;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_location);

        trainNumber = getIntent().getStringExtra("trainNumber");
        date = getIntent().getStringExtra("date");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng defaultLocation = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

        if (androidx.core.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
                androidx.core.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            androidx.core.app.ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        mMap.setMyLocationEnabled(true);

        startLocationUpdateTimer();
    }

    private void startLocationUpdateTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    LatLng trainLocation = DatabaseConnector.getTrainLocation(trainNumber, date);
                    if (trainLocation != null) {
                        runOnUiThread(() -> {
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(trainLocation).title("Train Location"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(trainLocation, 10));
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(DisplayLocationActivity.this, "Error retrieving train location", Toast.LENGTH_SHORT).show());
                }
            }
        }, 0, 60000); // Update every 1 minute (adjust as needed)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
