package co.za.foodscout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import co.za.foodscout.Adapters.RetailListAdapter;
import co.za.foodscout.Domain.Collections;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.retails.Retails;
import co.za.foodscout.Domain.Role;
import foodscout.R;;


public class RetailsActivity extends DrawerActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_retails, frameLayout);

        ProgressBar progressBar = findViewById(R.id.retailProgressBar);
        Button changeLocation = findViewById(R.id.retailChnageLocation);

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        } else {
            db.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    FirestoreUser firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                    if (firestoreUser != null){
                        if (firestoreUser.getRole().equals(Role.DRIVER)){
                            signOut();
                        }
                    }
                }
            });
        }

        String url = "https://foodbukka.herokuapp.com/api/v1/restaurant";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Retails retails = gson.fromJson(response, Retails.class);
                RecyclerView recyclerView = findViewById(R.id.retailRecycledView);
                RetailListAdapter adapter = new RetailListAdapter(RetailsActivity.this, retails.getResult());
                recyclerView.setHasFixedSize(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(RetailsActivity.this));
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
        getIntent().setAction("retail");

        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RetailsActivity.this, MapActivity.class));
            }
        });


    }

    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        if(action == null || !action.equals("retail")) {
            Intent intent = new Intent(this, RetailsActivity.class);
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