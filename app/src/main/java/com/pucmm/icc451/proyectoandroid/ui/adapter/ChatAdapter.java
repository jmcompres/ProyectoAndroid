package com.pucmm.icc451.proyectoandroid.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.icc451.proyectoandroid.R;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import lombok.SuperBuilder;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    //De mock por ahora
    private List<String> userNames = new ArrayList<>();

    public void setUsers(List<String> users) {
        this.userNames = users;
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
        String name = userNames.get(position);
        holder.txtContactName.setText(name);

        String initials = "";
        String[] parts = name.split(" ");
        int nParts = parts.length;
        if (nParts> 0 && !parts[0].isEmpty()) initials += parts[0].substring(0, 1).toUpperCase();
        if (nParts> 1 && !parts[nParts-1].isEmpty()) initials += parts[nParts-1].substring(0, 1).toUpperCase();
        holder.txtAvatarInitials.setText(initials);

        //TODO cambiar esto
        holder.txtLastMessage.setText("Último mensaje con " + name);
        holder.txtTime.setText("12:00 P.M.");
        if (position<2) {
            holder.cardUnreadBadge.setVisibility(View.VISIBLE);
            holder.txtUnreadCount.setText("3");
        }
        else {
            holder.cardUnreadBadge.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userNames.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView txtContactName;
        TextView txtLastMessage;
        TextView txtTime;
        TextView txtAvatarInitials;
        TextView txtUnreadCount;
        CardView cardUnreadBadge;

        public ChatViewHolder (@NonNull View itemView) {
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
