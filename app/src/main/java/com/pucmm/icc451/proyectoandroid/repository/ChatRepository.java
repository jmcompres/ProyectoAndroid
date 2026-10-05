package com.pucmm.icc451.proyectoandroid.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.pucmm.icc451.proyectoandroid.model.Message;
import com.pucmm.icc451.proyectoandroid.util.ChatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRepository {

    private static ChatRepository instance = null;
    private final FirebaseFirestore db;
    private final Map<String, MutableLiveData<List<Message>>> chatListeners = new HashMap<>();

    private ChatRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public static ChatRepository getInstance() {
        if (instance==null) {
            instance = new ChatRepository();
        }
        return instance;
    }

    public LiveData<List<Message>> getMessages(String chatId) {
        if (!chatListeners.containsKey(chatId)) {
            MutableLiveData<List<Message>> liveData = new MutableLiveData<>();
            chatListeners.put(chatId, liveData);

            db.collection("Chats")
                    .document(chatId)
                    .collection("Messages")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener((snapshots, error) -> {
                        if (error != null) {
                            Log.e("ChatRepository", "Error escuchando mensajes", error);
                            return;
                        }

                        if (snapshots != null) {
                            List<Message> messages = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : snapshots) {
                                Message msg = doc.toObject(Message.class);
                                messages.add(msg);
                            }
                            liveData.setValue(messages);
                        }
                    });
        }
        return chatListeners.get(chatId);
    }

    public void sendMessage(String text, String senderId, String receiverUserId, String senderName, String receiverName) {

        String chatId = ChatUtils.getChatId(senderId, receiverUserId);

        String messageId = java.util.UUID.randomUUID().toString();
        //TODO cambiar este timestamp para usar el de firebase
        long currentTimestamp = System.currentTimeMillis();

        Message newMessage = new Message(
                messageId,
                chatId,
                text,
                senderId,
                receiverUserId,
                currentTimestamp
        );

        db.collection("Chats")
                .document(chatId)
                .collection("Messages")
                .document(messageId)
                .set(newMessage)
                .addOnFailureListener(e -> Log.e("ChatRepository", "Error al enviar mensaje", e));


        Map<String, Object> chatUpdates = new HashMap<>();
        chatUpdates.put("chatId", chatId);
        chatUpdates.put("participantIds", Arrays.asList(senderId, receiverUserId));
        chatUpdates.put("lastMessageText", text);
        chatUpdates.put("lastMessageTimestamp", currentTimestamp);
        chatUpdates.put("lastMessageUserId", senderId);
        Map<String,String> mapNames = new HashMap<>();
        mapNames.put(senderId, senderName);
        mapNames.put(receiverUserId, receiverName);
        chatUpdates.put("participantNames", mapNames);

        db.collection("Chats")
                .document(chatId)
                .set(chatUpdates, SetOptions.merge());

        Log.d("LOG", "Mensaje enviado y conversación actualizada en Firestore");
    }
}