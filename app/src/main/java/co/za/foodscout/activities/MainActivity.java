package co.za.foodscout.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import co.za.foodscout.Domain.Collections;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Role;
import foodscout.R;

public class MainActivity extends DrawerActivity{

    Button firstButtonSignIn;
    Button permissionButton;
    Toast LocationDeniedToast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private long pressedTime;
    FirestoreUser firestoreUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

//        if (firebaseAuth.getCurrentUser() == null){
//            startActivity(new Intent(this, LoginActivity.class));
//        }

        ProgressBar loginProgressBar = findViewById(R.id.MainProgressBar);
        TextView welcomeText = findViewById(R.id.welcomeTextView);
        loginProgressBar.setProgress(50);
        loginProgressBar.setVisibility(View.INVISIBLE);

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        getIntent().setAction("main");

        firstButtonSignIn = findViewById(R.id.Mainbutton);
        permissionButton = findViewById(R.id.permissionButton);
        permissionButton.setVisibility(View.INVISIBLE);
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 13);

        if (firebaseAuth.getCurrentUser() != null){
            db.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                    if ( firestoreUser != null){
                        if (firestoreUser.getName() != null || !firestoreUser.getName().trim().equals("")){
                            welcomeText.setText("Welcome back "+firestoreUser.getName());
//                        firstButtonSignIn.setText("");
                        } else {
                            welcomeText.setText("Welcome back");
                        }
                    }else {
                        welcomeText.setText("Welcome back");
                    }
                }
            });
        }


        firstButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgressBar.setVisibility(View.VISIBLE);
                if (firebaseAuth.getCurrentUser() == null){
                   Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                   startActivity(intent);
                } else {
                    if (firestoreUser.getRole().equals(Role.DRIVER)){
                        Intent intent = new Intent(getApplicationContext(), DeliveriesActivity.class);
                        startActivity(intent);
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    } else{
                        //check of there order peindng
                        db.collection(Collections.delivery.name()).whereEqualTo("userId", firestoreUser.getId()).whereEqualTo("delivered", false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<FirestoreDelivery> firestoreDeliveryList = new ArrayList<>();
                                firestoreDeliveryList = queryDocumentSnapshots.toObjects(FirestoreDelivery.class);
                                if (firestoreDeliveryList.size() > 0){
                                    Toast.makeText(MainActivity.this, "View pending Order", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, UserOrderViewActivity.class));
                                } else {
                                    if (firestoreUser.getLocation() !=null){
                                        startActivity(new Intent(getApplicationContext(), RetailsActivity.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please choose location for delivery", Toast.LENGTH_SHORT).show();
                                        startActivity( new Intent(getApplicationContext(), MapActivity.class));
                                    }
                                }
                            }
                        });
                    }
                    loginProgressBar.setVisibility(View.INVISIBLE);

                }
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
                permissionButton.setVisibility(View.INVISIBLE);
                LocationDeniedToast.cancel();
                Toast.makeText(MainActivity.this, "Location Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                firstButtonSignIn.setVisibility(View.INVISIBLE);
                permissionButton.setVisibility(View.VISIBLE);
                LocationDeniedToast.show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        if(action == null || !action.equals("main")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
            getIntent().setAction(null);
        super.onResume();
    }
}