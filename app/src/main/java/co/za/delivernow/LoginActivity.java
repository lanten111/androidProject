package co.za.delivernow;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

import co.za.delivernow.Domain.FirestoreUser;
import co.za.delivernow.Domain.Role;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    String verificationId;
    String otpCode;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        TextView registerButton = findViewById(R.id.buttonRegisterLogin);
        Button emailLoginButton = findViewById(R.id.LoginEmailButton);
        Button phoneLoginButton = findViewById(R.id.loginPhoneButton);
        Button signInWithEmail = findViewById(R.id.loginEmailPassword);
        Button signInWithPhone = findViewById(R.id.loginPhone);
        Button verifyOpt = findViewById(R.id.loginVeryfyCodeBtn);
        EditText otpUserCode = findViewById(R.id.loginVerifyCodeTxt);
        EditText email = findViewById(R.id.userEmail);
        EditText password = findViewById(R.id.password);
        EditText phone = findViewById(R.id.loginPhoneTxt);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        ProgressBar loginProgressBar = findViewById(R.id.loginProgressBar);
        TextInputLayout phoneLayout = findViewById(R.id.LogintextInputLayoutPhone);
        TextInputLayout otpUserCodeLayout = findViewById(R.id.LogintextInputLayoutCode);
        TextInputLayout emailLayout = findViewById(R.id.LogintextInputLayoutEmail);
        TextInputLayout passwordCodeLayout = findViewById(R.id.LogintextInputLayoutPassword);

        loginProgressBar.setVisibility(View.INVISIBLE);
        loginProgressBar.setProgress(50);

        phoneLoginButton.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);
        phoneLayout.setVisibility(View.INVISIBLE);
        signInWithEmail.setVisibility(View.INVISIBLE);
        verifyOpt.setVisibility(View.INVISIBLE);
        otpUserCode.setVisibility(View.INVISIBLE);
        otpUserCodeLayout.setVisibility(View.INVISIBLE);

        phone.setText("+27659599252");
        email.setText("dev@1234.co.za");
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
                login(email.getText().toString(), password.getText().toString());
                loginProgressBar.setVisibility(View.INVISIBLE);
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
                                        verificationId = s;
                                        otpUserCode.setVisibility(View.VISIBLE);
                                        otpUserCodeLayout.setVisibility(View.VISIBLE);
                                        verifyOpt.setVisibility(View.VISIBLE);
                                        phoneLoginButton.setVisibility(View.INVISIBLE);
                                        phone.setVisibility(View.INVISIBLE);
                                        phoneLayout.setVisibility(View.INVISIBLE);
                                        loginProgressBar.setVisibility(View.INVISIBLE);
                                        phoneLoginButton.setClickable(true);
                                        signInWithEmail.setClickable(true);
                                        registerButton.setClickable(true);
                                    }
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        //auto et OTP
                                        otpUserCode.setText(phoneAuthCredential.getSmsCode());
                                        phoneSignin(phoneAuthCredential);
                                        otpUserCode.setVisibility(View.VISIBLE);
                                        otpUserCodeLayout.setVisibility(View.VISIBLE);
                                        verifyOpt.setVisibility(View.VISIBLE);
                                        phoneLoginButton.setVisibility(View.INVISIBLE);
                                        phone.setVisibility(View.INVISIBLE);
                                        phoneLayout.setVisibility(View.INVISIBLE);
                                        loginProgressBar.setVisibility(View.INVISIBLE);
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
                    phoneSignin(phoneAuthCredential);
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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void phoneSignin(PhoneAuthCredential phoneAuthCredential){
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                        if (firestoreUser.getRole().equals(Role.DRIVER)){
                            Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), DriverActivity.class);
                            startActivity(intent);
                        } else{
                            Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "e", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void emailSignIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
           .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                        if (firestoreUser.getRole().equals(Role.DRIVER)){
                            Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), DeliveriesActivity.class);
                            startActivity(intent);
                        } else{
                            Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "e", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    void login(String email, String password){
        if (checkEmail(email)) {
            Toast.makeText(this, "email must not be empty", Toast.LENGTH_SHORT).show();
        } else if (checkPassword(password)){
            Toast.makeText(this, "password must not be empty", Toast.LENGTH_SHORT).show();
        } else if ( !checkEmail(email) && !checkPassword(password)         ){
            emailSignIn(email, password);
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

//    private void updateUser(String uid){
//        if (na)
//        firestoreUser.setName();
//        db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(firestoreUser).addOnSuccessListener(new OnSuccessListener() {
//            @Override
//            public void onSuccess(Object o) {
//                Toast.makeText(LoginActivity.this, "Details Updated Successful", Toast.LENGTH_LONG).show();
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }

}