package com.example.KeepYourCredentialsSafeMadeByOmkar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    private TextInputEditText add_title_edit_text, add_account_edit_text, add_password_edit_text;
    private Button save_btn;



    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        add_title_edit_text = findViewById(R.id.add_title_edit_text);
        add_account_edit_text = findViewById(R.id.add_account_edit_text);
        add_password_edit_text = findViewById(R.id.add_password_edit_text);


        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener((view -> {
            insertDate();
        }));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void insertDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        Map<String, Object> map = new HashMap<>();
        map.put("title", add_title_edit_text.getText().toString());
        map.put("account", add_account_edit_text.getText().toString());
        map.put("password", add_password_edit_text.getText().toString());
        map.put("date", date);

        FirebaseDatabase.getInstance().getReference().child("users").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
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
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
}