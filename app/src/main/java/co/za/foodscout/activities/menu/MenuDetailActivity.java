package co.za.foodscout.activities.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.FireStoreCart;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Restaurant.Addons;
import co.za.foodscout.Domain.Restaurant.Menu;
import co.za.foodscout.Domain.Restaurant.MenuAddons;
import co.za.foodscout.Domain.Restaurant.Restaurant;
import co.za.foodscout.activities.DrawerActivity;
import co.za.foodscout.activities.order.CartViewActivity;
import foodscout.R;

public class MenuDetailActivity extends DrawerActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();
    Restaurant restaurant = new Restaurant();
    FirestoreDelivery fireStoreDelivery = new FirestoreDelivery();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Menu menu = new Menu();
    FireStoreCart fireStoreCart = new FireStoreCart();
    Double totalPrice = Double.valueOf(0);
    int intemNumebrInCart = 0;

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
        Button viewCart = findViewById(R.id.viewCartButton);
        Chip viewPrice = findViewById(R.id.viewcartPrice);
        Chip itemInCart = findViewById(R.id.itemInCart);
//        Button addItem = findViewById(R.id.addMenuButton);
//        Button removeItem = findViewById(R.id.removeMenuButton);


        TableLayout tableLayout = findViewById(R.id.RadioGroulinearLayout);
//        TableRow tableRow = findViewById(R.id.RadioGroulinearLayoutchild0);

        //cchek if there is something in the cart
        firestore.collection(Collections.cart.name()).whereEqualTo("complete", false).whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                intemNumebrInCart = queryDocumentSnapshots.size();
                List<FireStoreCart> fireStoreCartList = queryDocumentSnapshots.toObjects(FireStoreCart.class);
                for (FireStoreCart fireStoreCart: fireStoreCartList){
                    totalPrice = totalPrice + fireStoreCart.getItemPrice();
                }
                viewPrice.setText("R "+totalPrice.toString());
                itemInCart.setText("In cart "+ String.valueOf(intemNumebrInCart));
            }
        });

        List<MenuAddons> groupedCartMenuAddonsList = new ArrayList<>();
        List<MenuAddons> cartMenuAddonsList = new ArrayList<>();
        this.firestore.collection(Collections.restaurant.name()).document(intent.getStringExtra("restaurantId")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                restaurant = documentSnapshot.toObject(Restaurant.class);
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
                    public void onFailure(@NonNull Exception e) { }
                });

                TableRow.LayoutParams tableParam = new TableRow.LayoutParams();
                tableParam.setMargins(0, 10, 300, 10);
                int id = 0;
                for (int i = 0; i < menu.getAddOns().size(); i++){
                    TableRow tableRow = new TableRow(MenuDetailActivity.this);
                    TextView addOnTitleTextView = new TextView(MenuDetailActivity.this);
                    TextView addEmptyText = new TextView(MenuDetailActivity.this);
                    addOnTitleTextView.setLayoutParams(tableParam);

                    addOnTitleTextView.setText(menu.getAddOns().get(i).getName());
                    addOnTitleTextView.setTextSize(18);

                    tableRow.addView(addOnTitleTextView);
                    tableRow.addView(addEmptyText);
                    tableLayout.addView(tableRow);

                    id = id + 100;
                    for (int r = 0; r < menu.getAddOns().get(i).getAddonsList().size(); r++){
                        TableRow tableRow1 = new TableRow(MenuDetailActivity.this);
                        RadioButton radioButton  = new RadioButton(MenuDetailActivity.this);
                        TextView addonsPricesTextView = new TextView(MenuDetailActivity.this);
                        radioButton.setId(id+r);
                        radioButton.setTextSize(14);
                        radioButton.setLayoutParams(tableParam);
                        radioButton.setText(menu.getAddOns().get(i).getAddonsList().get(r).getAddon());
                        addonsPricesTextView.setId(id);

                        if (menu.getAddOns().get(i).getAddonsList().get(r).getPrice() != null){
                            addonsPricesTextView.setText("+ R"+menu.getAddOns().get(i).getAddonsList().get(r).getPrice().toString());
                        } else {
                            addonsPricesTextView.setText("");
                        }

                        tableRow1.addView(radioButton);
                        tableRow1.addView(addonsPricesTextView);
                        tableLayout.addView(tableRow1);

                        radioButton.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                        MenuAddons menuAddons = new MenuAddons();
                                           for (int t = 0; t < menu.getAddOns().size(); t++){
                                             for (int i = 0; i < menu.getAddOns().get(t).getAddonsList().size(); i++){
                                                if (menu.getAddOns().get(t).getAddonsList().get(i).getAddon().contentEquals(radioButton.getText())){
                                                    menuAddons = menu.getAddOns().get(t);
                                                }
                                            }
                                        }
                                        if (menuAddons.isGrouped()){
                                            MenuAddons cartMenuAddons = new MenuAddons();
                                            String x = String.valueOf(radioButton.getId()).charAt(0)+"00";
                                            for (int t = Integer.parseInt(x); t<menuAddons.getAddonsList().size() + Integer.parseInt(x); t++){

                                                RadioButton radioButton1 = findViewById(t);
                                                radioButton.setChecked(true);
                                                radioButton.setSelected(true);
                                                if (radioButton.getId() != t){
                                                    radioButton1.setChecked(false);
                                                    radioButton1.setSelected(false);
                                                }
                                                if (groupedCartMenuAddonsList.size() >= 1){
                                                    for (int i = 0; i <  groupedCartMenuAddonsList.size(); i++){
                                                        for (int r = 0; r < groupedCartMenuAddonsList.get(i).getAddonsList().size(); r++){
                                                            if (groupedCartMenuAddonsList.get(i).getAddonsList().get(r).getAddon().contentEquals(radioButton1.getText())){
                                                                groupedCartMenuAddonsList.remove(r);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                int index = Integer.parseInt(String.valueOf(String.valueOf(t).charAt(2)));
                                                if (radioButton.getText().equals(menuAddons.getAddonsList().get(index).getAddon())){
                                                    cartMenuAddons.setName(menuAddons.getName());
                                                    List<Addons> addonsList = new ArrayList<>();
                                                    for (Addons addon: menuAddons.getAddonsList()){
                                                        if (addon.getAddon().contentEquals(radioButton.getText())){
                                                            Addons addons = new Addons();
                                                            addons.setAddon(addon.getAddon());
                                                            addons.setPrice(addon.getPrice());
                                                            addonsList.add(addons);
                                                        }
                                                    }
                                                    cartMenuAddons.setAddonsList(addonsList);
                                                    radioButton.setChecked(true);
                                                    radioButton.setSelected(true);
                                                    groupedCartMenuAddonsList.add(cartMenuAddons);
                                                }

                                            }
                                        } else {
                                            if (radioButton.isChecked()){
                                                radioButton.setChecked(false);
                                                radioButton.setSelected(false);
                                                for (int i = 0; i <  cartMenuAddonsList.size(); i++){
                                                    for (int t = 0; t < cartMenuAddonsList.get(i).getAddonsList().size(); t++){
                                                        if (cartMenuAddonsList.get(i).getAddonsList().get(t).getAddon().contentEquals(radioButton.getText())){
                                                            cartMenuAddonsList.remove(i);
                                                            break;
                                                        }
                                                    }
                                                }
                                            } else {
                                                MenuAddons cartMenuAddons = new MenuAddons();
                                                List<Addons> addonsList = new ArrayList<>();
                                                cartMenuAddons.setName(menuAddons.getName());
                                                for (Addons addon: menuAddons.getAddonsList()){
                                                    if (addon.getAddon().contentEquals(radioButton.getText())){
                                                        Addons addons = new Addons();
                                                        addons.setAddon(addon.getAddon());
                                                        addons.setPrice(addon.getPrice());
                                                        addonsList.add(addons);
                                                    }
                                                }
                                                cartMenuAddons.setAddonsList(addonsList);
                                                radioButton.setChecked(true);
                                                radioButton.setSelected(true);
                                                cartMenuAddonsList.add(cartMenuAddons);
                                            }
                                        }
                                    }
//                                    System.out.println(cartMenuAddons.toString());
                                    return true;
                                }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalPrice = totalPrice + menu.getMenuItemPrice();
                List<MenuAddons> menuAddonsList = new ArrayList<>();
                for (MenuAddons menuAddons : cartMenuAddonsList) {
                    menuAddonsList.add(menuAddons);
                    for (Addons addon : menuAddons.getAddonsList()) {
                        if (addon.getPrice() != null){
                            totalPrice = totalPrice + addon.getPrice();
                        }
                    }
                }

                for (MenuAddons menuAddons : groupedCartMenuAddonsList) {
                    menuAddonsList.add(menuAddons);
                    for (Addons addon : menuAddons.getAddonsList()) {
                        if (addon.getPrice() != null){
                            totalPrice = totalPrice + addon.getPrice();
                        }
                    }
                }
                menuAddonsList.addAll(cartMenuAddonsList);
                menuAddonsList.addAll(groupedCartMenuAddonsList);
                String id = UUID.randomUUID().toString();
                fireStoreCart.setUserId(firebaseAuth.getCurrentUser().getUid());
                fireStoreCart.setMenuAddonsList(menuAddonsList);
                fireStoreCart.setItemName(menu.getMenuItemName());
                fireStoreCart.setItemPrice(totalPrice);
                fireStoreCart.setRestaurantId(restaurant.getId());
                fireStoreCart.setRetailName(restaurant.getName());
                fireStoreCart.setComplete(false);
                fireStoreCart.setCompleteDate(Timestamp.now());
                fireStoreCart.setDateAdded(Timestamp.now());
                fireStoreCart.setUserId(firebaseAuth.getCurrentUser().getUid());
                fireStoreCart.setId(id);
                fireStoreCart.setOrigin(restaurant.getLocation());

                firestore.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                        fireStoreCart.setDestination(firestoreUser.getLocation());
                        firestore.collection(Collections.cart.name()).document(id).set(fireStoreCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                viewPrice.setText("R " + totalPrice.toString());
                                Toast.makeText(MenuDetailActivity.this, "Added item to cart", Toast.LENGTH_SHORT).show();
                                firestore.collection(Collections.cart.name()).whereEqualTo("complete", false).whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        intemNumebrInCart = queryDocumentSnapshots.size();
                                        itemInCart.setText("In cart "+ String.valueOf(intemNumebrInCart));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuDetailActivity.this, CartViewActivity.class));
            }
        });
    }
}