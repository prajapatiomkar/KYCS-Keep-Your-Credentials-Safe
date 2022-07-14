package com.example.KeepYourCredentialsSafeMadeByOmkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private TextView login_link;
    private TextInputEditText register_email_id_field, register_password_field_1, register_password_field_2;
    private Button createBtn;
    private SignInButton sign_in_button_register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        register_email_id_field = findViewById(R.id.register_email_id_field);
        register_password_field_1 = findViewById(R.id.register_password_field_1);
        register_password_field_2 = findViewById(R.id.register_password_field_2);


        sign_in_button_register = findViewById(R.id.sign_in_button_register);
        sign_in_button_register.setOnClickListener((view -> {
            startActivity(new Intent(getApplicationContext(), GoogleSignInActivity.class));
            finish();
        }));

        login_link = findViewById(R.id.login_link);
        login_link.setOnClickListener((view) -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        createBtn = findViewById(R.id.createBtn);
        createBtn.setOnClickListener((view -> {

            String email_id_of_user = register_email_id_field.getText().toString();
            String password_of_user_1 = register_password_field_1.getText().toString();
            String password_of_user_2 = register_password_field_2.getText().toString();

            if (TextUtils.isEmpty(email_id_of_user)) {
                register_email_id_field.setError("Email cannot be empty");
                register_email_id_field.requestFocus();
            } else if (TextUtils.isEmpty(password_of_user_1)) {
                register_password_field_1.setError("Password cannot be empty");
                register_password_field_1.requestFocus();
            } else if (TextUtils.isEmpty(password_of_user_2)) {
                register_password_field_2.setError("Password cannot be empty");
                register_password_field_2.requestFocus();
            } else if (!password_of_user_1.equals(password_of_user_2)) {
                Toast.makeText(getApplicationContext(), "Password are not matching", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email_id_of_user, password_of_user_1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }));

    }
}