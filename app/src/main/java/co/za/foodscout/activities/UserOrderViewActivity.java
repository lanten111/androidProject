package co.za.foodscout.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.za.foodscout.Domain.Collections;
import co.za.foodscout.Domain.DeliveryTime;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.matrixNew.DurationMatrix;
import co.za.foodscout.Utils.DirectionsJSONParser;
import co.za.foodscout.Utils.Utils;
import foodscout.R;

public class UserOrderViewActivity extends DrawerActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreDelivery firestoreDelivery = null;
    LatLng mOrigin;
    LatLng mDestination;
    Polyline mPolyline;
    TextView orderHead;
    ArrayList<LatLng> mMarkerPoints;
    String documentId;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_user_order_view, frameLayout);

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        }

        ProgressBar progressBar = findViewById(R.id.orderProgressBar);
        orderHead = findViewById(R.id.orderHeadTxt);
        TextView orderName = findViewById(R.id.orderNameTxt);
        TextView orderFrom = findViewById(R.id.orderFromTxt);
        TextView orderTo = findViewById(R.id.orderToTxt);

        getIntent().setAction("orderView");

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.OrderDeliveryMap);
        mapFragment.getMapAsync(this);

        progressBar.setVisibility(View.INVISIBLE);

        db.collection(Collections.delivery.name()).whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("isDelivered", false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null){
                    List<FirestoreDelivery> firestoreDeliveryList = value.toObjects(FirestoreDelivery.class);
                    if (firestoreDeliveryList.size() > 0){
                        firestoreDelivery = firestoreDeliveryList.get(0);
                        documentId = value.getDocuments().get(0).getId();
                        mOrigin = Utils.getLatLong(firestoreDelivery.getRetailLocation());
                        mDestination = Utils.getLatLong(firestoreDelivery.getUserLocation());
                        orderFrom.setText("From: " + Utils.getAddress(firestoreDelivery.getRetailLocation(), UserOrderViewActivity.this));
                        orderTo.setText("TO: " + Utils.getAddress(firestoreDelivery.getUserLocation(), UserOrderViewActivity.this));
                        if (firestoreDelivery.isAssigned() &&  firestoreDelivery.isDeliveryPicked()){
                            getDeliveryTime(mOrigin, mDestination);
                        } else {
                            orderHead.setText("Order being prepared");
//                        getDeliveryTime(mOrigin, mDestination);
                        }
                        setDirection();
                    } else {
//                        Toast.makeText(UserOrderViewActivity.this, "Order completed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserOrderViewActivity.this, RetailsActivity.class));
                    }
                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    private void getDeliveryTime(LatLng origin, LatLng dest) {

        DeliveryTime deliveryTime = new DeliveryTime();
        String str_origin = "origins=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destinations=" + dest.latitude + "," + dest.longitude;
        String key = "key=" + getString(R.string.google_maps_key);
        String parameters = str_origin + "&" + str_dest + "&" + key;
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?" + parameters;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        DurationMatrix durationMatrix = gson.fromJson(response, DurationMatrix.class);
                        deliveryTime.setDistance(durationMatrix.getRows().get(0).getElements().get(0).getDistance().getText());
                        deliveryTime.setETA(durationMatrix.getRows().get(0).getElements().get(0).getDuration().getText());
                        orderHead.setText("Order Incoming - ETA " + deliveryTime.getETA() + " Distance " + deliveryTime.getDistance());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }

    void setDirection() {
        db.collection("delivery").document(documentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        firestoreDelivery = documentSnapshot.toObject(FirestoreDelivery.class);
                        mMarkerPoints = new ArrayList<>();
                        mMap.clear();
                        mMarkerPoints.add(mOrigin);
                        mMarkerPoints.add(mDestination);
                        for (int i = 0; i < mMarkerPoints.size(); i++) {
                            LatLng markerPoint = mMarkerPoints.get(i);
                            MarkerOptions options = new MarkerOptions();
                            options.position(markerPoint);
                            if (i == 0) {
                                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            } else if (i == 1) {
                                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            }
                            mMap.addMarker(options);
                        }
                        // Checks, whether start and end locations are captured
                        if (mMarkerPoints.size() >= 2) {
                            mOrigin = mMarkerPoints.get(0);
                            mDestination = mMarkerPoints.get(1);
                            drawRoute(getDirectionsUrl(mOrigin, mDestination));
                        }
                        Utils.zoomInTwoPoints(mMap, mOrigin, mDestination);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserOrderViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void drawRoute(String strUrl) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jObject;
                        ArrayList<LatLng> points = null;
                        PolylineOptions lineOptions = null;

                        List<List<HashMap<String, String>>> routes = null;

                        try {
                            jObject = new JSONObject(response);
                            DirectionsJSONParser parser = new DirectionsJSONParser();
                            routes = parser.parse(jObject);
                            for (int i = 0; i < routes.size(); i++) {
                                points = new ArrayList<LatLng>();
                                lineOptions = new PolylineOptions();

                                // Fetching i-th route
                                List<HashMap<String, String>> path = routes.get(i);

                                // Fetching all the points in i-th route
                                for (int j = 0; j < path.size(); j++) {
                                    HashMap<String, String> point = path.get(j);

                                    double lat = Double.parseDouble(point.get("lat"));
                                    double lng = Double.parseDouble(point.get("lng"));
                                    LatLng position = new LatLng(lat, lng);

                                    points.add(position);
                                }
                                // Adding all the points in the route to LineOptions
                                lineOptions.addAll(points);
                                lineOptions.width(8);
                                lineOptions.color(Color.RED);
                            }

                            // Drawing polyline in the Google Map for the i-th route
                            if (lineOptions != null) {
                                if (mPolyline != null) {
                                    mPolyline.remove();
                                }
                                mPolyline = mMap.addPolyline(lineOptions);

                            } else
                                Toast.makeText(getApplicationContext(), "No route is found", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String key = "key=" + getString(R.string.google_maps_key);
        String parameters = str_origin + "&" + str_dest + "&" + key;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
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