package co.za.foodscout.activities.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Enum.Role;
import co.za.foodscout.Utils.Utils;
import co.za.foodscout.activities.MapActivity;
import foodscout.R;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();
    TextInputLayout phoneInputLayout;
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;
    TextInputLayout passwordConfirmInputLayout;
    TextInputLayout nameInputLayout;
    TextInputLayout surnameInputLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        EditText email = findViewById(R.id.RegisterEmailAddress);
        EditText password = findViewById(R.id.RegisterPassword);
        EditText confirmPassword = findViewById(R.id.RegisterConfirmPassword);
        EditText name = findViewById(R.id.RegisterPersonName);
        EditText surname = findViewById(R.id.RegisterSurname);
        EditText phone = findViewById(R.id.RegisterPhone);
        ProgressBar loginProgressBar = findViewById(R.id.registerProgressBar);

        phoneInputLayout = findViewById(R.id.textInputLayout12);
        emailInputLayout = findViewById(R.id.textInputLayout7);
        passwordInputLayout = findViewById(R.id.textInputLayout10);
        passwordConfirmInputLayout = findViewById(R.id.textInputLayout6);
        nameInputLayout = findViewById(R.id.textInputLayout8);
        surnameInputLayout = findViewById(R.id.textInputLayout9);

        loginProgressBar.setVisibility(View.INVISIBLE);
        loginProgressBar.setProgress(50);

        phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                phoneInputLayout.setError(null);
                return false;

            }
        });

        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                emailInputLayout.setError(null);
                return false;

            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                passwordInputLayout.setError(null);
                return false;

            }
        });

        confirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                passwordConfirmInputLayout.setError(null);
                return false;

            }
        });

        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                nameInputLayout.setError(null);
                return false;

            }
        });

        surname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                surnameInputLayout.setError(null);
                return false;

            }
        });

        name.setText("bruce");
        surname.setText("wayne");
        password.setText("123456");
        confirmPassword.setText("123456");
        email.setText("1@123.co.za");
        phone.setText("0659599252");


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(email.getText().toString(), password.getText().toString(),
                        confirmPassword.getText().toString(), name.getText().toString(),
                        surname.getText().toString(),phone.getText().toString(),  loginProgressBar);
            }
        });

    }

    private void signUp(String email, String password, String name, String surname,String phone, ProgressBar loginProgressBar){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firestoreUser.setName(name);
                        firestoreUser.setSurname(surname);
                        firestoreUser.setEmail(email);
                        firestoreUser.setDateCreated(new Date());
                        firestoreUser.setRole(Role.USER);
                        firestoreUser.setId(firebaseAuth.getCurrentUser().getUid());
                        firestoreUser.setPhone(formatPhoneNumber(phone));
                        db.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).set(firestoreUser).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(RegisterActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(RegisterActivity.this, "User registered Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                                        loginProgressBar.setVisibility(View.INVISIBLE);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        loginProgressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loginProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loginProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
        });
    }

    void register(String email, String password, String confirmPassword, String name, String surname, String phone, ProgressBar loginProgressBar){

        if (isEmailEmpty(email)){
            emailInputLayout.setError("Email must not be empty");
        } if (!isValidEmail(email)){
            emailInputLayout.setError("Email badly formatted");
        } if (isPasswordEmpty(password)){
            passwordInputLayout.setError("password must not be empty");
        } if (isPasswordSame(password, confirmPassword)){
            passwordInputLayout.setError("password must be the same");
            passwordConfirmInputLayout.setError("password must be the same");
        }if (!isValidPhone(phone)){
            phoneInputLayout.setError("Please enter valid phone number");
        } if (Utils.isEmpty(name)){
            nameInputLayout.setError("name must not be empty");
        }if (Utils.isEmpty(surname)) {
            surnameInputLayout.setError("surname must not be empty");
        } if (Utils.isEmpty(phone)) {
            phoneInputLayout.setError("phone must not be empty");
        } else if (!isEmailEmpty(email) && isValidEmail(email) && !isPasswordEmpty(password) && isValidPhone(phone)
                    && !Utils.isEmpty(name) && !Utils.isEmpty(surname) && !Utils.isEmpty(phone) && !isPasswordSame(password, confirmPassword)){
            loginProgressBar.setVisibility(View.VISIBLE);
            signUp(email, password, name, surname, phone, loginProgressBar);
        }

    }

    String formatPhoneNumber(String phone){
       return phone.replace(phone.substring(0, 1), "+27");
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

    Boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    Boolean isValidPhone(String phone){
        return Patterns.PHONE.matcher(phone).matches();
    }
}