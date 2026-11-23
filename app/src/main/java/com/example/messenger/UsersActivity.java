package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UsersActivity extends AppCompatActivity {
    private UsersViewModel usersViewModel;
    private UsersAdapter usersAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerViewUsers), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        recyclerView.setAdapter(usersAdapter);
        observeViewModel();
    }

    private void initViews(){
        recyclerView = findViewById(R.id.recyclerViewUsers);
        usersAdapter = new UsersAdapter();
    }

    private void observeViewModel(){
        usersViewModel.getUser().observe(this, firebaseUser -> {
            if (firebaseUser==null){
                startActivity(LoginActivity.newIntent(this));
                finish();
            }
        });
        usersViewModel.getUsers().observe(this, users -> {
            usersAdapter.setUserList(users);
        });
    }

    private void setupClickListeners(){

    }

    public static Intent newIntent(Context context){
        return new Intent(context, UsersActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.itemLogout){
            usersViewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }
}