package com.example.KeepYourCredentialsSafeMadeByOmkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        list_view = findViewById(R.id.list_view);

        List<Credential> list = new ArrayList<>();
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));
        list.add(new Credential("Google", "prajpatiomkar01@gmail.com", "*********","02/07/22"));



        CredentialAdapter credentialAdapter = new CredentialAdapter(getApplicationContext(), list);
        list_view.setAdapter(credentialAdapter);
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