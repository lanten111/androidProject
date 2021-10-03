package co.za.foodscout.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import co.za.foodscout.Adapters.OrderViewAdapter;
import co.za.foodscout.Domain.Collections;
import co.za.foodscout.Domain.FireStoreCart;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Restaurant.Restaurant;
import foodscout.R;

public class CartViewActivity extends DrawerActivity {


    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();
    Restaurant restaurant = new Restaurant();
    FirestoreDelivery fireStoreDelivery = new FirestoreDelivery();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    List<FireStoreCart> fireStoreCartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_cart_view, frameLayout);

        ProgressBar progressBar = findViewById(R.id.CartViewProgressBar);
        Button checkOutButton = findViewById(R.id.checkOutButton);

        progressBar.setVisibility(View.VISIBLE);
        firestore.collection(Collections.cart.name()).whereEqualTo("complete", false).whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                fireStoreCartList = queryDocumentSnapshots.toObjects(FireStoreCart.class);
                RecyclerView recyclerView = findViewById(R.id.orderSummaryRecycleView);
                OrderViewAdapter adapter = new OrderViewAdapter(CartViewActivity.this, fireStoreCartList, firestore);
                recyclerView.setHasFixedSize(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(CartViewActivity.this));
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(false);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartViewActivity.this, CheckOutActivity.class));
            }
        });
    }
}