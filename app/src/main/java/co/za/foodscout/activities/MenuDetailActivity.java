package co.za.foodscout.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import co.za.foodscout.Domain.Collections;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Restaurant.Menu;
import co.za.foodscout.Domain.Restaurant.Restaurant;
import foodscout.R;

public class MenuDetailActivity extends DrawerActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();
    Restaurant restaurant = new Restaurant();
    FirestoreDelivery fireStoreDelivery = new FirestoreDelivery();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_menu_details, frameLayout);

        Intent intent = getIntent();
//        String menuItermId = intent.getStringExtra("menuItermId");
        ProgressBar progressBar = findViewById(R.id.menuProgressBar);

        TextView menuItemName = findViewById(R.id.menuItemtName);
        TextView menuItemDesc = findViewById(R.id.menuItemtDesc);
        ImageView imageView = findViewById(R.id.menuDetailsImage);
        FloatingActionButton addToCart = findViewById(R.id.addToCartButton);
        LinearLayout layout = findViewById(R.id.RadioGroulinearLayout);

        db.collection(Collections.restaurant.name()).document(intent.getStringExtra("restaurantId")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                restaurant = documentSnapshot.toObject(Restaurant.class);
                Menu menu = new Menu();
                for (Menu menuI: restaurant.getMenu()){
                    if (menuI.getMenuItemId().equals(intent.getStringExtra("menuItermId"))){
                        menu = menuI;
                    }
                }
                menuItemName.setText(menu.getMenuItemName());
                menuItemDesc.setText(menu.getMenuItemDescription());
                storageRef.child(menu.getMenuItemImagesId().get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                int i = 0;
                RadioGroup radioGroup = new RadioGroup(MenuDetailActivity.this);
                for (Map.Entry<String, HashMap<String, Double>> entry : menu.getAddOns().entrySet()){
                    radioGroup.setId(1001111 +  i++);
                    TextView textView = new TextView(MenuDetailActivity.this);
                    textView.setText(entry.getKey());
                    textView.setTextSize(18);
                    radioGroup.addView(textView);
                    int t = 0;
                    for (Map.Entry<String, Double> addOnsEntry: entry.getValue().entrySet()){
                        RadioButton radioButton  = new RadioButton(MenuDetailActivity.this);
                        radioButton.setId(1001111+t++);
                        radioButton.setTextSize(14);
                        radioButton.setText(addOnsEntry.getKey() +"                       R"+ addOnsEntry.getValue());
                        radioGroup.addView(radioButton);
                    }
                }
                layout.addView(radioGroup);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }





}