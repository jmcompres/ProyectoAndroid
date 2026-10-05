package com.pucmm.icc451.proyectoandroid.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.pucmm.icc451.proyectoandroid.R;
import com.pucmm.icc451.proyectoandroid.databinding.ActivityLoginBinding;
import com.pucmm.icc451.proyectoandroid.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        if (authViewModel.getCurrentUser() != null) {
            Intent intent = new Intent(this, ChatsListActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setupSignUpLink();
        setupObservers();
        setupClickListener();


        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupSignUpLink(){
        String fullText = binding.lblSignUpLink.getText().toString();
        String clickableText = getString(R.string.lblSignUpClick);

        SpannableString spannableString = new SpannableString(fullText);

        int startIndex = fullText.indexOf(clickableText);
        int endIndex = startIndex + clickableText.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                binding.txtEmail.setText("");
                binding.txtPassword.setText("");

                authViewModel.clearFormState();

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(LoginActivity.this, R.color.btn_dark));
                ds.setTypeface(Typeface.DEFAULT_BOLD);
            }
        };

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.lblSignUpLink.setText(spannableString);
        binding.lblSignUpLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setupObservers() {
        authViewModel.getIsLoading().observe(this, isLoading -> {
            binding.btnLogin.setEnabled(!isLoading);
        });

        authViewModel.getAuthError().observe(this, errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        authViewModel.getAuthSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, ChatsListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        authViewModel.getEmailError().observe(this, errorMsg -> {
            binding.ilEmail.setError(errorMsg);
        });

        authViewModel.getPasswordError().observe(this, errorMsg -> {
            binding.ilPassword.setError(errorMsg);
        });
    }

    private void setupClickListener() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.txtEmail.getText().toString().trim();
            String password = binding.txtPassword.getText().toString().trim();

            authViewModel.login(email, password);
        });
    }
}