package co.za.foodscout.activities.delivery;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.DeliveryTime;
import co.za.foodscout.Domain.Enum.DeliveryStatus;
import co.za.foodscout.Domain.FireStoreOrders;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.matrixNew.DurationMatrix;
import co.za.foodscout.Utils.DirectionsJSONParser;
import co.za.foodscout.Utils.Utils;
import co.za.foodscout.activities.DrawerActivity;
import co.za.foodscout.activities.account.LoginActivity;
import foodscout.R;

public class DeliveryDetailsActivity extends DrawerActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirestoreDelivery firestoreDelivery = new FirestoreDelivery();
    private Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;
    String currentDeliveryId;
    LatLng mOrigin;
    LatLng mDestination;
    TextView fromHeader;
    Location currentLocation;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirestoreUser firestoreUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_deilverydetails, frameLayout);

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ProgressBar progressBar = findViewById(R.id.DeliveriesDProgressBar);

        TextView destination = findViewById(R.id.driverDeliveryDestination);
        TextView fromRetail = findViewById(R.id.deliveryFromTxt);
        TextView destinationHeader = findViewById(R.id.destinationHeaderTxt);
        fromHeader = findViewById(R.id.fromHeaderTxt);
        Button openMap = findViewById(R.id.openMapsBtn);
        TextView userDetails = findViewById(R.id.driverDeliveryUserDetails);
        TextView retailName = findViewById(R.id.deliveryFromDetails);
        TextView orderNumber = findViewById(R.id.deliveryOrderNumber);
        Button delivered = findViewById(R.id.deliveryCompleteBtn);
        Button pickUpDelivery = findViewById(R.id.deliveryPickUpBtn);

//        toCardView.setVisibility(View.INVISIBLE);
//        delivered.setVisibility(View.INVISIBLE);
        getIntent().setAction("deliveriesDetails");

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.driverDeliveryMap);
        mapFragment.getMapAsync(this);

        mMarkerPoints = new ArrayList<>();

        String deliveryId = getIntent().getStringExtra("deliveryId");
        firestore.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                setNewDelivery(deliveryId, destination, fromRetail, userDetails, retailName, pickUpDelivery, delivered, orderNumber,  progressBar);
            }
        });

//        open direction in google maps
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mDestination.latitude + "," + mDestination.longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

//        Set order to Delivered
        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection(Collections.delivery.name()).document(currentDeliveryId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        firestoreDelivery = documentSnapshot.toObject(FirestoreDelivery.class);
                        firestoreDelivery.setDateDelivered(Timestamp.now());
                        firestoreDelivery.setDelivered(true);
                        firestoreDelivery.setDateUpdated(Timestamp.now());
                        firestoreDelivery.setDeliveryStatus(DeliveryStatus.Completed);
                        firestore.collection(Collections.delivery.name()).document(currentDeliveryId).set(firestoreDelivery);
                        firestore.collection(Collections.order.name()).whereEqualTo("orderId", firestoreDelivery.getOrderId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                FireStoreOrders fireStoreOrders = queryDocumentSnapshots.toObjects(FireStoreOrders.class).get(0);
                                fireStoreOrders.setComplete(true);
                                fireStoreOrders.setDateUpdated(Timestamp.now());
                                firestore.collection(Collections.order.name()).document(fireStoreOrders.getId()).set(fireStoreOrders);
                            }
                        });
                        Toast.makeText(DeliveryDetailsActivity.this, "Delivery Completed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DeliveryDetailsActivity.this, DeliveriesActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });

        pickUpDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickUpDelivery.setVisibility(View.INVISIBLE);
                firestore.collection(Collections.delivery.name()).document(currentDeliveryId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        firestoreDelivery = documentSnapshot.toObject(FirestoreDelivery.class);
                        firestoreDelivery.setDeliveryPicked(true);
                        firestoreDelivery.setDeliveryStatus(DeliveryStatus.OnRoute);
                        firestoreDelivery.setDeliveryStatus(DeliveryStatus.OnRoute);
                        firestoreDelivery.setAssigned(true);
                        firestoreDelivery.setAssigneeId(firestoreUser.getId());
                        firestoreDelivery.setDriverName(firestoreUser.getName());
                        mDestination = Utils.getLatLong(firestoreDelivery.getUserLocation());
                        mOrigin = Utils.getLatLong(firestoreDelivery.getRetailLocation());
                        firestore.collection(Collections.delivery.name()).document(firestoreDelivery.getId()).set(firestoreDelivery).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                delivered.setVisibility(View.VISIBLE);
                            }
                        });
                        setDirection();
                    }
                });
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

    void setDirection() {

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
        try {
            getDeliveryTime(mOrigin, mDestination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setNewDelivery(String deliveryId, TextView destination, TextView fromRetail, TextView userDetails, TextView retailName, Button pickUpDelivery,Button delivered , TextView orderNumber, ProgressBar progressBar) {
        firestore.collection(Collections.delivery.name()).whereEqualTo("id", deliveryId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<FirestoreDelivery> firestoreDeliveryList = queryDocumentSnapshots.toObjects(FirestoreDelivery.class);
                firestoreDelivery = firestoreDeliveryList.get(0);
                currentDeliveryId = firestoreDelivery.getId();
                destination.setText(Utils.getAddress(firestoreDelivery.getUserLocation(), DeliveryDetailsActivity.this));
                fromRetail.setText(Utils.getAddress(firestoreDelivery.getRetailLocation(), DeliveryDetailsActivity.this));
                userDetails.setText("Order for " + firestoreDelivery.getUserNames() + "  Contact No: " + firestoreDelivery.getContactNo());
                retailName.setText(" Pick up from: "+firestoreDelivery.getRetailName());
                orderNumber.setText("Order#"+firestoreDelivery.getOrderNumber());
                if (firestoreDelivery.isDeliveryPicked()) {
                    delivered.setVisibility(View.VISIBLE);
                    pickUpDelivery.setVisibility(View.INVISIBLE);
                    mDestination = Utils.getLatLong(firestoreDelivery.getUserLocation());
                    mOrigin = Utils.getLatLong(firestoreDelivery.getRetailLocation());
                    setDirection();
                } else {
                    delivered.setVisibility(View.INVISIBLE);
                    pickUpDelivery.setVisibility(View.VISIBLE);
                    mDestination = Utils.getLatLong(firestoreDelivery.getRetailLocation());
                    getCurrentLocation();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DeliveryDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Utils.zoomInTwoPoints(mMap, mOrigin, mDestination);
        return true;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
    private void getDeliveryTime(LatLng origin, LatLng dest) throws IOException {

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
                        fromHeader.setText("Estimated time " + deliveryTime.getETA() + ", Distance " + deliveryTime.getDistance());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
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

    void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    //set different driver location
                    location.setLatitude(-26.362981804140688);
                    location.setLongitude(28.222809041511223);
                    currentLocation = location;
                    mOrigin = Utils.getLatLong(currentLocation);
                    setDirection();
                } else {
//                    getCurrentLocation1();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println();
            }
        });
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
        if(action == null || !action.equals("deliveriesDetails")) {
            Intent intent = new Intent(this, DeliveryDetailsActivity.class);
            startActivity(intent);
            finish();
        }
        else
            getIntent().setAction(null);
        super.onResume();
    }


}