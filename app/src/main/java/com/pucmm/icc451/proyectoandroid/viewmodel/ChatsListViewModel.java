package com.pucmm.icc451.proyectoandroid.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pucmm.icc451.proyectoandroid.model.Chat;
import com.pucmm.icc451.proyectoandroid.repository.ChatsListRepository;

import java.util.List;

public class ChatsListViewModel extends ViewModel {

    private final ChatsListRepository repository;

    public ChatsListViewModel() {
        repository = ChatsListRepository.getInstance();
    }

    public LiveData<List<Chat>> getChats() {
        return repository.getChats();
    }
}