package co.za.foodscout.activities.delivery;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import co.za.foodscout.Adapters.DeliveryListAdapter;
import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.activities.DrawerActivity;
import co.za.foodscout.activities.account.LoginActivity;
import foodscout.R;

public class DeliveriesActivity extends DrawerActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_deliveries, frameLayout);

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        }

        TextView fromRetail = findViewById(R.id.deliveriesFromTxt);
        TextView userDestination = findViewById(R.id.SellerDeliveriesTo);
        TextView orderDetail = findViewById(R.id.deliveriesOrderDetailTxt);
        TextView deliveryStatus = findViewById(R.id.deliveriesStatusTxt);
        TextView noDelivery = findViewById(R.id.NoDeliveryTxt);

        noDelivery.setVisibility(View.INVISIBLE);
        ProgressBar loginProgressBar = findViewById(R.id.DeliveriesProgressBar);
        loginProgressBar.setProgress(50);
        loginProgressBar.setVisibility(View.VISIBLE);

        firestore.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                firestore.collection(Collections.delivery.name()).whereEqualTo("delivered", false).orderBy("dateCreated").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<FirestoreDelivery> firestoreDeliveryList = queryDocumentSnapshots.toObjects(FirestoreDelivery.class);
                        if (firestoreDeliveryList.size() == 0){
                            noDelivery.setVisibility(View.VISIBLE);
                        }
                        RecyclerView recyclerView = findViewById(R.id.deliveriesRecycleView);
                        DeliveryListAdapter adapter = new DeliveryListAdapter(DeliveriesActivity.this, firestoreDeliveryList, firestore, firestoreUser);
                        recyclerView.setHasFixedSize(false);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DeliveriesActivity.this));
                        recyclerView.setAdapter(adapter);
                        recyclerView.setHasFixedSize(false);
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e.getMessage());
                        Toast.makeText(DeliveriesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });


    }

//    @Override
//    protected void onResume() {
//        String action = getIntent().getAction();
//        if(action == null || !action.equals("deliveries")) {
//            Intent intent = new Intent(this, DeliveriesActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        else
//            getIntent().setAction(null);
//        super.onResume();
//    }

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