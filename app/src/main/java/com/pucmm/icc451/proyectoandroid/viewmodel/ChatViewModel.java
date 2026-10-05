package com.pucmm.icc451.proyectoandroid.viewmodel;

import androidx.lifecycle.LiveData;

import com.pucmm.icc451.proyectoandroid.model.Message;
import com.pucmm.icc451.proyectoandroid.repository.ChatRepository;
import com.pucmm.icc451.proyectoandroid.util.ChatUtils;

import java.util.List;

public class ChatViewModel {

    private final ChatRepository repository;

    public ChatViewModel() {
        repository = ChatRepository.getInstance();
    }

    public LiveData<List<Message>> getMessages(String otherUserId) {
        return ChatRepository.getInstance().getMessages(ChatUtils.getChatId("MyId", otherUserId));
    }

    public boolean sendMessage(String messageContent, String receiverUserId) {
        if (messageContent == null || messageContent.trim().isEmpty()) return false;
        if (receiverUserId == null) return false;

        //TODO cambiar esto con firebase auth
        String senderId = "MyId";
        String senderName = "José";
        repository.sendMessage(messageContent, senderId, receiverUserId, senderName);
        return true;
    }

}
