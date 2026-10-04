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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.icc451.proyectoandroid.R;
import com.pucmm.icc451.proyectoandroid.databinding.ActivityChatBinding;
import com.pucmm.icc451.proyectoandroid.ui.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private MessageAdapter adapter;
    private List<String> messagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter();
        binding.recyclerViewMessages.setAdapter(adapter);

        binding.btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.editMessageText.getText().toString();

                if (!text.trim().isEmpty()) {
                    messagesList.add(text);
                    adapter.setMessages(messagesList);

                    binding.editMessageText.setText("");
                    binding.recyclerViewMessages.scrollToPosition(messagesList.size()-1);
                }
            }
        });
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}