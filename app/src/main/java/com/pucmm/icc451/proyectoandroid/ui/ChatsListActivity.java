package com.pucmm.icc451.proyectoandroid.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.icc451.proyectoandroid.R;
import com.pucmm.icc451.proyectoandroid.databinding.ActivityChatBinding;
import com.pucmm.icc451.proyectoandroid.databinding.ActivityChatsListBinding;
import com.pucmm.icc451.proyectoandroid.ui.adapter.ChatAdapter;
import com.pucmm.icc451.proyectoandroid.ui.adapter.MessageAdapter;
import com.pucmm.icc451.proyectoandroid.viewmodel.ChatsListViewModel;

public class ChatsListActivity extends AppCompatActivity {

    private ActivityChatsListBinding binding;
    private ChatAdapter adapter;
    private ChatsListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewConversations.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter();
        binding.recyclerViewConversations.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ChatsListViewModel.class);

        viewModel.getChats().observe(this, chats -> {
            if (chats != null) {
                adapter.setChats(chats);
            }
        });
    }
}