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

public class LoginActivity extends AppCompatActivity {
    private TextView register_link;
    private TextInputEditText login_email_id_field, login_password_field;
    private Button loginBtn;
    private SignInButton sign_in_button_login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        login_email_id_field = findViewById(R.id.login_email_id_field);
        login_password_field = findViewById(R.id.login_password_field);


        sign_in_button_login = findViewById(R.id.sign_in_button_login);
        sign_in_button_login.setOnClickListener((view -> {
            startActivity(new Intent(getApplicationContext(), GoogleSignInActivity.class));
            finish();
        }));

        register_link = findViewById(R.id.register_link);
        register_link.setOnClickListener((view -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        }));

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener((view -> {
            String email_id_of_user = login_email_id_field.getText().toString();
            String password_of_user = login_password_field.getText().toString();

            if (TextUtils.isEmpty(email_id_of_user)) {
                login_email_id_field.setError("Email cannot be empty");
                login_email_id_field.requestFocus();
            } else if (TextUtils.isEmpty(password_of_user)) {
                login_password_field.setError("Password cannot be empty");
                login_password_field.requestFocus();
            } else {
                mAuth.signInWithEmailAndPassword(email_id_of_user, password_of_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "user logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }));
    }
}