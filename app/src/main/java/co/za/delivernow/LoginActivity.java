package co.za.delivernow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        TextView registerButton = (TextView) findViewById(R.id.buttonRegisterLogin);
        Button loginButton = (Button) findViewById(R.id.buttonLogin);
        ProgressBar loginProgressBar = findViewById(R.id.loginProgressBar);
        loginProgressBar.setVisibility(View.INVISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        EditText email = findViewById(R.id.userEmail);
        EditText password = findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgressBar.setVisibility(View.VISIBLE);
                login(email.getText().toString(), password.getText().toString());
                loginProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void signIn(String email, String password){

        firebaseAuth.signInWithEmailAndPassword(email, password)
           .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    void login(String email, String password){
        if (checkEmail(email)) {
            Toast.makeText(this, "email must not be empty", Toast.LENGTH_LONG).show();
        } else if (checkPassword(password)){
            Toast.makeText(this, "password must not be empty", Toast.LENGTH_LONG).show();
        } else if ( !checkEmail(email) && !checkPassword(password)         ){
            signIn(email, password);
        }
    }

    Boolean checkEmail(String email){
        return TextUtils.isEmpty(email);
    }

    Boolean checkPassword(String password){ ;
        return TextUtils.isEmpty(password);
    }

    Boolean isValidEmail(EditText email){
        CharSequence str = email.getText();
        return  !(Patterns.EMAIL_ADDRESS.matcher(str).matches());
    }
}