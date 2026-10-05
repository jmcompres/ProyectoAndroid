package com.pucmm.icc451.proyectoandroid.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.icc451.proyectoandroid.R;
import com.pucmm.icc451.proyectoandroid.databinding.ActivityChatBinding;
import com.pucmm.icc451.proyectoandroid.databinding.ActivityChatsListBinding;
import com.pucmm.icc451.proyectoandroid.enums.Extras;
import com.pucmm.icc451.proyectoandroid.model.User;
import com.pucmm.icc451.proyectoandroid.repository.UserRepository;
import com.pucmm.icc451.proyectoandroid.ui.adapter.ChatAdapter;
import com.pucmm.icc451.proyectoandroid.ui.adapter.MessageAdapter;
import com.pucmm.icc451.proyectoandroid.util.UserUtils;
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

        User currentUser = UserRepository.getInstance().getCurrentUser();
        binding.txtCurrentUserName.setText(currentUser.getName());
        binding.txtAvatarInitials.setText(UserUtils.getInitials(currentUser.getName()));

        binding.recyclerViewConversations.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter((chat, otherUserId, otherUserName) -> {

            Intent intent = new Intent(ChatsListActivity.this, ChatActivity.class);
            intent.putExtra(Extras.EXTRA_TARGET_USER_ID.name(), otherUserId);
            intent.putExtra(Extras.EXTRA_TARGET_USER_NAME.name(), otherUserName);
            startActivity(intent);

        });
        binding.recyclerViewConversations.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ChatsListViewModel.class);

        viewModel.getChats().observe(this, chats -> {
            if (chats != null) {
                adapter.setChats(chats);
            }
        });
    }
}