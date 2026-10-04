package com.pucmm.icc451.proyectoandroid.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthRepository {
    private final FirebaseAuth firebaseAuth;

    public AuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> register(String name, String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            FirebaseUser user = authResult.getUser();
            if (user != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                user.updateProfile(profileUpdates);
            }
        });
    }

    public Task<AuthResult> login(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
}
