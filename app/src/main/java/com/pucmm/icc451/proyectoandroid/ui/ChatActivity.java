package com.pucmm.icc451.proyectoandroid.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pucmm.icc451.proyectoandroid.databinding.ActivityChatBinding;
import com.pucmm.icc451.proyectoandroid.enums.Extras;
import com.pucmm.icc451.proyectoandroid.ui.adapter.MessageAdapter;
import com.pucmm.icc451.proyectoandroid.util.UserUtils;
import com.pucmm.icc451.proyectoandroid.viewmodel.ChatViewModel;

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

        String receiverId = getIntent().getStringExtra(Extras.EXTRA_TARGET_USER_ID.name());
        String receiverName = getIntent().getStringExtra(Extras.EXTRA_TARGET_USER_NAME.name());

        binding.txtToolbarUserName.setText(receiverName);
        binding.txtToolbarInitials.setText(UserUtils.getInitials(receiverName));

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
                String receiverId = getIntent().getStringExtra(Extras.EXTRA_TARGET_USER_ID.name());
                boolean updated = chatViewModel.sendMessage(text, receiverId, binding.txtToolbarUserName.getText().toString());
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