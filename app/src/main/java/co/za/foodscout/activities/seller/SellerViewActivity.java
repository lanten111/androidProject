package co.za.foodscout.activities.seller;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import co.za.foodscout.Adapters.SellerViewListAdapter;
import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.FireStoreOrders;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.activities.DrawerActivity;
import foodscout.R;

public class SellerViewActivity extends DrawerActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_seller_view, frameLayout);

        ProgressBar progressBar = findViewById(R.id.SellerProgressBar);

        firestore.collection(Collections.order.name()).whereEqualTo("retailId", firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                progressBar.setVisibility(View.VISIBLE);
                List<FireStoreOrders> fireStoreOrdersList = value.toObjects(FireStoreOrders.class);
                RecyclerView recyclerView = findViewById(R.id.selleerViewRecycledView);
                SellerViewListAdapter adapter = new SellerViewListAdapter(SellerViewActivity.this, fireStoreOrdersList, firestore);
                recyclerView.setHasFixedSize(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(SellerViewActivity.this));
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(false);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}