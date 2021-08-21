package co.za.delivernow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import co.za.delivernow.Adapters.MyListAdapter;
import co.za.delivernow.Domain.DeliveryTo;
import co.za.delivernow.Domain.FirestoreUser;

public class DeliveriesActivity extends DrawerActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_deliveries, frameLayout);

        TextView fromRetail = findViewById(R.id.deliveriesFromTxt);
        TextView userDestination = findViewById(R.id.deliveriesToTxt);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        ProgressBar loginProgressBar = findViewById(R.id.DeliveriesProgressBar);
        loginProgressBar.setProgress(50);
        loginProgressBar.setVisibility(View.VISIBLE);

        db.collection("delivery").whereEqualTo("delivered", false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DeliveryTo> deliveryToList = new ArrayList<>();
                List<DocumentChange> documentChangeList = queryDocumentSnapshots.getDocumentChanges();
                for (DocumentChange documentChange: documentChangeList){
                    DeliveryTo deliveryTo = new DeliveryTo();
                    deliveryTo = documentChange.getDocument().toObject(DeliveryTo.class);
                    deliveryTo.setId(documentChange.getDocument().getId());
//                    System.out.println(deliveryTo.toString());
                    deliveryToList.add(deliveryTo);
                }
                RecyclerView recyclerView = findViewById(R.id.deliveriesRecycleView);
                MyListAdapter adapter = new MyListAdapter(DeliveriesActivity.this, deliveryToList);
                recyclerView.setHasFixedSize(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(DeliveriesActivity.this));
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(false);
                loginProgressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DeliveriesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                loginProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

}