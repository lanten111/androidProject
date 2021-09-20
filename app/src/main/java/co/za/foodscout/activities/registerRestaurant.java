package co.za.foodscout.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import co.za.foodscout.Domain.MenuCatagories;
import co.za.foodscout.Domain.Restaurant.Menu;
import co.za.foodscout.Domain.Restaurant.Restaurant;
import foodscout.R;

public class registerRestaurant extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_raustarant);

        Button restaurantRegister = findViewById(R.id.restaurantRegister);

        restaurantRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurant restaurant = new Restaurant();
                Menu menu1 = new Menu();
                Menu menu2 = new Menu();
                List<Menu> menuList = new ArrayList<>();
                Map hashMap = new HashMap();
                Map hashMap2 = new HashMap();
                Map hashMap3 = new HashMap();

                menu1.setMenuItemId(UUID.randomUUID().toString());
                menu1.setMenuCatagories(MenuCatagories.BURGER);
                menu1.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu1.setMenuItemName("cheese Burger");
                menu1.setMenuItemPrice(Double.valueOf(31));
                List<String> menuImages = new ArrayList<>();
                menuImages.add("burger 2.jpg");
                menu1.setMenuItemImagesId(menuImages);

                menu2.setMenuItemId(UUID.randomUUID().toString());
                menu2.setMenuCatagories(MenuCatagories.BURGER);
                menu2.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu2.setMenuItemName("Beef Burger");
                menu2.setMenuItemPrice(Double.valueOf(35));
                List<String> menuImages2 = new ArrayList<>();
                menuImages2.add("burger 1.jpg");
                menu2.setMenuItemImagesId(menuImages2);

                hashMap2.put("1L coke", Double.valueOf(5));
                hashMap2.put("2L Sprite", Double.valueOf(18));
                hashMap2.put("1L Sprite", Double.valueOf(35));
                hashMap2.put("350ML coke", Double.valueOf(13));

                hashMap3.put("small", Double.valueOf(12));
                hashMap3.put("medium", Double.valueOf(18));
                hashMap3.put("large", Double.valueOf(35));


                hashMap.put("Flavour of a drink", hashMap2);
                hashMap.put("Choose Meal Type", hashMap3);
                menu1.setAddOns(hashMap);
                menu2.setAddOns(hashMap);
                menuList.add(menu1);
                menuList.add(menu2);


                restaurant.setName("something house Restaurant");
                restaurant.setDescription("Lorem ipsum dolor sit amet. Sit facilis omnis ut voluptatem vero a doloribus veniam ea eaque porro qui soluta fuga qui quaerat dolorem! Est adipisci maxime qui laudantium quia in voluptas modi est quia");
                restaurant.setOwner("Bary Allene");
                restaurant.setLocation(new GeoPoint(-26.36459761078716, 28.212583271318966));
                restaurant.setAddress(getAddress(restaurant.getLocation()));
                restaurant.setContactNumber("0659599252");
                restaurant.setRating(47);
                restaurant.setMainImageId("restaurant 6.jpeg");
                restaurant.setMenu(menuList);

                db.collection("restaurant").add(restaurant).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(registerRestaurant.this, "successfully added new restaurant",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
}