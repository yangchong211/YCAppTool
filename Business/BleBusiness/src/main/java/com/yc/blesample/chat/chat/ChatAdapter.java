package com.yc.blesample.chat.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.blesample.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ChatAdapter";

    public static final int SELF_CHAT = 0;
    public static final int FRIEND_CHAT = 1;


    private Context context;

    private List<Chat> list;

    public ChatAdapter(Context context, List<Chat> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType==SELF_CHAT) {
            return new RightHolder(LayoutInflater.from(context).inflate(R.layout.adapter_chat_right, viewGroup, false));
        } else {
            return new LeftHolder(LayoutInflater.from(context).inflate(R.layout.adapter_chat_left, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RightHolder) {
            ((RightHolder) viewHolder).tvText.setText(list.get(i).getText());
        } else {
            ((LeftHolder) viewHolder).tvText.setText(list.get(i).getText());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isSelf()) {
            return SELF_CHAT;
        } else {
            return FRIEND_CHAT;
        }
    }

    class LeftHolder extends RecyclerView.ViewHolder {

        TextView tvText;

        public LeftHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvLeftChatText);
        }

    }


    class RightHolder extends RecyclerView.ViewHolder {

        TextView tvText;

        public RightHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvRightChatText);
        }
    }

}
