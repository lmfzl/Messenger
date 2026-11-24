package com.example.messenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    private List<Message> messages = new ArrayList<>();
    private static final int VIEW_TYPE_MY_MESSAGE = 100;
    private static final int VIEW_TYPE_OTHER_MESSAGE = 101;
    private String currentUserId;

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public MessageAdapter(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        if (viewType == VIEW_TYPE_MY_MESSAGE){
            layoutResId = R.layout.item_my_message;
        }else{
            layoutResId = R.layout.item_other_message;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)){
            return VIEW_TYPE_MY_MESSAGE;
        }else {
            return VIEW_TYPE_OTHER_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message message = messages.get(position);
        holder.textViewMessage.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageHolder extends RecyclerView.ViewHolder{
        TextView textViewMessage;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}
