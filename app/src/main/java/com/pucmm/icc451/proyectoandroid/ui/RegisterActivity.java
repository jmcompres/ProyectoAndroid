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
import com.pucmm.icc451.proyectoandroid.databinding.ActivityRegisterBinding;
import com.pucmm.icc451.proyectoandroid.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        if (authViewModel.getCurrentUser() != null) {
            Intent intent = new Intent(this, ChatsListActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setupSignInLink();
        setupObservers();
        setupClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupSignInLink() {
        String fullText = binding.lblSignInLink.getText().toString();
        String clickableText = getString(R.string.lblSignInClick);

        SpannableString spannableString = new SpannableString(fullText);

        int startIndex = fullText.indexOf(clickableText);
        int endIndex = startIndex + clickableText.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                binding.txtSignUpEmail.setText("");
                binding.txtSignUpPassword.setText("");

                authViewModel.clearFormState();

                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(RegisterActivity.this, R.color.btn_dark));
                ds.setTypeface(Typeface.DEFAULT_BOLD);
            }
        };

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.lblSignInLink.setText(spannableString);
        binding.lblSignInLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setupObservers() {
        authViewModel.getIsLoading().observe(this, isLoading -> {
            binding.btnCreateAccount.setEnabled(!isLoading);
        });

        authViewModel.getAuthError().observe(this, errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        authViewModel.getAuthSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, ChatsListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        authViewModel.getNameError().observe(this, errorMsg -> {
            binding.ilName.setError(errorMsg);
        });

        authViewModel.getEmailError().observe(this, errorMsg -> {
            binding.ilSignUpEmail.setError(errorMsg);
        });

        authViewModel.getPasswordError().observe(this, errorMsg -> {
            binding.ilSignUpPassword.setError(errorMsg);
        });
    }

    private void setupClickListener() {
        binding.btnCreateAccount.setOnClickListener(v -> {
            String name = binding.txtName.getText().toString().trim();
            String email = binding.txtSignUpEmail.getText().toString().trim();
            String password = binding.txtSignUpPassword.getText().toString().trim();

            authViewModel.register(name, email, password);
        });
    }
}