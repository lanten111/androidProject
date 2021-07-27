package co.za.delivernow;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Button buttonRegister = (Button) findViewById(R.id.buttonRegister);

        EditText email = findViewById(R.id.RegisterEmailAddress);
        EditText password = findViewById(R.id.RegisterPassword);
        EditText confirmPassword = findViewById(R.id.RegisterConfirmPassword);
//        EditText name = findViewById(R.id.RegisterPersonName);
//        EditText surname = findViewById(R.id.RegisterSurname);
//        EditText phone = findViewById(R.id.RegisterPhone);
//        EditText deliveryLocation = findViewById(R.id.RegisterDeliveryLocation);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(email.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
            }
        });

    }

    private void signUp(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User registered Successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "registration failed, try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void register(String email, String password, String confirmPassword){

        if (isEmailEmpty(email)){
            Toast toast = Toast.makeText(this, "Email must not be empty", Toast.LENGTH_LONG);
            toast.show();
        } else if (isPasswordEmpty(password)){
            Toast toast = Toast.makeText(this, "password must not be empty", Toast.LENGTH_LONG);
            toast.show();
        } else if (isPasswordSame(password, confirmPassword)){
            Toast toast = Toast.makeText(this, "password must be the same", Toast.LENGTH_LONG);
            toast.show();
        } else if (!isPasswordSame(password, confirmPassword) && !isEmailEmpty(email) && !isPasswordEmpty(password)){
            signUp(email, password);
        }

//        else if (checkName(name)){
//            Toast toast = Toast.makeText(this, "Name must not be empty", Toast.LENGTH_LONG);
//            toast.show();
//        } else if (checkSurname(surname)){
//            Toast toast = Toast.makeText(this, "surname must not be empty", Toast.LENGTH_LONG);
//            toast.show();
//        } else if (checkPhone(phone)){
//            Toast toast = Toast.makeText(this, "Phone must not be empty", Toast.LENGTH_LONG);
//            toast.show();
//        } else if (isValidEmail(email)){
//            Toast toast = Toast.makeText(this, "Enter valid email", Toast.LENGTH_LONG);
//            toast.show();
//        } else if (isValidPhone(phone)){
//            Toast toast = Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_LONG);
//            toast.show();
//        }
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