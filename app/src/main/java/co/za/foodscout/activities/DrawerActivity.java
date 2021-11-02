package co.za.foodscout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Enum.Role;
import co.za.foodscout.activities.account.AccountActivity;
import co.za.foodscout.activities.account.LoginActivity;
import co.za.foodscout.activities.retail.registerRestaurant;
import co.za.foodscout.activities.delivery.DeliveriesActivity;
import co.za.foodscout.activities.retail.RetailsActivity;
import foodscout.R;

public abstract class DrawerActivity extends AppCompatActivity {

    protected NavigationView navigationView;
    protected ActionBarDrawerToggle drawerToggle;
    protected FrameLayout frameLayout;
    private DrawerLayout mDrawerLayout;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        frameLayout = findViewById(R.id.frame);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        setupDrawerContent(navigationView);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        if (firebaseAuth.getCurrentUser() != null){
            firestore.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    FirestoreUser firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                    if (firestoreUser != null && firestoreUser.getRole()!= null){
                        if (firestoreUser.getRole().equals(Role.DRIVER)){
                            menu.findItem(R.id.nav_order_history).setVisible(false);
                        } else  if(firestoreUser.getRole().equals(Role.USER)){
                            menu.findItem(R.id.nav_active_deliveries).setVisible(false);
                            menu.findItem(R.id.nav_completed_deliveries).setVisible(false);
                        }
                    } else {
                        signOut();
                        Toast.makeText(DrawerActivity.this, "something went wrong, Please login again", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    menu.findItem(R.id.nav_order_history).setVisible(false);
                    menu.findItem(R.id.nav_active_deliveries).setVisible(false);
                    menu.findItem(R.id.nav_completed_deliveries).setVisible(false);
                }
            });
        }else {
            menu.findItem(R.id.nav_order_history).setVisible(false);
            menu.findItem(R.id.nav_active_deliveries).setVisible(false);
            menu.findItem(R.id.nav_completed_deliveries).setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectNavItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectNavItem(MenuItem item){
        if (item.getTitle().toString().equals(getResources().getString(R.string.logout))){
            signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Successfully signed Out", Toast.LENGTH_SHORT).show();
        }else if (item.getTitle().toString().equals(getResources().getString(R.string.my_account))){
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        } else if (item.getTitle().toString().equals(getResources().getString(R.string.settings))){
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (item.getTitle().toString().equals(getResources().getString(R.string.active_delivery))){
            Intent intent = new Intent(this, DeliveriesActivity.class);
            startActivity(intent);
        } else if (item.getTitle().toString().equals(getResources().getString(R.string.register_restaurant))){
            Intent intent = new Intent(this, registerRestaurant.class);
            startActivity(intent);
        } else if (item.getTitle().toString().equals(getResources().getString(R.string.home))){
            Intent intent = new Intent(this, RetailsActivity.class);
            startActivity(intent);
        }
    }

    public void checkIfUserLoggedIn(){
        if (firebaseAuth.getCurrentUser() == null){
            Toast.makeText(getApplicationContext(), "please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    public void disabledWindow(CircularProgressIndicator circularProgressIndicator){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
    }

    public void enableWindow(CircularProgressIndicator circularProgressIndicator){
        circularProgressIndicator.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void signOut() {
        startActivity(new Intent(DrawerActivity.this, LoginActivity.class));
        FirebaseAuth.getInstance().signOut();
    }

}