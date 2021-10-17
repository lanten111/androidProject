package co.za.foodscout.activities.retail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import co.za.foodscout.Adapters.RetailListAdapter;
import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Restaurant.Restaurant;
import co.za.foodscout.activities.DrawerActivity;
import co.za.foodscout.activities.MapActivity;
import co.za.foodscout.activities.account.LoginActivity;
import foodscout.R;;


public class RetailsActivity extends DrawerActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    FirestoreUser firestoreUser = new FirestoreUser();
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_retails, frameLayout);

        getIntent().setAction("retail");
        ProgressBar progressBar = findViewById(R.id.retailProgressBar);
        Button changeLocation = findViewById(R.id.retailChnageLocation);

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        } else {
            firestore.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                    firestore.collection(Collections.restaurant.toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Restaurant> restaurant =  queryDocumentSnapshots.toObjects(Restaurant.class);
                            RecyclerView recyclerView = findViewById(R.id.retailRecycledView);
                            RetailListAdapter adapter = new RetailListAdapter(RetailsActivity.this, restaurant,storageRef, firestoreUser, getString(R.string.google_maps_key));
                            recyclerView.setHasFixedSize(false);
                            recyclerView.setLayoutManager(new LinearLayoutManager(RetailsActivity.this));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(false);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
//                    if (firestoreUser != null){
//                        if (firestoreUser.getRole().equals(Role.DRIVER)){
//                            signOut();
//                        }
//                    }
                }
            });
        }

        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RetailsActivity.this, MapActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        if(action == null || !action.equals("retail")) {
            Intent intent = new Intent(this, RetailsActivity.class);
            startActivity(intent);
            finish();
        }
        else
            getIntent().setAction(null);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (firebaseAuth.getCurrentUser() != null){
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        }
    }
}