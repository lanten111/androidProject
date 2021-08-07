package co.za.delivernow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;

import co.za.delivernow.Utils.Utils;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accout);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        EditText name = findViewById(R.id.accountNametxt);
        EditText surname = findViewById(R.id.accountSurnametxt);
        EditText email  = findViewById(R.id.accountEmailtxt);
        EditText oldPassword = findViewById(R.id.accountOldPsswordTxt);
        EditText newPassword = findViewById(R.id.accountNewPasswordTxt);

        Button changePasswordBt = findViewById(R.id.accountChangePasswordBtn);

        if (isUserLoggedIn(user)){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        } else {
            name.setText(user.getDisplayName());
            surname.setText("something");
            email.setText(user.getEmail());
        }

        changePasswordBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isEmpty(oldPassword.getText().toString()) || Utils.isEmpty(newPassword.getText().toString())){
                    if(!isUserLoggedIn(user)){
                        user.updatePassword(newPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AccountActivity.this, "Password changed successfully", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        Toast.makeText(AccountActivity.this, "User not logged in, Please log in first", Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(AccountActivity.this, "old password/ new Password empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isUserLoggedIn(FirebaseUser firebaseUser){
        return firebaseUser == null;
    }

}