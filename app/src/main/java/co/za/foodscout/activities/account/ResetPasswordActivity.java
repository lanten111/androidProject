package co.za.foodscout.activities.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import co.za.foodscout.Utils.Utils;
import foodscout.R;

public class ResetPasswordActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        }

        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        EditText resetPasswordEmail = findViewById(R.id.resetPasswordEmail);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.isEmpty(resetPasswordEmail.getText().toString())){
                    firebaseAuth.sendPasswordResetEmail(resetPasswordEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ResetPasswordActivity.this, "Reset link has been sent to your email", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ResetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Enter correct email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        veryfyCodeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!Utils.isEmpty(verifyCodeText.getText().toString())){
//                    firebaseAuth.verifyPasswordResetCode(verifyCodeText.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
//                        @Override
//                        public void onSuccess(String s) {
//                            verifyCodeText.setVisibility(View.INVISIBLE);
//                            veryfyCodeButton.setVisibility(View.INVISIBLE);
//                            resetPassword.setVisibility(View.VISIBLE);
//                            resetConfirmPassword.setVisibility(View.VISIBLE);
//                            changePassword.setVisibility(View.VISIBLE);
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ResetPasswordActivity.this, "varification failed, please enter correct code", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(ResetPasswordActivity.this, "Enter code you received on email", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }
}