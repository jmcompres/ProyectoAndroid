package com.pucmm.icc451.proyectoandroid.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
import com.pucmm.icc451.proyectoandroid.viewmodel.AuthViewModel;
import com.pucmm.icc451.proyectoandroid.util.UserUtils;
import com.pucmm.icc451.proyectoandroid.viewmodel.ChatsListViewModel;

public class ChatsListActivity extends AppCompatActivity {

    private ActivityChatsListBinding binding;
    private ChatAdapter adapter;
    private ChatsListViewModel viewModel;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

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
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupLogoutButton();

        viewModel.getChats().observe(this, chats -> {
            if (chats != null) {
                adapter.setChats(chats);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupLogoutButton() {
        binding.btnLogout.setOnClickListener(v -> {
            authViewModel.logout();

            Intent intent = new Intent(ChatsListActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}