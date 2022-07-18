package com.example.KeepYourCredentialsSafeMadeByOmkar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class HomeActivity extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    private FloatingActionButton floating_btn;
    private RecyclerView recyclerView;
    private CredentialAdapter credentialAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Credential> options =
                new FirebaseRecyclerOptions.Builder<Credential>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(mAuth.getUid()), Credential.class)
                        .build();
        credentialAdapter = new CredentialAdapter(options);
        recyclerView.setAdapter(credentialAdapter);

        floating_btn = findViewById(R.id.floating_btn);
        floating_btn.setOnClickListener((view -> {
            startActivity(new Intent(getApplicationContext(), AddActivity.class));
            finish();
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        credentialAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        credentialAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.signOut:
                AuthUI.getInstance().signOut(this);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}