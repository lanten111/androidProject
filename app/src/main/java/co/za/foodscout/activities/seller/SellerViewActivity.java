package co.za.foodscout.activities.seller;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
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
import co.za.foodscout.activities.account.LoginActivity;
import co.za.foodscout.activities.delivery.DeliveryDetailsActivity;
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

        getIntent().setAction("seller");

        RecyclerView recyclerView = findViewById(R.id.selleerViewRecycledView);
        CircularProgressIndicator circularProgressBar = findViewById(R.id.loadingBar);
        TextView noData = findViewById(R.id.noDeliveryTxt);
        recyclerView.setVisibility(View.INVISIBLE);

        checkIfUserLoggedIn();
        noData.setVisibility(View.INVISIBLE);
        String id = firebaseAuth.getCurrentUser().getUid();
        firestore.collection(Collections.order.name()).whereEqualTo("retailId", id).whereEqualTo("orderReady", false).whereEqualTo("complete", false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                noData.setVisibility(View.INVISIBLE);
                List<FireStoreOrders> fireStoreOrdersList = value.toObjects(FireStoreOrders.class);
                if (fireStoreOrdersList.size() == 0){
                    circularProgressBar.setVisibility(View.INVISIBLE);
                    noData.setVisibility(View.VISIBLE);
                    return;
                }
                SellerViewListAdapter adapter = new SellerViewListAdapter(SellerViewActivity.this, fireStoreOrdersList, firestore, circularProgressBar, recyclerView);
                recyclerView.setHasFixedSize(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(SellerViewActivity.this));
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(false);
            }
        });
    }

    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        if(action == null || !action.equals("seller")) {
            Intent intent = new Intent(this, SellerViewActivity.class);
            startActivity(intent);
            finish();
        }
        else
            getIntent().setAction(null);
        super.onResume();
    }
}