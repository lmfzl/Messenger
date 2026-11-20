package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private AppCompatButton buttonResetPassword;
    private ResetPasswordViewModel resetPasswordViewModel;
    private static final String EMAIL_STATUS = "Password reset instructions have been sent to your email";
    private static final String ERROR="Something went wrong";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        observeViewModel();
        setupClickListeners();

    }

    private void observeViewModel(){
        resetPasswordViewModel.getLetterSent().observe(this, letterSent -> {
            if (letterSent){
                Toast.makeText(ResetPasswordActivity.this, EMAIL_STATUS, Toast.LENGTH_SHORT).show();
                startActivity(LoginActivity.newIntent(ResetPasswordActivity.this));
                finish();
            }
        });
        resetPasswordViewModel.getError().observe(this, error ->{
            Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }
    private void setupClickListeners(){
        buttonResetPassword.setOnClickListener(v ->{
            resetPasswordViewModel.resetPassword(editTextEmail.getText().toString().trim());
        });
    }

    private void initViews(){
        editTextEmail=findViewById(R.id.editTextForgotPasswordEmail);
        buttonResetPassword=findViewById(R.id.buttonResetPassword);
    }
    public static Intent newIntent(Context context){
        return new Intent(context, ResetPasswordActivity.class);
    }
}