package com.pucmm.icc451.proyectoandroid.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pucmm.icc451.proyectoandroid.model.Chat;
import com.pucmm.icc451.proyectoandroid.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatsListRepository {

    private static ChatsListRepository instance = null;
    private final MutableLiveData<List<Chat>> chatsLiveData = new MutableLiveData<>();
    private final FirebaseFirestore db;

    private ChatsListRepository() {
        db = FirebaseFirestore.getInstance();
        loadChats();
    }

    public static ChatsListRepository getInstance() {
        if (instance == null) {
            instance = new ChatsListRepository();
        }
        return instance;
    }

    public LiveData<List<Chat>> getChats() {
        return chatsLiveData;
    }

    private void loadChats() {
        User currentUser = UserRepository.getInstance().getCurrentUser();

        db.collection("Conversations")
                .whereArrayContains("participantIds", currentUser.getId())
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        Log.e("ChatsListRepository", "Error al escuchar las conversaciones", error);
                        return;
                    }

                    if (snapshots != null) {
                        List<Chat> chatList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Chat chat = doc.toObject(Chat.class);
                            chatList.add(chat);
                        }
                        chatsLiveData.setValue(chatList);
                    }
                });
    }
}