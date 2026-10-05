package com.pucmm.icc451.proyectoandroid.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.pucmm.icc451.proyectoandroid.enums.Extras;
import com.pucmm.icc451.proyectoandroid.ui.adapter.MessageAdapter;
import com.pucmm.icc451.proyectoandroid.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private MessageAdapter adapter;
    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter();
        binding.recyclerViewMessages.setAdapter(adapter);

        chatViewModel = new ChatViewModel();

        String receiverId = getIntent().getStringExtra(Extras.EXTRA_TARGET_USER.name());
        chatViewModel.getMessages(receiverId).observe(this, messages -> {
            adapter.setMessages(messages);
            if (!messages.isEmpty()) {
                binding.recyclerViewMessages.scrollToPosition(messages.size() - 1);
            }
        });

        binding.btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.editMessageText.getText().toString();
                String receiverId = getIntent().getStringExtra(Extras.EXTRA_TARGET_USER.name());
                boolean updated = chatViewModel.sendMessage(text, receiverId);
                if (updated) binding.editMessageText.setText("");
            }
        });
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}