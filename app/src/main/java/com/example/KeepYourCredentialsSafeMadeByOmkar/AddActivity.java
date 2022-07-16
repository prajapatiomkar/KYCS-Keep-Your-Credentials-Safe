package com.example.KeepYourCredentialsSafeMadeByOmkar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class AddActivity extends AppCompatActivity {
    private TextInputEditText add_title_edit_text, add_account_edit_text, add_password_edit_text;
    private Button save_btn;
    private FirebaseAuth mAuth;
    SecurityPipeline securityPipeline;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        add_title_edit_text = findViewById(R.id.add_title_edit_text);
        add_account_edit_text = findViewById(R.id.add_account_edit_text);
        add_password_edit_text = findViewById(R.id.add_password_edit_text);
        securityPipeline = new SecurityPipeline();
        mAuth = FirebaseAuth.getInstance();

        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener((view -> {
            try {
                insertDate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeKeyboard();
        }));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void insertDate() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        Map<String, Object> map = new HashMap<>();
        map.put("title",securityPipeline.encrypt(add_title_edit_text.getText().toString(),mAuth.getUid()) );
        map.put("account",securityPipeline.encrypt(add_account_edit_text.getText().toString(),mAuth.getUid()));
        map.put("password",securityPipeline.encrypt(add_password_edit_text.getText().toString(),mAuth.getUid()));
        map.put("date",securityPipeline.encrypt(date,mAuth.getUid()));

        FirebaseDatabase.getInstance().getReference().child(mAuth.getUid()).push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();

        if (view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}