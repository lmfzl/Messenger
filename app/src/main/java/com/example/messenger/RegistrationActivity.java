package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private AppCompatButton buttonRegistry;
    private RegistrationViewModel registrationViewModel;
    private static final String SUCCESS = "Successful registration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, Math.max(systemBars.bottom, ime.bottom));
            return insets;
        });
        initViews();
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();
        setupClickListeners();

    }


    private void setupClickListeners() {
        buttonRegistry.setOnClickListener(v -> {
            String email = stringTrimmer(editTextEmail);
            String password = stringTrimmer(editTextPassword);
            String name = stringTrimmer(editTextName);
            String lastName = stringTrimmer(editTextLastName);
            int age = Integer.parseInt(editTextAge.getText().toString().trim());
            registrationViewModel.registerNewUser(email, password, name, lastName, age);
        });
    }

    private void observeViewModel() {
        registrationViewModel.getUser().observe(this, (regResult -> {
            if (regResult != null) {
                Toast.makeText(this, SUCCESS, Toast.LENGTH_SHORT).show();
                startActivity(UsersActivity.newIntent(this));
                finish();
            }
        }));
        registrationViewModel.getError().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextRegistrationEmail);
        editTextPassword = findViewById(R.id.editTextRegistrationPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        buttonRegistry = findViewById(R.id.buttonSignUp);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }

    private String stringTrimmer(TextView textView) {
        return textView.getText().toString().trim();
    }
}