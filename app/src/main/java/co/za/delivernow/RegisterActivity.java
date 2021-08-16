package co.za.delivernow;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import co.za.delivernow.Domain.FirestoreUser;
import co.za.delivernow.Domain.Role;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Button buttonRegister = findViewById(R.id.buttonRegister);

        EditText email = findViewById(R.id.RegisterEmailAddress);
        EditText password = findViewById(R.id.RegisterPassword);
        EditText confirmPassword = findViewById(R.id.RegisterConfirmPassword);
        EditText name = findViewById(R.id.RegisterPersonName);
        EditText surname = findViewById(R.id.RegisterSurname);
        ProgressBar loginProgressBar = findViewById(R.id.registerProgressBar);

        loginProgressBar.setVisibility(View.INVISIBLE);
        loginProgressBar.setProgress(50);

        name.setText("bruce");
        surname.setText("wayne");
        password.setText("123456");
        confirmPassword.setText("123456");


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgressBar.setVisibility(View.VISIBLE);
                register(email.getText().toString(), password.getText().toString(),
                        confirmPassword.getText().toString(), name.getText().toString(), surname.getText().toString(), loginProgressBar);
            }
        });

    }

    private void signUp(String email, String password, String name, String surname, ProgressBar loginProgressBar){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firestoreUser.setName(name);
                        firestoreUser.setSurname(surname);
                        firestoreUser.setEmail(firebaseAuth.getCurrentUser().getEmail());
                        firestoreUser.setDateCreated(new Date());
                        firestoreUser.setRole(Role.USER);
                        db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(firestoreUser).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(RegisterActivity.this, "User registered Successful", Toast.LENGTH_SHORT).show();
                                loginProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(RegisterActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
        });
    }

    void register(String email, String password, String confirmPassword, String name, String surname, ProgressBar loginProgressBar){

        if (isEmailEmpty(email)){
            Toast toast = Toast.makeText(this, "Email must not be empty", Toast.LENGTH_SHORT);
            toast.show();
        } else if (isPasswordEmpty(password)){
            Toast toast = Toast.makeText(this, "password must not be empty", Toast.LENGTH_SHORT);
            toast.show();
        } else if (isPasswordSame(password, confirmPassword)){
            Toast toast = Toast.makeText(this, "password must be the same", Toast.LENGTH_SHORT);
            toast.show();
        } else if (!isPasswordSame(password, confirmPassword) && !isEmailEmpty(email) && !isPasswordEmpty(password)){
            signUp(email, password, name, surname, loginProgressBar);
        }
        loginProgressBar.setVisibility(View.INVISIBLE);
    }

    Boolean isPasswordEmpty(String password){
        return TextUtils.isEmpty(password);
    }

    Boolean isEmailEmpty(String email){
        return TextUtils.isEmpty(email);
    }

    Boolean isPasswordSame(String password, String confirmPassword){
        return !password.equals(confirmPassword);
    }

    Boolean checkName(EditText name){
        CharSequence str = name.getText();
        return TextUtils.isEmpty(str);
    }

    Boolean checkSurname(EditText surname){
        CharSequence str = surname.getText();
        return TextUtils.isEmpty(str);
    }

    Boolean checkPhone(EditText phone){
        CharSequence str = phone.getText();
        return TextUtils.isEmpty(str);
    }

    Boolean isValidEmail(EditText email){
        CharSequence str = email.getText();
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    Boolean isValidPhone(EditText phone){
        CharSequence str = phone.getText();
        return Patterns.PHONE.matcher(str).matches();
    }
}