package co.za.foodscout.activities.retail;

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
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.Enum.MenuCatagories;
import co.za.foodscout.Domain.Restaurant.Addons;
import co.za.foodscout.Domain.Restaurant.Menu;
import co.za.foodscout.Domain.Restaurant.MenuAddons;
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
                Menu menu3 = new Menu();
                Menu menu4 = new Menu();
                Menu menu5 = new Menu();
                Menu menu6 = new Menu();
                Menu menu7 = new Menu();
                Menu menu8 = new Menu();
                Menu menu9 = new Menu();
                Menu menu10 = new Menu();
                Menu menu11 = new Menu();
                Menu menu12 = new Menu();

                List<Menu> menuList = new ArrayList<>();
                MenuAddons menuAddons = new MenuAddons();
                MenuAddons menuAddons1 = new MenuAddons();
                MenuAddons menuAddons2 = new MenuAddons();
                List<MenuAddons> menuAddonsList = new ArrayList<>();

                menu1.setMenuItemId(UUID.randomUUID().toString());
                menu1.setMenuCatagories(MenuCatagories.PAP);
                menu1.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu1.setMenuItemName("cheese Burger");
                menu1.setMenuItemPrice(Double.valueOf(31));
                List<String> menuImages = new ArrayList<>();
                menuImages.add("burger 2.jpg");
                menu1.setMenuItemImagesId(menuImages);

                menu2.setMenuItemId(UUID.randomUUID().toString());
                menu2.setMenuCatagories(MenuCatagories.Pizza);
                menu2.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu2.setMenuItemName("Beef Burger");
                menu2.setMenuItemPrice(Double.valueOf(35));
                List<String> menuImages2 = new ArrayList<>();
                menuImages2.add("burger 1.jpg");
                menu2.setMenuItemImagesId(menuImages2);

                menu3.setMenuItemId(UUID.randomUUID().toString());
                menu3.setMenuCatagories(MenuCatagories.BURGER);
                menu3.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu3.setMenuItemName("Tomato Burger");
                menu3.setMenuItemPrice(Double.valueOf(33));
                List<String> menuImages3 = new ArrayList<>();
                menuImages3.add("burger 3.jpg");
                menu3.setMenuItemImagesId(menuImages3);

                menu4.setMenuItemId(UUID.randomUUID().toString());
                menu4.setMenuCatagories(MenuCatagories.BURGER);
                menu4.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu4.setMenuItemName("Tomato Burger");
                menu4.setMenuItemPrice(Double.valueOf(33));
                List<String> menuImages4 = new ArrayList<>();
                menuImages4.add("burger 3.jpg");
                menu4.setMenuItemImagesId(menuImages3);

                menu5.setMenuItemId(UUID.randomUUID().toString());
                menu5.setMenuCatagories(MenuCatagories.BURGER);
                menu5.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu5.setMenuItemName("Tomato Burger");
                menu5.setMenuItemPrice(Double.valueOf(33));
                List<String> menuImages6 = new ArrayList<>();
                menuImages6.add("burger 3.jpg");
                menu5.setMenuItemImagesId(menuImages3);

                menu6.setMenuItemId(UUID.randomUUID().toString());
                menu6.setMenuCatagories(MenuCatagories.BURGER);
                menu6.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu6.setMenuItemName("Tomato Burger");
                menu6.setMenuItemPrice(Double.valueOf(33));
                List<String> menuImages7 = new ArrayList<>();
                menuImages7.add("burger 3.jpg");
                menu6.setMenuItemImagesId(menuImages3);

                menu7.setMenuItemId(UUID.randomUUID().toString());
                menu7.setMenuCatagories(MenuCatagories.BURGER);
                menu7.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu7.setMenuItemName("Tomato Burger");
                menu7.setMenuItemPrice(Double.valueOf(33));
                List<String> menuImages8 = new ArrayList<>();
                menuImages8.add("burger 1.jpg");
                menu7.setMenuItemImagesId(menuImages8);

                menu8.setMenuItemId(UUID.randomUUID().toString());
                menu8.setMenuCatagories(MenuCatagories.BURGER);
                menu8.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu8.setMenuItemName("Tomato Burger");
                menu8.setMenuItemPrice(Double.valueOf(33));
                List<String> menuImages9 = new ArrayList<>();
                menuImages9.add("burger 2.jpg");
                menu8.setMenuItemImagesId(menuImages9);


                menu9.setMenuItemId(UUID.randomUUID().toString());
                menu9.setMenuCatagories(MenuCatagories.PAP);
                menu9.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu9.setMenuItemName("cheese Burger");
                menu9.setMenuItemPrice(Double.valueOf(31));
                List<String> menuImages11 = new ArrayList<>();
                menuImages11.add("burger 2.jpg");
                menu9.setMenuItemImagesId(menuImages11);

                menu10.setMenuItemId(UUID.randomUUID().toString());
                menu10.setMenuCatagories(MenuCatagories.PAP);
                menu10.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu10.setMenuItemName("cheese Burger");
                menu10.setMenuItemPrice(Double.valueOf(31));
                List<String> menuImages12 = new ArrayList<>();
                menuImages.add("burger 2.jpg");
                menu10.setMenuItemImagesId(menuImages);

                menu11.setMenuItemId(UUID.randomUUID().toString());
                menu11.setMenuCatagories(MenuCatagories.PAP);
                menu11.setMenuItemDescription("A tomato - based beef stew served with rice or starchy vegetables");
                menu11.setMenuItemName("cheese Burger");
                menu11.setMenuItemPrice(Double.valueOf(31));
                List<String> menuImages13 = new ArrayList<>();
                menuImages.add("burger 2.jpg");
                menu11.setMenuItemImagesId(menuImages);


                List<Addons> addonsList = new ArrayList<>();
                menuAddons.setGrouped(false);
                menuAddons.setName("Flavour of a drink");
                Addons addons01 = new Addons();
                addons01.setAddon("1L coke");
                addons01.setPrice(Double.valueOf(23));
                addonsList.add(addons01);
                Addons addons02 = new Addons();
                addons02.setAddon("2L Sprite");
                addons02.setPrice(Double.valueOf(23));
                addonsList.add(addons02);
                Addons addons03 = new Addons();
                addons03.setAddon("1L Sprite");
                addons03.setPrice(Double.valueOf(23));
                addonsList.add(addons03);
                Addons addons04 = new Addons();
                addons04.setAddon("350ML coke");
                addons04.setPrice(Double.valueOf(23));
                addonsList.add(addons04);
                menuAddons.setAddonsList(addonsList);

                List<Addons> addonsList1 = new ArrayList<>();
                Addons addons1 = new Addons();
                menuAddons1.setGrouped(true);
                menuAddons1.setName("Choose Meal Type");
                menuAddons.setName("Flavour of a drink");
                addons1.setAddon("small");
                addons1.setPrice(Double.valueOf(23));
                addonsList1.add(addons1);
                Addons addons11 = new Addons();
                addons11.setAddon("medium");
                addons11.setPrice(Double.valueOf(23));
                addonsList1.add(addons11);
                Addons addons12 = new Addons();
                addons12.setAddon("large");
                addons12.setPrice(Double.valueOf(23));
                addonsList1.add(addons12);
                menuAddons1.setAddonsList(addonsList1);

                List<Addons> addonsList2 = new ArrayList<>();
                Addons addons2 = new Addons();
                menuAddons2.setName("Flavour");
                menuAddons2.setGrouped(true);
                Addons addons21 = new Addons();
                addons21.setAddon("checiken");
                addons21.setPrice(Double.valueOf(0));
                addonsList2.add(addons21);
                Addons addons22 = new Addons();
                addons22.setAddon("beef");
                addons22.setPrice(Double.valueOf(0));
                addonsList2.add(addons22);
                Addons addons23 = new Addons();
                addons23.setAddon("vegetarian");
                addons23.setPrice(Double.valueOf(0));
                addonsList2.add(addons23);
                menuAddons2.setAddonsList(addonsList2);

                MenuAddons menuAddons3 = new MenuAddons();
                List<Addons> addonsList3 = new ArrayList<>();
                menuAddons3.setName("What to add");
                menuAddons3.setGrouped(false);
                Addons addons33 = new Addons();
                addons33.setAddon("cheese");
                addons33.setPrice(Double.valueOf(23));
                addonsList3.add(addons33);
                Addons addons31 = new Addons();
                addons31.setAddon("toamntos");
                addons31.setPrice(Double.valueOf(23));
                addonsList3.add(addons31);
                Addons addons32 = new Addons();
                addons32.setAddon("source");
                addons32.setPrice(Double.valueOf(23));
                addonsList3.add(addons32);
                menuAddons3.setAddonsList(addonsList3);

                menuAddonsList.add(menuAddons);
                menuAddonsList.add(menuAddons1);
                menuAddonsList.add(menuAddons2);
                menuAddonsList.add(menuAddons3);


                menu1.setAddOns(menuAddonsList);
                menu2.setAddOns(menuAddonsList);
                menu3.setAddOns(menuAddonsList);
                menu4.setAddOns(menuAddonsList);
                menu5.setAddOns(menuAddonsList);
                menu6.setAddOns(menuAddonsList);
                menu7.setAddOns(menuAddonsList);
                menu8.setAddOns(menuAddonsList);
                menu9.setAddOns(menuAddonsList);
                menu10.setAddOns(menuAddonsList);
                menu11.setAddOns(menuAddonsList);
                menu12.setAddOns(menuAddonsList);

                menuList.add(menu1);
                menuList.add(menu2);
                menuList.add(menu3);
                menuList.add(menu4);
                menuList.add(menu5);
                menuList.add(menu6);
                menuList.add(menu7);
                menuList.add(menu8);
                menuList.add(menu9);
                menuList.add(menu10);
                menuList.add(menu11);
//                menuList.add(menu12);


                restaurant.setName("house Restaurant");
                restaurant.setDescription("Lorem ipsum dolor sit amet. Sit facilis omnis ut voluptatem vero a doloribus veniam ea eaque porro qui soluta fuga qui quaerat dolorem! Est adipisci maxime qui laudantium quia in voluptas modi est quia");
                restaurant.setOwner("Bary Allene");
                restaurant.setLocation(new GeoPoint(-26.36459761078716, 28.212583271318966));
                restaurant.setAddress(getAddress(restaurant.getLocation()));
                restaurant.setContactNumber("0659599252");
                restaurant.setRating(47);
                restaurant.setMainImageId("restaurant 4.jpg");
                restaurant.setMenu(menuList);
//
//                db.collection(Collections.restaurant.name()).document("RoAYAcCn6HOjTzz3K4yd").set(restaurant).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(registerRestaurant.this, "successfully added new restaurant",Toast.LENGTH_SHORT).show();
//                    }
//                });
                db.collection("restaurant").document(firebaseAuth.getCurrentUser().getUid()).set(restaurant).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        restaurant.setId(firebaseAuth.getCurrentUser().getUid());
                        for (Menu menu: restaurant.getMenu()){
                            menu.setRestaurantId(firebaseAuth.getCurrentUser().getUid());
                        }
                        db.collection(Collections.restaurant.name()).document(firebaseAuth.getCurrentUser().getUid()).set(restaurant).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(registerRestaurant.this, "successfully added new restaurant",Toast.LENGTH_SHORT).show();
                            }
                        });
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