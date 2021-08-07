package co.za.delivernow;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    LocationManager locationManager;
    private FirebaseAuth firebaseAuth;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Button useLocationButton = findViewById(R.id.useLocation);
        TextView addressBar = findViewById(R.id.adressOnLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        if (addressBar != null){
            useLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), MenuActivity.class);
                    intent.putExtra("location", location);
                    startActivity(intent);
                }
            });
        }else {
            Toast.makeText(this, "choose delivery adress first", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        enableMyLocation();
        Location location = getCurrentLocation();
//        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        String addressLine = getAddressLine(location);
        TextView addressOnLocation = findViewById(R.id.adressOnLocation);

        addressOnLocation.setText(addressLine);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                onMarkerMove(mMap.getCameraPosition().target, addressOnLocation);
            }
        });
    }

    private String getAddressLine(Location location){
        return getAddressFromLocation(location).get(0).getAddressLine(0);
    }

    private String getAddressLine(LatLng latLng){
        return getAddressFromLocation(latLng).get(0).getAddressLine(0);
    }

    private void onMarkerMove(LatLng latLng, TextView addressBar){
        List<Address> addresses = getAddressFromLocation(latLng);
        if (addresses.size() > 0){
            addressBar.setText(addresses.get(0).getAddressLine(0));
            location = addresses.get(0).getAddressLine(0);
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            ZoomInLocation();
        } else {
            Toast.makeText(this, "Please allow location permission", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        ZoomInLocation();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Please allow location permission", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void ZoomInLocation(){
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (isGPSEnabled){
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20.0f));
            } else {
                //ask to enable gps
            }
        } else {
            Toast.makeText(this, "Please allow location permission", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private Location getCurrentLocation(){
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Location location = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (isGPSEnabled){
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else {
                //ask to enable gps
            }
        } else {
            Toast.makeText(this, "Please allow location permission", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return location;
    }

    private List<Address> getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        }catch (IOException exception){
            Toast.makeText(this, "Could not get you address", Toast.LENGTH_LONG).show();
        }
        return addresses;
    }

    private List<Address> getAddressFromLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        }catch (IOException exception){
            Toast.makeText(this, "Could not get you address", Toast.LENGTH_LONG).show();
        }
        return addresses;
    }
}