package co.za.delivernow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.za.delivernow.Adapters.MyListAdapter;
import co.za.delivernow.Domain.DeliveryTo;

public class DeliveriesActivity extends DrawerActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_deliveries, frameLayout);

        TextView fromRetail = findViewById(R.id.deliveriesFromTxt);
        TextView userDestination = findViewById(R.id.deliveriesToTxt);

        db.collection("delivery").whereEqualTo("delivered", false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DeliveryTo> deliveryToList = new ArrayList<>();
                deliveryToList = queryDocumentSnapshots.toObjects(DeliveryTo.class);
                RecyclerView recyclerView = findViewById(R.id.deliveriesRecycleView);
                MyListAdapter adapter = new MyListAdapter(DeliveriesActivity.this, deliveryToList);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        new LinearLayoutManager(DeliveriesActivity.this).getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(DeliveriesActivity.this));
                recyclerView.setAdapter(adapter);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DeliveriesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}