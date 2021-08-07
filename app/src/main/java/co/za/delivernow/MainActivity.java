package co.za.delivernow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends DrawerActivity{

    Button firstButtonSignIn;
    Button permissionButton;
    Toast LocationDeniedToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 13);
        firstButtonSignIn = findViewById(R.id.button);
        permissionButton = findViewById(R.id.permissionButton);
        permissionButton.setVisibility(View.GONE);
        firstButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (firebaseUser == null){
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                }
                startActivity(intent);
            }
        });

        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 13);
            }
        });
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
//            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LocationDeniedToast = Toast.makeText(MainActivity.this, "Location Permission Denied, please allow location permission in order to use the app", Toast.LENGTH_LONG);
        if (requestCode == 13) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                firstButtonSignIn.setVisibility(View.VISIBLE);
                permissionButton.setVisibility(View.GONE);
                LocationDeniedToast.cancel();
                Toast.makeText(MainActivity.this, "Location Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                firstButtonSignIn.setVisibility(View.GONE);
                permissionButton.setVisibility(View.VISIBLE);
                LocationDeniedToast.show();
            }
        }
    }

    @Override
    public void onBackPressed() { }
}