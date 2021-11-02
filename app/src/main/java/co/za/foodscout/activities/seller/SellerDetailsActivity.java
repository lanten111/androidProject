package co.za.foodscout.activities.seller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.Enum.DeliveryStatus;
import co.za.foodscout.Domain.FireStoreCart;
import co.za.foodscout.Domain.FireStoreOrders;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Restaurant.Addons;
import co.za.foodscout.Domain.Restaurant.MenuAddons;
import co.za.foodscout.activities.DrawerActivity;
import foodscout.R;

public class SellerDetailsActivity extends DrawerActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    FireStoreOrders fireStoreOrders;
    FirestoreUser firestoreUser;
    FirestoreDelivery firestoreDelivery;
    String orderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_seller_details, frameLayout);

        CircularProgressIndicator circularProgressBar = findViewById(R.id.loadingBar);
        ConstraintLayout constraintLayout = findViewById(R.id.secondayLayout);
        constraintLayout.setVisibility(View.INVISIBLE);

        orderId = getIntent().getStringExtra("orderId");
        TextView userName = findViewById(R.id.SellerUserName);
        TextView orderNumber = findViewById(R.id.SellerOrderNumber);
        TextView orderName = findViewById(R.id.SellerOrderContact);
        LinearLayout addonList = findViewById(R.id.SellerOrderDetailLinearLayout);
        TextView additionalNotes = findViewById(R.id.SellerAddtionalNotes);
        TextView totalPrice = findViewById(R.id.totalPrice);
//        TextView userAddress = findViewById(R.id.SellerDeliveriesTo);
        ProgressBar progressBar = findViewById(R.id.SellerProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        TextView additionalNotesHead = findViewById(R.id.sellerAdditionaHead);
        Button orderReady = findViewById(R.id.orderReady);
        FloatingActionButton callUser = findViewById(R.id.callUser);

//        getIntent().setAction("sellerDetails");

        firestore.collection(Collections.order.name()).document(orderId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fireStoreOrders = documentSnapshot.toObject(FireStoreOrders.class);
                firestore.collection(Collections.user.name()).document(fireStoreOrders.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                        userName.setText("Order for " + firestoreUser.getName());
                        orderName.setText(firestoreUser.getPhone());
                        orderNumber.setText("Order#" + fireStoreOrders.getOrderNumber());
                        additionalNotes.setText(fireStoreOrders.getAdditionalOrderNote());
                        totalPrice.setText(Html.fromHtml("Paid amount:" + "<font color=#f57f17>" +" R"+fireStoreOrders.getTotalPrice().toString()));
                        if (fireStoreOrders.getAdditionalOrderNote().trim().equals("")){
                            additionalNotesHead.setVisibility(View.INVISIBLE);
                        }
//                        userAddress.setText(Utils.getAddress(firestoreUser.getLocation(), getApplicationContext()));

//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                        params.setMargins(10,0,0,0);
                        for (FireStoreCart fireStoreCart: fireStoreOrders.getCartList()){
                            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            TextView itemName = new TextView(getApplicationContext());
                            itemName.setTextSize(18);
                            itemName.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.food, 0 , 0 , 0);
                            itemName.setCompoundDrawablePadding(12);
                            itemName.setText(Html.fromHtml(fireStoreCart.getItemName()+":" + "<font color=#f57f17>" + " R"+fireStoreCart.getItemPrice()));
                            linearLayout.addView(itemName);

                            for (MenuAddons menuAddons: fireStoreCart.getMenuAddonsList()){
                                TextView addonCat = new TextView(getApplicationContext());
                                addonCat.setText(space(fireStoreCart.getItemName().length() + 10) + menuAddons.getName());
                                addonCat.setTextSize(16);
                                addonCat.setCompoundDrawablePadding(12);
                                linearLayout.addView(addonCat);
                                for (Addons addons: menuAddons.getAddonsList()){
                                    TextView addon = new TextView(getApplicationContext());
                                    addon.setText(space(menuAddons.getName().length() + 20)+"--"+addons.getAddon());
                                    addon.setTextSize(16);
                                    addon.setCompoundDrawablePadding(12);
//                                    addon.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle_double, 0 , 0 , 0);
                                    linearLayout.addView(addon);
                                }
                            }
                            addonList.addView(linearLayout);
                        }
                        circularProgressBar.setVisibility(View.INVISIBLE);
                        constraintLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        orderReady.setEnabled(true);
        orderReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disabledWindow(circularProgressBar);
                firestore.collection(Collections.delivery.name()).whereEqualTo("orderId", fireStoreOrders.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 1){
                            Toast.makeText(getApplicationContext(), "check me, somrehtinf is not right" ,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        firestoreDelivery = queryDocumentSnapshots.toObjects(FirestoreDelivery.class).get(0);
                        firestoreDelivery.setDeliveryStatus(DeliveryStatus.Ready);
                        firestoreDelivery.setDateUpdated(Timestamp.now());
                        fireStoreOrders.setOrderReady(true);
                        fireStoreOrders.setDateUpdated(Timestamp.now());
                        firestore.collection(Collections.order.name()).document(fireStoreOrders.getId()).set(fireStoreOrders).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                firestore.collection(Collections.delivery.name()).document(firestoreDelivery.getId()).set(firestoreDelivery).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        enableWindow(circularProgressBar);
                                        startActivity(new Intent(getApplicationContext(), SellerViewActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        enableWindow(circularProgressBar);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        callUser.setEnabled(true);
        callUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUser.setEnabled(false);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+firestoreUser.getPhone()));
                startActivity(intent);
                callUser.setEnabled(true);
            }
        });
    }

    public String space(int number){
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < number; i++){
            spaces.append(" ");
        }
        return spaces.toString();
    }

//    @Override
//    protected void onResume() {
//        String action = getIntent().getAction();
//        if(action == null || !action.equals("sellerDetails")) {
//            Intent intent = new Intent(this, SellerDetailsActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        else
//            getIntent().setAction(null);
//        super.onResume();
//    }
}