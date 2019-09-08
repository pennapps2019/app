package com.pennapps2019.application;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final int REQUEST_CODE = 1;
    private static final String TAG = MapsActivity.class.getName();

    private FusedLocationProviderClient fusedLocationClient = null;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private GoogleMap mMap;

    private LocationRecorder locationRecorder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.d(TAG, "Instantiating");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Toast.makeText(getApplicationContext(), "Received location.", Toast.LENGTH_SHORT).show();

                for (Location l : locationResult.getLocations()) {
                    Log.d(TAG, "Location: " + l.getLatitude() + "," + l.getLongitude());

                    Location gridLoc = roundToGrid(l);
                    Log.d(
                            TAG,
                            "Location rounded to: " + gridLoc.getLatitude() + "," + gridLoc.getLongitude()
                    );
                    locationRecorder.recordAt(gridLoc);
                }
            }
        };

        locationRecorder = new LocationRecorder();

        boolean permissionAccessFineLocationApproved =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        if (!permissionAccessFineLocationApproved) {
            // App doesn't have access to the device's location at all. Make full request
            // for permission.
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    0);
        }

        // Set button behaviour
        final Button button = findViewById(R.id.create_output_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createOutputLog(locationRecorder.dump());
            }
        });

    }

    private Location roundToGrid(Location l) {
        final Double gridSize = 0.00005d;

        Double latitude = l.getLatitude();
        Double longitude = l.getLongitude();

        // Round down to nearest grid size
        latitude -= latitude % gridSize;
        longitude -= longitude % gridSize;

        // Move to center of grid square
        latitude += gridSize/2;
        longitude += gridSize/2;

        // Return
        Location res = new Location("");
        res.setLatitude(latitude);
        res.setLongitude(longitude);
        return res;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addHeatMap();

        LatLng levineHall = new LatLng(39.952090, -75.190840);
        mMap.addMarker(new MarkerOptions().position(levineHall).title("Marker at Levine Hall"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(levineHall));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }

    private void addHeatMap() {
        List<LatLng> list = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            list = readItems(R.raw.police_stations);
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        TileOverlay mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null);

    }

    private void createOutputLog(String content) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE
                );
            }
        }

        try {
            File locationLog = new File(
                    getApplicationContext().getExternalFilesDir(null),
                    "location-log.csv"
            );
            FileWriter writer = new FileWriter(locationLog);
            writer.write(content);
            writer.flush();
            writer.close();

            Log.d(TAG, "Created location log file at " + locationLog.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Toast.makeText(this, "Created location log file.", Toast.LENGTH_LONG).show();

    }

}

