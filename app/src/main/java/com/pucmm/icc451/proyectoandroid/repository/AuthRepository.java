package com.pucmm.icc451.proyectoandroid.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pucmm.icc451.proyectoandroid.model.User;

public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;

    public AuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }
    public Task<Void> register(String name, String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser != null) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        firebaseUser.updateProfile(profileUpdates);

                        User newUser = new User(firebaseUser.getUid(), name, email);

                        return db.collection("users")
                                .document(firebaseUser.getUid())
                                .set(newUser);
                    }
                    return null;
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
