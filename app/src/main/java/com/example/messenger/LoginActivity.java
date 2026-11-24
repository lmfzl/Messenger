package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

public class LoginActivity extends AppCompatActivity {
    boolean imeWasVisible = false;

    private TextView editTextEmail;
    private TextView editTextPassword;
    private AppCompatButton buttonLogin;
    private TextView textViewForgotPassword;
    private TextView textViewRegister;
    private LoginViewModel loginViewModel;
    private static final String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_login);
        androidx.core.widget.NestedScrollView scrollView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(scrollView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomInset = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomInset);
            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            if (imeVisible && !imeWasVisible) {
                android.view.View focused = scrollView.findFocus();
                if (focused != null) {
                    focused.post(() -> scrollView.smoothScrollTo(0, focused.getTop()));
                }
            }
            imeWasVisible = imeVisible;
            return insets;
        });
        initViews();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        observeViewModel();
        setupClickListeners();
    }


    private void observeViewModel(){
        loginViewModel.getError().observe(this, error ->{
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
        });
        loginViewModel.getUser().observe(this, firebaseUser -> {
            if (firebaseUser!=null){
                Toast.makeText(LoginActivity.this, "Authorized", Toast.LENGTH_SHORT).show();
                startActivity(UsersActivity.newIntent(LoginActivity.this, firebaseUser.getUid()));
                finish();
            }
        });
    }

    private void setupClickListeners(){
        textViewRegister.setOnClickListener(v ->
                startActivity(RegistrationActivity.newIntent(LoginActivity.this)));

        textViewForgotPassword.setOnClickListener(v ->
                startActivity(ResetPasswordActivity.newIntent(LoginActivity.this)));

        buttonLogin.setOnClickListener(v ->
            loginViewModel.login(stringTrimmer(editTextEmail), stringTrimmer(editTextPassword)));
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewRegister = findViewById(R.id.textViewRegister);
    }

    private String stringTrimmer(TextView textView){
        return textView.getText().toString().trim();
    }

    public static Intent newIntent(Context context){
        return new Intent(context, LoginActivity.class);
    }


}