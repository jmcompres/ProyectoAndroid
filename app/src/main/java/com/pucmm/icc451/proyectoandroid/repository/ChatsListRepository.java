package com.pucmm.icc451.proyectoandroid.repository;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pucmm.icc451.proyectoandroid.model.Chat;
import com.pucmm.icc451.proyectoandroid.model.User;
import com.pucmm.icc451.proyectoandroid.util.ChatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsListRepository {

    private static ChatsListRepository instance = null;
    private final MutableLiveData<List<Chat>> chatsLiveData = new MutableLiveData<>();
    private final FirebaseFirestore db;
    private List<User> allUsersList = new ArrayList<>();
    private List<Chat> activeChatsList = new ArrayList<>();

    private ListenerRegistration usersListener;
    private ListenerRegistration chatsListener;

    private String myId;
    private String myName;

    private ChatsListRepository(String currentUserId, String currentUserName) {
        db = FirebaseFirestore.getInstance();
        this.myId = currentUserId;
        this.myName = currentUserName;
        loadUsersAndChats();
    }

    public static ChatsListRepository getInstance() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) return instance;

        String actualUid = firebaseUser.getUid();
        String actualName = firebaseUser.getDisplayName();

        if (instance == null || !instance.myId.equals(actualUid)) {

            if (instance != null) {
                instance.cleanup();
            }

            instance = new ChatsListRepository(actualUid, actualName);
        }
        return instance;
    }

    public void cleanup() {
        if (usersListener != null) usersListener.remove();
        if (chatsListener != null) chatsListener.remove();
    }

    public LiveData<List<Chat>> getChats() {
        return chatsLiveData;
    }

    private void loadUsersAndChats() {
        usersListener = db.collection("users").addSnapshotListener((snapshots, error) -> {
            if (error != null || snapshots == null) return;

            List<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot doc : snapshots) {
                User user = doc.toObject(User.class);
                if (user.getId() != null && !user.getId().equals(myId)) {
                    users.add(user);
                }
            }
            allUsersList = users;
            Log.d("DEBUG", "Total de usuarios = " + allUsersList.size());
            combineData();
        });

        chatsListener = db.collection("Chats")
                .whereArrayContains("participantIds", myId)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null) return;

                    List<Chat> chats = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        chats.add(doc.toObject(Chat.class));
                    }
                    activeChatsList = chats;
                    combineData();
                });
    }

    private void combineData() {
        List<Chat> finalChatsToShow = new ArrayList<>();

        for (User user : allUsersList) {
            Chat existingChat = findChatWithUser(user.getId());

            if (existingChat != null) {
                finalChatsToShow.add(existingChat);
            } else {
                String newChatId = ChatUtils.getChatId(myId, user.getId());

                Map<String, String> names = new HashMap<>();
                names.put(myId, myName);
                names.put(user.getId(), user.getName());

                Chat emptyChat = new Chat(
                        newChatId,
                        Arrays.asList(myId, user.getId()),
                        names,
                        "Toca para iniciar conversación",
                        "",
                        0,
                        0
                );
                finalChatsToShow.add(emptyChat);
            }
        }

        Collections.sort(finalChatsToShow, (c1, c2) ->
                Long.compare(c2.getLastMessageTimestamp(), c1.getLastMessageTimestamp())
        );

        chatsLiveData.setValue(finalChatsToShow);
    }

    private Chat findChatWithUser(String otherUserId) {
        for (Chat chat : activeChatsList) {
            if (chat.getParticipantIds().contains(otherUserId)) {
                return chat;
            }
        }
        return null;
    }
}