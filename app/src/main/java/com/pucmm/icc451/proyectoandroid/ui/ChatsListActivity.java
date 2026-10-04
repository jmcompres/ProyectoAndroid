package com.pucmm.icc451.proyectoandroid.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.icc451.proyectoandroid.R;
import com.pucmm.icc451.proyectoandroid.ui.adapter.ChatAdapter;

import java.util.Arrays;
import java.util.List;

public class ChatsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        recyclerView = findViewById(R.id.recyclerViewConversations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChatAdapter();
        recyclerView.setAdapter(adapter);

        List<String> mockUsers = Arrays.asList(
                "Lucía Morales",
                "Diego Ruiz",
                "Ana Martín",
                "Carlos Pérez",
                "Sara Vidal",
                "Miguel Contreras"
        );
        adapter.setUsers(mockUsers);
    }
}