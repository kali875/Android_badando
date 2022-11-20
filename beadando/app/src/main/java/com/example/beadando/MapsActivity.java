package com.example.beadando;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final int REQUEST_CODE =0;
    static private GoogleMap GMap;

    private static int User_Id = User.ID;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    private static final String KEY_LOCATION = "location";

    private static final String TAG = MapsActivity.class.getSimpleName();

    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);

    FusedLocationProviderClient fusedLocationProviderClient;
    PlacesClient placesClient = new PlacesClient() {
        @NonNull
        @Override
        public Task<FetchPhotoResponse> fetchPhoto(@NonNull FetchPhotoRequest fetchPhotoRequest) {
            return null;
        }

        @NonNull
        @Override
        public Task<FetchPlaceResponse> fetchPlace(@NonNull FetchPlaceRequest fetchPlaceRequest) {
            return null;
        }

        @NonNull
        @Override
        public Task<FindAutocompletePredictionsResponse> findAutocompletePredictions(@NonNull FindAutocompletePredictionsRequest findAutocompletePredictionsRequest) {
            return null;
        }

        @NonNull
        @Override
        public Task<FindCurrentPlaceResponse> findCurrentPlace(@NonNull FindCurrentPlaceRequest findCurrentPlaceRequest) {
            return null;
        }
    };


    static ListView listView;
    static Button button;
    static EditText editText;
    static Button Button_Call;
    static String Friend_Name;
    static Button Button_Friend;
    static EditText Search;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), "AIzaSyC09C4HuYlNWZK5_VG6mOnJMdUxifXhv8Y");
        placesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        //example GUI element manipulation
        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);
        //editText = (EditText) findViewById(R.id.firend_request_text );

        Button_Call = (Button) findViewById(R.id.Button_Call_Friend);
        Button_Friend = (Button) findViewById(R.id.Search_friend);
        Search = (EditText) findViewById(R.id.FriendName);
        // Create a List from String Array elements
        new Search_Friends(getApplicationContext()).execute(String.valueOf(String.valueOf(User_Id)));
        // Create an ArrayAdapter from List


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Not Enough Permission", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                new Mylocation().execute(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()),String.valueOf(User_Id));
            }
            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStatusChanged(String provider, int status,Bundle extras) {
                // TODO Auto-generated method stub
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                try{
                    for (int ctr=0;ctr<=listView.getCount();ctr++){
                        if(i==ctr)
                        {
                            listView.getChildAt(ctr).setBackgroundColor(Color.GRAY);
                        }else
                        {
                            listView.getChildAt(ctr).setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Friend_Name = String.valueOf((String) listView.getItemAtPosition(i));
                new Location_shared(getApplicationContext(),GMap,lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()).execute(String.valueOf((String) listView.getItemAtPosition(i)));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //new Friend_request(editText.getText().toString()).execute();
            }
        });
        Button_Friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                if(User.friends.indexOf(Search.getText().toString()) == -1)
                {
                    AsyncTask login = (Friend_request) new Friend_request(MapsActivity.this).execute(Search.getText().toString());
                }
            }
        });
        Button_Call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+User.PhoneNumber));
                if (ActivityCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                   ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.CALL_PHONE},2);
                   return;
                }
                startActivity(callIntent);
            }
        });

    }
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.GMap = googleMap;

        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        updateLocationUI();
    }
    private void updateLocationUI() {
        if (GMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                GMap.setMyLocationEnabled(true);
                GMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                GMap.setMyLocationEnabled(false);
                GMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void getDeviceLocation()
    {
        try {
            if (locationPermissionGranted)
            {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null)
                            {
                                GMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 2));
                                new Mylocation().execute(String.valueOf(lastKnownLocation.getLatitude()), String.valueOf(lastKnownLocation.getLongitude()),String.valueOf(User_Id));
                            }
                        } else
                        {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            GMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 2));
                            GMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)
        {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

}