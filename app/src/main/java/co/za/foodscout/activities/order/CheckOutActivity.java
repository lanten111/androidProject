package co.za.foodscout.activities.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.Enum.DeliveryStatus;
import co.za.foodscout.Domain.Enum.PaymentMethod;
import co.za.foodscout.Domain.FireStoreCart;
import co.za.foodscout.Domain.FireStoreOrders;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Restaurant.Restaurant;
import co.za.foodscout.Utils.Utils;
import co.za.foodscout.activities.DrawerActivity;
import co.za.foodscout.activities.retail.RetailsActivity;
import co.za.foodscout.activities.user.UserOrderViewActivity;
import foodscout.R;

public class CheckOutActivity extends DrawerActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();
    Restaurant restaurant = new Restaurant();
    FirestoreDelivery fireStoreDelivery = new FirestoreDelivery();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    List<FireStoreCart> fireStoreCartList = new ArrayList<>();
    Double totalPrice = Double.valueOf(0);
    FireStoreOrders fireStoreOrders = new FireStoreOrders();
    FirestoreDelivery firestoreDelivery = new FirestoreDelivery();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_check_out, frameLayout);

        getIntent().setAction("checkout");

        CircularProgressIndicator circularProgressBar = findViewById(R.id.loadingBar);
        ConstraintLayout constraintLayout = findViewById(R.id.secondayLayout);
        constraintLayout.setVisibility(View.INVISIBLE);

        TextView orderOrigin = findViewById(R.id.orderOrigin);
        TextView orderAmount = findViewById(R.id.orderAmount);
        RadioGroup paymentMethod = findViewById(R.id.paymentMethod);
        Button placeOrder = findViewById(R.id.placeOderButton);
        EditText additionalNote  = findViewById(R.id.additionalNote);
        TextView retailLocation = findViewById(R.id.retailLocation);
        TextView retailName = findViewById(R.id.retailName);


        for (int i = 0; i < PaymentMethod.values().length; i++){
            RadioButton radioButton = new RadioButton(CheckOutActivity.this);
            radioButton.setId(i);
            radioButton.setText(PaymentMethod.values()[i].getDescription());
            radioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_card, 0, 0, 0);
            paymentMethod.addView(radioButton);
        }

        firestore.collection(Collections.cart.name()).whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("complete", false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                circularProgressBar.setVisibility(View.INVISIBLE);
                constraintLayout.setVisibility(View.VISIBLE);

                fireStoreCartList = queryDocumentSnapshots.toObjects(FireStoreCart.class);
                if (fireStoreCartList.size() <= 0){
                    startActivity(new Intent(getApplicationContext(), RetailsActivity.class));
                }
                retailName.setText(fireStoreCartList.get(0).getRetailName());
                retailLocation.setText(" " +Utils.getAddress(fireStoreCartList.get(0).getDestination(), CheckOutActivity.this));
                orderOrigin.setText("Restaurant Adress: "+Utils.getAddress(fireStoreCartList.get(0).getOrigin(), CheckOutActivity.this));
                for (FireStoreCart fireStoreCart: fireStoreCartList){
                    totalPrice = totalPrice + fireStoreCart.getItemPrice();
                }
                orderAmount.setText("R"+totalPrice.toString());
            }
        });

        paymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                fireStoreOrders.setPaymentMethod(PaymentMethod.findByDescription(radioButton.getText().toString()));
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disabledWindow(circularProgressBar);
                String orderId = UUID.randomUUID().toString();
                if (paymentMethod.getCheckedRadioButtonId() == -1){
                    Toast.makeText(CheckOutActivity.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                    enableWindow(circularProgressBar);
                    return;
                }
                fireStoreOrders.setAdditionalOrderNote(additionalNote.getText().toString());
                fireStoreOrders.setRetailId(fireStoreCartList.get(0).getRestaurantId());
                fireStoreOrders.setUserId(firebaseAuth.getCurrentUser().getUid());
                fireStoreOrders.setRetailLocation(fireStoreCartList.get(0).getOrigin());
                fireStoreOrders.setId(orderId);
                fireStoreOrders.setComplete(false);
                fireStoreOrders.setCartList(fireStoreCartList);
                fireStoreOrders.setPaid(false);
                fireStoreOrders.setRetailName(fireStoreCartList.get(0).getRetailName());
                fireStoreOrders.setTotalPrice(totalPrice);
                fireStoreOrders.setDateCreated(Timestamp.now());
                fireStoreOrders.setDateUpdated(Timestamp.now());
                fireStoreOrders.setOrderReady(false);
                firestore.collection(Collections.order.name()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int orderNumber = queryDocumentSnapshots.size() + 1;
                        fireStoreOrders.setOrderNumber(orderNumber);
                        firestore.collection(Collections.order.name()).document(orderId).set(fireStoreOrders).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                String firestoreDeliveryId = UUID.randomUUID().toString();
                                firestoreDelivery.setAssigned(false);
                                firestoreDelivery.setDelivered(false);
                                firestoreDelivery.setDeliveryPicked(false);
                                firestoreDelivery.setDateCreated(Timestamp.now());
                                firestoreDelivery.setDateUpdated(Timestamp.now());
                                firestoreDelivery.setId(firestoreDeliveryId);
                                firestoreDelivery.setRetailLocation(fireStoreOrders.getRetailLocation());
                                firestoreDelivery.setRetailName(fireStoreOrders.getRetailName());
                                firestoreDelivery.setRetailId(fireStoreOrders.getRetailId());
                                firestoreDelivery.setOrderId(fireStoreOrders.getId());
                                firestoreDelivery.setOrderNumber(fireStoreOrders.getOrderNumber());
                                firestoreDelivery.setDeliveryStatus(DeliveryStatus.Preparing);
                                firestore.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                                        firestoreDelivery.setUserLocation(firestoreUser.getLocation());
                                        firestoreDelivery.setUserId(firestoreUser.getId());
                                        firestoreDelivery.setUserNames(firestoreUser.getName());
                                        firestoreDelivery.setContactNo(firestoreUser.getPhone());
                                        firestoreDelivery.setUserId(firebaseAuth.getCurrentUser().getUid());
                                        firestore.collection(Collections.delivery.name()).document(firestoreDeliveryId).set(firestoreDelivery).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                for (FireStoreCart fireStoreCart: fireStoreCartList){
                                                    fireStoreCart.setCompleteDate(Timestamp.now());
                                                    fireStoreCart.setComplete(true);
                                                    firestore.collection(Collections.cart.name()).document(fireStoreCart.getId()).set(fireStoreCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(CheckOutActivity.this, "Order successfully placed", Toast.LENGTH_SHORT).show();
                                                            enableWindow(circularProgressBar);
                                                        }
                                                    });
                                                }
                                                startActivity(new Intent(CheckOutActivity.this, UserOrderViewActivity.class));
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        if(action == null || !action.equals("checkout")) {
            Intent intent = new Intent(this, RetailsActivity.class);
            startActivity(intent);
            finish();
        }
        else
            getIntent().setAction(null);
        super.onResume();
    }
}