package com.pucmm.icc451.proyectoandroid.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.icc451.proyectoandroid.R;
import com.pucmm.icc451.proyectoandroid.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> chatList = new ArrayList<>();
    private final String currentUserId = "MyId";

    public void setChats(List<Chat> chats) {
        this.chatList = chats;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        String otherUserId = chat.getParticipantIds().get(0).equals(currentUserId) ?
                chat.getParticipantIds().get(1) : chat.getParticipantIds().get(0);

        String contactName = chat.getParticipantNames().get(otherUserId);
        if (contactName == null) contactName = "Usuario Desconocido";

        holder.txtContactName.setText(contactName);

        String initials = "";
        String[] parts = contactName.split(" ");
        int nParts = parts.length;
        if (nParts > 0 && !parts[0].isEmpty()) initials += parts[0].substring(0, 1).toUpperCase();
        if (nParts > 1 && !parts[nParts-1].isEmpty()) initials += parts[nParts-1].substring(0, 1).toUpperCase();
        holder.txtAvatarInitials.setText(initials);

        holder.txtLastMessage.setText(chat.getLastMessageText());
        holder.txtTime.setText("12:00 P.M."); // TODO Formatear el chat.getLastMessageTimestamp()

        if (chat.getUnreadCount() > 0) {
            holder.cardUnreadBadge.setVisibility(View.VISIBLE);
            holder.txtUnreadCount.setText(String.valueOf(chat.getUnreadCount()));
        } else {
            holder.cardUnreadBadge.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView txtContactName, txtLastMessage, txtTime, txtAvatarInitials, txtUnreadCount;
        CardView cardUnreadBadge;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContactName = itemView.findViewById(R.id.txtContactName);
            txtLastMessage = itemView.findViewById(R.id.txtLastMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtAvatarInitials = itemView.findViewById(R.id.txtAvatarInitials);
            txtUnreadCount = itemView.findViewById(R.id.txtUnreadCount);
            cardUnreadBadge = itemView.findViewById(R.id.cardUnreadBadge);
        }
    }
}