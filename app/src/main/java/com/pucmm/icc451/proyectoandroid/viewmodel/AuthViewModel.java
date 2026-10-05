package com.pucmm.icc451.proyectoandroid.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.pucmm.icc451.proyectoandroid.repository.AuthRepository;

public class AuthViewModel extends ViewModel {
    private static final int MIN_PASSWORD_LENGTH = 6;
    private final AuthRepository repository;

    private final MutableLiveData<Boolean> authSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> authError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<String> passwordError = new MutableLiveData<>();
    private final MutableLiveData<String> nameError = new MutableLiveData<>();

    public AuthViewModel() {
        repository = new AuthRepository();
    }

    public LiveData<Boolean> getAuthSuccess() { return authSuccess; }
    public LiveData<String> getAuthError() { return authError; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getEmailError() { return emailError; }
    public LiveData<String> getPasswordError() { return passwordError; }
    public LiveData<String> getNameError() { return nameError; }

    public void register(String name, String email, String password) {
        if (!isValidRegisterForm(name, email, password)) {
            return;
        }

        isLoading.setValue(true);
        repository.register(name, email, password).addOnCompleteListener(task -> {
            isLoading.setValue(false);
            if (task.isSuccessful()) {
                FirebaseUser currentUser = repository.getCurrentUser();
                if (currentUser != null) {
                    repository.updateDeviceToken(currentUser.getUid());
                }

                authSuccess.setValue(true);
            } else {
                handleAuthError(task.getException());
            }
        });
    }

    public void login(String email, String password) {
        if (!isValidLoginForm(email, password)) {
            return;
        }

        isLoading.setValue(true);
        repository.login(email, password).addOnCompleteListener(task -> {
            isLoading.setValue(false);
            if (task.isSuccessful()) {
                FirebaseUser currentUser = repository.getCurrentUser();
                if (currentUser != null) {
                    repository.updateDeviceToken(currentUser.getUid());
                }

                authSuccess.setValue(true);
            } else {
                handleAuthError(task.getException());
            }
        });
    }

    private void handleAuthError(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            authError.setValue("La contraseña es muy débil. Usa al menos 6 caracteres.");
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            authError.setValue("El correo o la contraseña son incorrectos.");
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            authError.setValue("Este correo ya está registrado en el sistema.");
        } else {
            authError.setValue("Error de autenticación: Verifica tu conexión a internet.");
        }
    }

    private boolean isValidLoginForm(String email, String password) {
        boolean isValid = true;
        emailError.setValue(null);
        passwordError.setValue(null);

        if (email == null || email.trim().isEmpty()) {
            emailError.setValue("El correo es obligatorio.");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            emailError.setValue("Ingresa un correo electrónico válido.");
            isValid = false;
        }

        if (password == null || password.trim().isEmpty()) {
            passwordError.setValue("La contraseña es obligatoria.");
            isValid = false;
        } else if (password.trim().length() < MIN_PASSWORD_LENGTH) {
            passwordError.setValue("La contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres.");
            isValid = false;
        }

        return isValid;
    }

    private boolean isValidRegisterForm(String name, String email, String password) {
        boolean isNameValid = true;
        nameError.setValue(null);

        if (name == null || name.trim().isEmpty()) {
            nameError.setValue("El nombre es obligatorio.");
            isNameValid = false;
        }

        boolean isEmailAndPassValid = isValidLoginForm(email, password);

        return isNameValid && isEmailAndPassValid;
    }

    public void clearFormState() {
        nameError.setValue(null);
        emailError.setValue(null);
        passwordError.setValue(null);
        authError.setValue(null);
    }

    public FirebaseUser getCurrentUser() {
        return repository.getCurrentUser();
    }

    public void logout() {
        repository.logout();
        clearFormState();
    }

}
