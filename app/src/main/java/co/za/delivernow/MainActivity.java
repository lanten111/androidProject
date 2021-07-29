package co.za.delivernow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    Button firstButtonSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        firstButtonSignin = (Button)findViewById(R.id.button);

        firstButtonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (firebaseUser == null){
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                }
                startActivity(intent);
            }
        });

    }
}