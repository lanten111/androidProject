package co.za.delivernow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends DrawerActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_menu, frameLayout);

        Intent intent = getIntent();
        String adress = intent.getStringExtra("location");
        TextView textView = findViewById(R.id.txt1);
        textView.setText(adress);
    }
}