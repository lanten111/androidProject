package co.za.foodscout.activities.order;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import co.za.foodscout.Adapters.OrderViewAdapter;
import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.FireStoreCart;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Restaurant.Restaurant;
import co.za.foodscout.activities.DrawerActivity;
import co.za.foodscout.activities.retail.RetailsActivity;
import co.za.foodscout.activities.seller.SellerViewActivity;
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

        getIntent().setAction("cart");

        CircularProgressIndicator circularProgressBar = findViewById(R.id.loadingBar);
        ConstraintLayout constraintLayout = findViewById(R.id.secondayLayout);
        constraintLayout.setVisibility(View.INVISIBLE);

        Button checkOutButton = findViewById(R.id.checkOutButton);

        firestore.collection(Collections.cart.name()).whereEqualTo("complete", false).whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                circularProgressBar.setVisibility(View.INVISIBLE);
                constraintLayout.setVisibility(View.VISIBLE);

                fireStoreCartList = queryDocumentSnapshots.toObjects(FireStoreCart.class);
                if (fireStoreCartList.size() <= 0){
                    startActivity(new Intent(getApplicationContext(), RetailsActivity.class));
                }
                RecyclerView recyclerView = findViewById(R.id.orderSummaryRecycleView);
                OrderViewAdapter adapter = new OrderViewAdapter(CartViewActivity.this, fireStoreCartList, firestore, getWindow(), circularProgressBar);
                recyclerView.setHasFixedSize(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(CartViewActivity.this));
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(false);
            }
        });

        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOutButton.setEnabled(false);
                startActivity(new Intent(CartViewActivity.this, CheckOutActivity.class));
                checkOutButton.setEnabled(true);
            }
        });
    }

    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        if(action == null || !action.equals("cart")) {
            Intent intent = new Intent(this, CartViewActivity.class);
            startActivity(intent);
            finish();
        }
        else
            getIntent().setAction(null);
        super.onResume();
    }
}