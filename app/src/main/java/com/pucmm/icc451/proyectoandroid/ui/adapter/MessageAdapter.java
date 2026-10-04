package com.pucmm.icc451.proyectoandroid.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;

import com.pucmm.icc451.proyectoandroid.R;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<String> messages = new ArrayList<>();

    public void setMessages (List<String> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        //TODO cambiar el tipo de contenedor de mesnaje según quien envíe el mensaje
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String message = messages.get(position);
        holder.textContent.setText(message);
        //TODO cambiar el usuario y el teimestamp
        holder.textUserName.setText("TÚ");
        holder.textTimeStamp.setText("12:00 P.M.");
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textUserName;
        TextView textContent;
        TextView textTimeStamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            //textUserName = itemView.findViewById(R.id.txtSenderUser);
            textUserName = itemView.findViewById(R.id.txtSelfUser);
            textContent = itemView.findViewById(R.id.txtMessageContent);
            textTimeStamp = itemView.findViewById(R.id.txtTimeStamp);
        }
    }
}
