package com.pucmm.icc451.proyectoandroid.viewmodel;

import androidx.lifecycle.LiveData;

import com.pucmm.icc451.proyectoandroid.model.Message;
import com.pucmm.icc451.proyectoandroid.repository.ChatRepository;

import java.util.List;

public class ChatViewModel {

    private final ChatRepository repository;

    public ChatViewModel() {
        repository = ChatRepository.getInstance();
    }

    public LiveData<List<Message>> getMessages() {
        return repository.getMessages();
    }

    public boolean sendMessage(String messageContent, String receiverUserId) {
        if (messageContent == null || !messageContent.trim().isEmpty()) return false;
        if (receiverUserId == null) return false;

        String senderId = "MyId";
        String senderName = "José";
        repository.sendMessage(messageContent, senderId, receiverUserId, senderName);
        return true;
    }

}
