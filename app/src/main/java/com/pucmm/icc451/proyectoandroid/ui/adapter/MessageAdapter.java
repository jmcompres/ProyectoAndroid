package com.pucmm.icc451.proyectoandroid.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.pucmm.icc451.proyectoandroid.R;
import com.pucmm.icc451.proyectoandroid.model.Message;
import com.pucmm.icc451.proyectoandroid.util.ChatUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<Message> messages = new ArrayList<>();
    private final String currentUserId;

    public MessageAdapter(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setMessages (List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()) {
            holder.textContent.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(message.getImageUrl())
                    .into(holder.imageContent);
        } else {
            holder.textContent.setVisibility(View.VISIBLE);
            holder.imageContent.setVisibility(View.GONE);
            holder.textContent.setText(message.getText());
        }

        String timeString = ChatUtils.formatTimeStamp(message.getTimestamp());
        holder.textTimeStamp.setText(timeString);

        if (holder.textUserName != null) {
            if (message.getSenderId().equals(currentUserId)) {
                holder.textUserName.setText("Tú");
            } else {
                holder.textUserName.setText(message.getSenderName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textUserName;
        TextView textContent;
        TextView textTimeStamp;
        ImageView imageContent;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            textUserName = itemView.findViewById(R.id.txtSenderUser);
            textContent = itemView.findViewById(R.id.txtMessageContent);
            textTimeStamp = itemView.findViewById(R.id.txtTimeStamp);
            imageContent = itemView.findViewById(R.id.imgMessageContent);
        }
    }
}
