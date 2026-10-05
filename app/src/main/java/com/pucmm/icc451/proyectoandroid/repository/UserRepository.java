package com.pucmm.icc451.proyectoandroid.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pucmm.icc451.proyectoandroid.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static UserRepository instance;
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    private UserRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public LiveData<List<User>> getAllUsers(String currentUserId) {
        MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();

        db.collection("users").addSnapshotListener((snapshots, error) -> {
            if (error != null || snapshots == null) return;

            List<User> userList = new ArrayList<>();
            for (QueryDocumentSnapshot doc : snapshots) {
                User user = doc.toObject(User.class);
                if (user.getId() != null && !user.getId().equals(currentUserId)) {
                    userList.add(user);
                }
            }
            usersLiveData.setValue(userList);
        });
        return usersLiveData;
    }

    public User getCurrentUser() {
        User currentUser = new User();
        FirebaseUser fbUser = auth.getCurrentUser();
        if (fbUser == null) {
            currentUser.setId("0");
            currentUser.setName("SIN USUARIO");
            currentUser.setEmail("nada@nada.com");
        }
        else {
            currentUser.setId(fbUser.getUid());
            currentUser.setName(fbUser.getDisplayName());
            currentUser.setEmail(fbUser.getEmail());
        }

        return currentUser;
    }
}