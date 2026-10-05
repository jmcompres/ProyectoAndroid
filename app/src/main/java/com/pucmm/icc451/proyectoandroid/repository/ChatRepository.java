package com.pucmm.icc451.proyectoandroid.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pucmm.icc451.proyectoandroid.model.Message;
import com.pucmm.icc451.proyectoandroid.util.ChatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRepository {

    private static ChatRepository instance = null;
    private final Map<String, MutableLiveData<List<Message>>> mockDatabase = new HashMap<>();

    private ChatRepository() {
    }

    public static ChatRepository getInstance() {
        if (instance==null) {
            instance = new ChatRepository();
        }
        return instance;
    }

    public LiveData<List<Message>> getMessages(String chatId) {
        if (!mockDatabase.containsKey(chatId)) {
            mockDatabase.put(chatId, new MutableLiveData<>(new ArrayList<>()));
        }
        return mockDatabase.get(chatId);
    }

    public void sendMessage(String text, String senderId, String receiverUserId, String senderName) {

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

        if (!mockDatabase.containsKey(chatId)) {
            mockDatabase.put(chatId, new MutableLiveData<>(new ArrayList<>()));
        }
        MutableLiveData<List<Message>> chatLiveData = mockDatabase.get(chatId);
        List<Message> currentMessages = chatLiveData.getValue();
        currentMessages.add(newMessage);
        chatLiveData.postValue(currentMessages);

        Map<String, Object> chatUpdates = new HashMap<>();
        List<String> participants = new ArrayList<>();
        participants.add(senderId);
        participants.add(receiverUserId);
        chatUpdates.put("participantIds", participants);
        chatUpdates.put("lastMessageText", text);
        chatUpdates.put("lastMessageTimestamp", currentTimestamp);
        chatUpdates.put("lastMessageUserName", senderName);
        //TODO Luego hay que actualizar la conversación con firebase
        Log.d("LOG", "Conversación " + chatId + " actualizada por " + senderName + " con el mensaje: " + text);
    }
}
