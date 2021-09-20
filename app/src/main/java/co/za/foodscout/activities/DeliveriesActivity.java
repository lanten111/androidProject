package co.za.foodscout.activities;

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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import co.za.foodscout.Adapters.DeliveryListAdapter;
import co.za.foodscout.Domain.Collections;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import foodscout.R;

public class DeliveriesActivity extends DrawerActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        TextView userDestination = findViewById(R.id.deliveriesToTxt);
        TextView orderDetail = findViewById(R.id.deliveriesOrderDetailTxt);
        TextView deliveryStatus = findViewById(R.id.deliveriesStatusTxt);
        TextView noDelivery = findViewById(R.id.NoDeliveryTxt);

        noDelivery.setVisibility(View.INVISIBLE);
        ProgressBar loginProgressBar = findViewById(R.id.DeliveriesProgressBar);
        loginProgressBar.setProgress(50);
        loginProgressBar.setVisibility(View.VISIBLE);

        getIntent().setAction("deliveries");

        db.collection(Collections.delivery.name()).whereEqualTo("delivered", false).orderBy("dateCreated").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<FirestoreDelivery> firestoreDeliveryList = new ArrayList<>();
                List<DocumentChange> documentChangeList = queryDocumentSnapshots.getDocumentChanges();
                for (DocumentChange documentChange: documentChangeList){
                    FirestoreDelivery firestoreDelivery = new FirestoreDelivery();
                    firestoreDelivery = documentChange.getDocument().toObject(FirestoreDelivery.class);
                    firestoreDelivery.setId(documentChange.getDocument().getId());
                    db.collection(Collections.delivery.name()).document(documentChange.getDocument().getId()).set(firestoreDelivery);
                    firestoreDeliveryList.add(firestoreDelivery);
                }
                if (firestoreDeliveryList.size() == 0){
                    noDelivery.setVisibility(View.VISIBLE);
                }
                RecyclerView recyclerView = findViewById(R.id.deliveriesRecycleView);
                DeliveryListAdapter adapter = new DeliveryListAdapter(DeliveriesActivity.this, firestoreDeliveryList);
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

    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        if(action == null || !action.equals("deliveries")) {
            Intent intent = new Intent(this, DeliveriesActivity.class);
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