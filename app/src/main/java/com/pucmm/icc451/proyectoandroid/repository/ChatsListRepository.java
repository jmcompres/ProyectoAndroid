package com.pucmm.icc451.proyectoandroid.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pucmm.icc451.proyectoandroid.model.Chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsListRepository {

    private static ChatsListRepository instance = null;
    private final MutableLiveData<List<Chat>> chatsLiveData = new MutableLiveData<>();

    private ChatsListRepository() {
        loadMockChats();
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

    private void loadMockChats() {
        List<Chat> mockChats = new ArrayList<>();
        String myId = "MyId";

        Map<String, String> names1 = new HashMap<>();
        names1.put(myId, "José Miguel");
        names1.put("id_lucia", "Lucía Morales");

        mockChats.add(new Chat(
                "MyId_id_lucia",
                Arrays.asList(myId, "id_lucia"),
                names1,
                "Perfecto, nos vemos entonces.",
                "Lucía Morales",
                System.currentTimeMillis(),
                3
        ));

        Map<String, String> names2 = new HashMap<>();
        names2.put(myId, "José Miguel");
        names2.put("id_diego", "Diego Ruiz");

        mockChats.add(new Chat(
                "MyId_id_diego",
                Arrays.asList(myId, "id_diego"),
                names2,
                "Te envié los archivos del proyecto.",
                "José Miguel",
                System.currentTimeMillis(),
                0
        ));

        chatsLiveData.setValue(mockChats);
    }
}