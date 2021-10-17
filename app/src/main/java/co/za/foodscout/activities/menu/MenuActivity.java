package co.za.foodscout.activities.menu;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import co.za.foodscout.Adapters.MenuViewPagerAdapter;
import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.DeliveryTime;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Restaurant.Menu;
import co.za.foodscout.Domain.Restaurant.Restaurant;
import co.za.foodscout.Domain.matrixNew.DurationMatrix;
import co.za.foodscout.Utils.Utils;
import co.za.foodscout.activities.DrawerActivity;
import co.za.foodscout.activities.account.LoginActivity;
import foodscout.R;

public class MenuActivity extends DrawerActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();
    Restaurant restaurant = new Restaurant();
    FirestoreDelivery fireStoreDelivery = new FirestoreDelivery();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    MenuViewPagerAdapter MyAdapter;
    List< List<Menu> > menuListCatagorized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_menu, frameLayout);

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        }

        ImageView retailImage = findViewById(R.id.menuRetailImage);
        TextView retailName = findViewById(R.id.menuRetailName);
        TextView retailAddress = findViewById(R.id.menuRetailAddress);
        TextView retailDeliveryTime = findViewById(R.id.menuRetailDeliveryTime);
        RatingBar retailRating = findViewById(R.id.menuRestaurantRatingBar);
        ProgressBar progressBar = findViewById(R.id.menuProgressBar);

        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra("restaurantId");

        ViewPager2 viewpager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#f57f17")));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#f57f17"));

        firestore.collection(Collections.restaurant.name()).document(restaurantId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                restaurant = documentSnapshot.toObject(Restaurant.class);
                firestore.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                        getDeliveryTime(Utils.getLatLong(restaurant.getLocation()), Utils.getLatLong(firestoreUser.getLocation()), retailDeliveryTime, getString(R.string.google_maps_key));
                    }
                });

                storageRef.child(restaurant.getMainImageId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(retailImage);
                    }
                });
                retailName.setText(restaurant.getName());
                retailAddress.setText(restaurant.getAddress());
                retailRating.setRating(restaurant.getRating());

                menuListCatagorized = new ArrayList<>( restaurant.getMenu().stream().collect(Collectors.groupingBy(p -> p.getMenuCatagories().name()) ).values() );

                MyAdapter = new MenuViewPagerAdapter(MenuActivity.this, menuListCatagorized, storageRef);
                viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                viewpager.setAdapter(MyAdapter);
                new TabLayoutMediator(tabLayout, viewpager,
                        (tab, position) -> {
                            switch (position) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                    tab.setText(menuListCatagorized.get(position).get(0).getMenuCatagories().name());
                                    break;
                                default:
                                    break;
                            }
                        }).attach();

                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private String getRetailName(GeoPoint geoPoint) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
        }catch (IOException exception){
            Toast.makeText(this, "Could not get you address", Toast.LENGTH_SHORT).show();
        }
        return addresses.get(0).getFeatureName();
    }

    private void getDeliveryTime(LatLng origin, LatLng dest, TextView retailDeliveryTime, String gKey) {

        DeliveryTime deliveryTime = new DeliveryTime();
        String str_origin = "origins=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destinations=" + dest.latitude + "," + dest.longitude;
        String key = "key=" + gKey;
        String parameters = str_origin + "&" + str_dest+ "&" + key;
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?" + parameters;

        RequestQueue queue = Volley.newRequestQueue(MenuActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        DurationMatrix durationMatrix = gson.fromJson(response, DurationMatrix.class);
                        deliveryTime.setDistance(durationMatrix.getRows().get(0).getElements().get(0).getDistance().getText());
                        deliveryTime.setETA(durationMatrix.getRows().get(0).getElements().get(0).getDuration().getText());
                        retailDeliveryTime.setText(" Estimated Delivery Time: "+ deliveryTime.getETA() + " " + deliveryTime.getDistance()+ " away");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }


}