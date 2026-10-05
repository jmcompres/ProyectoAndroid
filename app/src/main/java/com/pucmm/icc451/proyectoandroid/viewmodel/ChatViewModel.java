package com.pucmm.icc451.proyectoandroid.viewmodel;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pucmm.icc451.proyectoandroid.model.Message;
import com.pucmm.icc451.proyectoandroid.model.User;
import com.pucmm.icc451.proyectoandroid.repository.ChatRepository;
import com.pucmm.icc451.proyectoandroid.repository.UserRepository;
import com.pucmm.icc451.proyectoandroid.util.ChatUtils;

import java.util.List;

public class ChatViewModel {

    private final ChatRepository repository;

    public ChatViewModel() {
        repository = ChatRepository.getInstance();
    }

    public LiveData<List<Message>> getMessages(String otherUserId) {
        User currentUser = UserRepository.getInstance().getCurrentUser();
        return ChatRepository.getInstance().getMessages(ChatUtils.getChatId(currentUser.getId(), otherUserId));
    }

    public boolean sendMessage(String messageContent, String receiverUserId, String receiverName) {
        if (messageContent == null || messageContent.trim().isEmpty()) return false;
        if (receiverUserId == null) return false;

        User currentUser = UserRepository.getInstance().getCurrentUser();
        repository.sendMessage(messageContent, currentUser.getId(), receiverUserId, currentUser.getName(), receiverName);
        return true;
    }

    public void sendImageMessage(android.net.Uri imageUri, String receiverUserId, String receiverName) {
        User currentUser = UserRepository.getInstance().getCurrentUser();

        if (currentUser.getId() != null && receiverUserId != null) {
            repository.sendImageMessage(imageUri, currentUser.getId(), receiverUserId, currentUser.getName(), receiverName);
        }
    }

}
