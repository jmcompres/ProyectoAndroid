package com.pucmm.icc451.proyectoandroid.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pucmm.icc451.proyectoandroid.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRepository {

    private static ChatRepository instance = null;
    private final List<Message> localMessages = new ArrayList<>();
    private final MutableLiveData<List<Message>> messagesLiveData = new MutableLiveData<>();

    private ChatRepository() {
        messagesLiveData.setValue(new ArrayList<>(localMessages));
    }

    public static ChatRepository getInstance() {
        if (instance==null) {
            instance = new ChatRepository();
        }
        return instance;
    }

    public LiveData<List<Message>> getMessages() {
        return messagesLiveData;
    }

    public void sendMessage(String text, String senderId, String receiverUserId, String senderName) {

        String chatId = getChatId(senderId, receiverUserId);

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

        Map<String, Object> chatUpdates = new HashMap<>();

        List<String> participants = new ArrayList<>();
        participants.add(senderId);
        participants.add(receiverUserId);
        chatUpdates.put("participantIds", participants);
        chatUpdates.put("lastMessageText", text);
        chatUpdates.put("lastMessageTimestamp", currentTimestamp);
        chatUpdates.put("lastMessageUserName", senderName);

        localMessages.add(newMessage);
        messagesLiveData.postValue(new ArrayList<>(localMessages));

        //TODO Luego hay que actualizar la conversación con firebase
        Log.d("LOG", "Conversación " + chatId + " actualizada por " + senderName + " con el mensaje: " + text);
    }

    public String getChatId(String myId, String otherUserId) {
        if (myId.compareTo(otherUserId) < 0) {
            return myId + "_" + otherUserId;
        } else {
            return otherUserId + "_" + myId;
        }
    }
}
