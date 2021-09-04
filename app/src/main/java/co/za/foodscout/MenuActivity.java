package co.za.foodscout;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import co.za.foodscout.Adapters.MenuItemAdapter;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.menu.Menu;
import co.za.foodscout.Domain.retail.Retail;
import co.za.foodscout.Domain.retails.RetailDetails;
import foodscout.R;

public class MenuActivity extends DrawerActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();
    FirestoreDelivery fireStoreDelivery = new FirestoreDelivery();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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
        TextView retailRating = findViewById(R.id.menuRetailRating);
        ProgressBar progressBar = findViewById(R.id.menuProgressBar);
        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra("retailId");

        String url = "https://foodbukka.herokuapp.com/api/v1/restaurant/"+restaurantId;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Retail retail = gson.fromJson(response, Retail.class);
                RetailDetails retailDetails= retail.getData();
                Picasso.get().load(Uri.parse(retailDetails.getImage())).into(retailImage);
                retailName.setText(retailDetails.getBusinessname());
                retailAddress.setText(retailDetails.getAddress());
                retailDeliveryTime.setText("30Mins 3KM away");
                retailRating.setText(String.valueOf(retailDetails.getReviews()));

                String url = "https://foodbukka.herokuapp.com/api/v1/menu";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                         Menu menu = gson.fromJson(response, Menu.class);
                        RecyclerView recyclerView = findViewById(R.id.menuRecycledView);
                        MenuItemAdapter adapter = new MenuItemAdapter(MenuActivity.this, menu.getResult());
                        recyclerView.setHasFixedSize(false);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
                        recyclerView.setAdapter(adapter);
                        recyclerView.setHasFixedSize(false);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(stringRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);



//        addToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DocumentReference documentReference = db.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid());
//                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
//                        fireStoreDelivery.setUserDestination(firestoreUser.getLocation());
//                        fireStoreDelivery.setAssigned(false);
//                        fireStoreDelivery.setUserId(documentReference.getId());
//                        fireStoreDelivery.setRetailLocation(new GeoPoint(-26.344080728294937, 28.216650379414723));
//                        fireStoreDelivery.setRetailAddress(getAddress(fireStoreDelivery.getRetailLocation()));
//                        fireStoreDelivery.setUserAddress(getAddress(firestoreUser.getLocation()));
//                        fireStoreDelivery.setDelivered(false);
//                        fireStoreDelivery.setContactNo(firebaseAuth.getCurrentUser().getPhoneNumber());
//                        fireStoreDelivery.setUserNames(firestoreUser.getName()+ " "+firestoreUser.getSurname());
//                        fireStoreDelivery.setRetailName(getRetailName(fireStoreDelivery.getRetailLocation()));
//                        fireStoreDelivery.setDeliveryStatus("Pending Delivery");
//                        fireStoreDelivery.setDelivered(false);
//                        fireStoreDelivery.setDateCreated(Timestamp.now());
//                        fireStoreDelivery.setDateUpdated(Timestamp.now());
//                        fireStoreDelivery.setDeliveryPicked(false);
//                        db.collection(Collections.delivery.name()).document().set(fireStoreDelivery).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(MenuActivity.this, "added to order", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(MenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        Intent orderIntent = new Intent(MenuActivity.this, OrderViewActivity.class);
//                        startActivity(orderIntent);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        
    }


    private String getAddress(GeoPoint geoPoint) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
        }catch (IOException exception){
            Toast.makeText(this, "Could not get you address", Toast.LENGTH_SHORT).show();
        }
        return addresses.get(0).getAddressLine(0);
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
}