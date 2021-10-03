package co.za.foodscout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import co.za.foodscout.Domain.Collections;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Role;
import foodscout.R;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    String verificationId;
    String otpCode;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();
    EditText surname;
    EditText name;
    TextInputLayout nameLayout;
    TextInputLayout surnameCodeLayout;
    Button addDetails;
    Button verifyOpt;
    EditText otpUserCode;
    TextInputLayout otpUserCodeLayout;
    Button signInWithEmail;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        if (firebaseAuth.getCurrentUser() == null){
//            startActivity(new Intent(this, LoginActivity.class));
//            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
//        }

        ProgressBar loginProgressBar = findViewById(R.id.loginProgressBar);
        TextView registerButton = findViewById(R.id.buttonRegisterLogin);
        Button emailLoginButton = findViewById(R.id.LoginEmailButton);
        Button phoneLoginButton = findViewById(R.id.loginPhoneButton);
        signInWithEmail = findViewById(R.id.loginEmailPassword);
        Button signInWithPhone = findViewById(R.id.loginPhone);
        verifyOpt = findViewById(R.id.loginVeryfyCodeBtn);
        addDetails = findViewById(R.id.loginAddUserdetailsBtn);
        otpUserCode = findViewById(R.id.loginVerifyCodeTxt);
        EditText email = findViewById(R.id.userEmail);
        EditText password = findViewById(R.id.password);
        EditText phone = findViewById(R.id.loginPhoneTxt);
        name = findViewById(R.id.phoneLoginname);
        surname = findViewById(R.id.phoneLoginSurname);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextInputLayout phoneLayout = findViewById(R.id.LogintextInputLayoutPhone);
        otpUserCodeLayout = findViewById(R.id.LogintextInputLayoutCode);
        TextInputLayout emailLayout = findViewById(R.id.LogintextInputLayoutEmail);
        TextInputLayout passwordCodeLayout = findViewById(R.id.LogintextInputLayoutPassword);
        nameLayout = findViewById(R.id.LogintextInputLayoutName);
        surnameCodeLayout = findViewById(R.id.LogintextInputLayoutPhoneSurname);

        loginProgressBar.setVisibility(View.INVISIBLE);
        loginProgressBar.setProgress(50);

        name.setVisibility(View.INVISIBLE);
        surname.setVisibility(View.INVISIBLE);
        nameLayout.setVisibility(View.INVISIBLE);
        surnameCodeLayout.setVisibility(View.INVISIBLE);
        phoneLoginButton.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);
        phoneLayout.setVisibility(View.INVISIBLE);
        signInWithEmail.setVisibility(View.INVISIBLE);
        verifyOpt.setVisibility(View.INVISIBLE);
        otpUserCode.setVisibility(View.INVISIBLE);
        otpUserCodeLayout.setVisibility(View.INVISIBLE);
        addDetails.setVisibility(View.INVISIBLE);

        phone.setText("+27659599252");
        email.setText("user2@123.co.za");
        password.setText("123456");


        signInWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setVisibility(View.INVISIBLE);
                emailLayout.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
                passwordCodeLayout.setVisibility(View.INVISIBLE);
                emailLoginButton.setVisibility(View.INVISIBLE);
                signInWithPhone.setVisibility(View.INVISIBLE);
                phoneLoginButton.setVisibility(View.VISIBLE);
                phone.setVisibility(View.VISIBLE);
                phoneLayout.setVisibility(View.VISIBLE);
                signInWithEmail.setVisibility(View.VISIBLE);
                forgotPassword.setVisibility(View.INVISIBLE);
            }
        });

        signInWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setVisibility(View.VISIBLE);
                emailLayout.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                passwordCodeLayout.setVisibility(View.VISIBLE);
                emailLoginButton.setVisibility(View.VISIBLE);
                signInWithPhone.setVisibility(View.VISIBLE);
                phoneLoginButton.setVisibility(View.INVISIBLE);
                phone.setVisibility(View.INVISIBLE);
                phoneLayout.setVisibility(View.INVISIBLE);
                signInWithEmail.setVisibility(View.INVISIBLE);
                forgotPassword.setVisibility(View.VISIBLE);
            }
        });

        emailLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgressBar.setVisibility(View.VISIBLE);
                login(email.getText().toString(), password.getText().toString(), loginProgressBar);
            }
        });

        phoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneLoginButton.setClickable(false);
                loginProgressBar.setVisibility(View.VISIBLE);
                signInWithEmail.setClickable(false);
                registerButton.setClickable(false);
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(firebaseAuth)
                                .setPhoneNumber(phone.getText().toString())       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Time and unit
                                .setActivity(LoginActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        Toast.makeText(LoginActivity.this, "Waiting to get otp code for you", Toast.LENGTH_SHORT).show();
                                        verificationId = s;
                                        otpUserCode.setVisibility(View.VISIBLE);
                                        otpUserCodeLayout.setVisibility(View.VISIBLE);
                                        verifyOpt.setVisibility(View.VISIBLE);
                                        phoneLoginButton.setVisibility(View.INVISIBLE);
                                        phone.setVisibility(View.INVISIBLE);
                                        phoneLayout.setVisibility(View.INVISIBLE);
                                        phoneLoginButton.setClickable(true);
                                        signInWithEmail.setClickable(true);
                                        registerButton.setClickable(true);
                                        loginProgressBar.setVisibility(View.INVISIBLE);
                                    }
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        //auto et OTP
                                        Toast.makeText(LoginActivity.this, "Code verified successfully", Toast.LENGTH_SHORT).show();
                                        loginProgressBar.setVisibility(View.INVISIBLE);
                                        otpUserCode.setText(phoneAuthCredential.getSmsCode());
                                        phoneSignIn(phoneAuthCredential, loginProgressBar);
                                        otpUserCode.setVisibility(View.VISIBLE);
                                        otpUserCodeLayout.setVisibility(View.VISIBLE);
                                        verifyOpt.setVisibility(View.VISIBLE);
                                        phoneLoginButton.setVisibility(View.INVISIBLE);
                                        phone.setVisibility(View.INVISIBLE);
                                        phoneLayout.setVisibility(View.INVISIBLE);
                                        phoneLoginButton.setClickable(true);
                                        signInWithEmail.setClickable(true);
                                        registerButton.setClickable(true);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        phoneLoginButton.setVisibility(View.VISIBLE);
                                        phone.setVisibility(View.VISIBLE);
                                        phoneLayout.setVisibility(View.VISIBLE);
                                        verifyOpt.setVisibility(View.INVISIBLE);
                                        otpUserCode.setVisibility(View.INVISIBLE);
                                        loginProgressBar.setVisibility(View.INVISIBLE);
                                        phoneLoginButton.setClickable(true);
                                        signInWithEmail.setClickable(true);
                                        registerButton.setClickable(true);
                                    }
                                }).build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        verifyOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpUserCodeStr = otpUserCode.getText().toString();
                if (!otpUserCodeStr.isEmpty()){
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, otpUserCodeStr);
                    phoneSignIn(phoneAuthCredential, loginProgressBar);
                } else {
                    Toast.makeText(LoginActivity.this, "OTP Code cannot be empty", Toast.LENGTH_SHORT).show();
                }
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

        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirestoreUser firestoreUser1 = new FirestoreUser();
                loginProgressBar.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Please add name and surname", Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(name.getText().toString()) && TextUtils.isEmpty(surname.getText().toString()) ){
                    Toast.makeText(LoginActivity.this, "name or surname can not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    firestoreUser1.setName(name.getText().toString());
                    firestoreUser1.setSurname(surname.getText().toString());
                }
                firestoreUser1.setEmail(firebaseAuth.getCurrentUser().getEmail());
                firestoreUser1.setDateCreated(new Date());
                firestoreUser1.setRole(Role.USER);
                firestoreUser1.setId(firebaseAuth.getCurrentUser().getUid());
                firestoreUser1.setPhone(firebaseAuth.getCurrentUser().getPhoneNumber());
                db.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).set(firestoreUser1).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(LoginActivity.this, "User details created Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MapActivity.class));
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }


    private void phoneSignIn(PhoneAuthCredential phoneAuthCredential, ProgressBar progressBar){
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                checkUserRole(progressBar, firebaseAuth.getCurrentUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void emailSignIn(String email, String password, ProgressBar progressBar){
        firebaseAuth.signInWithEmailAndPassword(email, password)
           .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                checkUserRole(progressBar, firebaseAuth.getCurrentUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    void checkUserRole(ProgressBar progressBar, String userId){
        db.collection(Collections.user.name()).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                if (firestoreUser == null){
                    otpUserCode.setVisibility(View.INVISIBLE);
                    otpUserCodeLayout.setVisibility(View.INVISIBLE);
                    verifyOpt.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.VISIBLE);
                    surname.setVisibility(View.VISIBLE);
                    nameLayout.setVisibility(View.VISIBLE);
                    surnameCodeLayout.setVisibility(View.VISIBLE);
                    addDetails.setVisibility(View.VISIBLE);
                    signInWithEmail.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (firestoreUser.getRole().equals(Role.DRIVER)){
//                        Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DeliveryDetailsActivity.class);
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(intent);
                    } else if(firestoreUser.getRole().equals(Role.SELLER)) {
//                        Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SellerViewActivity.class);
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(intent);
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                        db.collection(Collections.delivery.name()).whereEqualTo("userId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<FirestoreDelivery> firestoreDeliveryList = new ArrayList<>();
                                firestoreDeliveryList = queryDocumentSnapshots.toObjects(FirestoreDelivery.class);
                                if (firestoreDeliveryList.size() > 0){
                                    Toast.makeText(LoginActivity.this, "View pending Order", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, UserOrderViewActivity.class));
                                } else {
                                    if (firestoreUser.getLocation() !=null){
                                        startActivity(new Intent(getApplicationContext(), RetailsActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please choose location for delivery", Toast.LENGTH_SHORT).show();
                                        startActivity( new Intent(getApplicationContext(), MapActivity.class));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    void login(String email, String password, ProgressBar progressBar){
        if (checkEmail(email)) {
            Toast.makeText(this, "email must not be empty", Toast.LENGTH_SHORT).show();
        } else if (checkPassword(password)){
            Toast.makeText(this, "password must not be empty", Toast.LENGTH_SHORT).show();
        } else if ( !checkEmail(email) && !checkPassword(password)         ){
            emailSignIn(email, password, progressBar);
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

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }



}