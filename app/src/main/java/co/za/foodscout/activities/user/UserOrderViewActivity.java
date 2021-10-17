package co.za.foodscout.activities.user;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import co.za.foodscout.Adapters.UserOrderViewPagerAdapter;
import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.activities.DrawerActivity;
import co.za.foodscout.activities.account.LoginActivity;
import co.za.foodscout.activities.retail.RetailsActivity;
import foodscout.R;

public class UserOrderViewActivity extends DrawerActivity {

    private GoogleMap mMap;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirestoreDelivery firestoreDelivery = null;
    LatLng mOrigin;
    LatLng mDestination;
    Polyline mPolyline;
    TextView orderHead;
    ArrayList<LatLng> mMarkerPoints;
    String documentId;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    UserOrderViewPagerAdapter MyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_user_order_view, frameLayout);

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        }

        ProgressBar progressBar = findViewById(R.id.userOrderProgressBar);

        ViewPager2 viewpager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        getIntent().setAction("orderView");

//        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
//                .findFragmentById(R.id.OrderDeliveryMap);


//        progressBar.setVisibility(View.INVISIBLE);
        firestore.collection(Collections.delivery.name()).whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("delivered", false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null){
                    List<FirestoreDelivery> firestoreDeliveryList = value.toObjects(FirestoreDelivery.class);
                    if (firestoreDeliveryList.size() > 0){
                        MyAdapter = new UserOrderViewPagerAdapter(UserOrderViewActivity.this, firestoreDeliveryList, firestore, progressBar);
                        viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                        viewpager.setAdapter(MyAdapter);
                        new TabLayoutMediator(tabLayout, viewpager,
                                (tab, position) -> {
                                    switch (position) {
                                        case 0:
                                        case 1:
                                        case 2:
                                        case 3:
                                            tab.setText("Order#"+firestoreDeliveryList.get(position).getOrderNumber());
                                            break;
                                        default:
                                            break;
                                    }
                                }).attach();
                    } else {
                        Toast.makeText(UserOrderViewActivity.this, "No Pending order to view", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserOrderViewActivity.this, RetailsActivity.class));
                    }
                }
            }
        });
    }



    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        if(action == null || !action.equals("orderView")) {
            Intent intent = new Intent(this, UserOrderViewActivity.class);
            startActivity(intent);
            finish();
        }
        else
            getIntent().setAction(null);
        super.onResume();
    }


}