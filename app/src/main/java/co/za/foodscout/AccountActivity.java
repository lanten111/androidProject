package co.za.foodscout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import co.za.foodscout.Domain.Collections;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Utils.Utils;
import foodscout.R;

public class AccountActivity extends DrawerActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirestoreUser firestoreUser = new FirestoreUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_account, frameLayout);

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        }

        EditText email  = findViewById(R.id.accountEmailtxt);
        EditText phone = findViewById(R.id.accountNumbertxt);

        Button changePassword = findViewById(R.id.accountChangePasswordBtn);
        Button updateAccount = findViewById(R.id.accountUpdateAccountBtn);
        Button editAccount = findViewById(R.id.accountEditBtn);
        EditText name = findViewById(R.id.accountNametxt);
        EditText surname = findViewById(R.id.accountSurnametxt);
        EditText newPassword = findViewById(R.id.AccountpasswordOldTxt);

        ProgressBar loginProgressBar = findViewById(R.id.AccountProgressBar);
        loginProgressBar.setProgress(50);
        loginProgressBar.setVisibility(View.VISIBLE);

        name.setEnabled(false);
        surname.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
        updateAccount.setVisibility(View.INVISIBLE);

        if (isUserLoggedIn(firebaseAuth.getCurrentUser() )){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        } else {
            DocumentReference documentReference = db.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser() .getUid());
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                    if (firestoreUser != null){
                        name.setText(firestoreUser.getName());
                        surname.setText(firestoreUser.getSurname());
                        email.setText(firestoreUser.getEmail());
                        phone.setText(firestoreUser.getPhone());
                    }
                    loginProgressBar.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AccountActivity.this, "Something went wrong getting account details, please try again later", Toast.LENGTH_LONG).show();
                    loginProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        }

        editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setEnabled(true);
                surname.setEnabled(true);
//                email.setEnabled(true);
//                phone.setEnabled(true);
                updateAccount.setVisibility(View.VISIBLE);
                editAccount.setVisibility(View.INVISIBLE);
            }
        });

        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser(name.getText().toString(), surname.getText().toString(), firebaseAuth);
                name.setEnabled(false);
                surname.setEnabled(false);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.isEmpty(newPassword.getText().toString())){
                    if(!isUserLoggedIn(firebaseAuth.getCurrentUser() )){
                        firebaseAuth.getCurrentUser() .updatePassword(newPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AccountActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                newPassword.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                newPassword.setText("");
                            }
                        });
                    }else {
                        Toast.makeText(AccountActivity.this, "User not logged in, Please log in first", Toast.LENGTH_SHORT).show();
                        newPassword.setText("");
                    }
                } else{
                    Toast.makeText(AccountActivity.this, "new Password cant empty", Toast.LENGTH_SHORT).show();
                    newPassword.setText("");
                }
            }
        });
    }

    private void updateUser(String name, String surname, FirebaseAuth firebaseAuth){
        FirestoreUser firestoreUser = new FirestoreUser();
        if (!name.isEmpty()){
            firestoreUser.setName(name);
        }
        if (!surname.isEmpty()){
            firestoreUser.setSurname(surname);
        }
        if (!(firebaseAuth == null)){
            firestoreUser.setEmail(firebaseAuth.getCurrentUser().getEmail());
            firestoreUser.setPhone(firebaseAuth.getCurrentUser().getPhoneNumber());
            db.collection(Collections.user.name()).document(firebaseAuth.getCurrentUser().getUid()).set(firestoreUser).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(AccountActivity.this, "Details Updated Successful", Toast.LENGTH_SHORT).show();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isUserLoggedIn(FirebaseUser firebaseUser){
        return firebaseUser == null;
    }

}