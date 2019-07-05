package com.anshu.www.gdapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    Context context;
    List<message> messages;
    DatabaseReference msgdb;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(Context context, List<message> messages, DatabaseReference msgdb) {
        this.context = context;
        this.messages = messages;
        this.msgdb = msgdb;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.msglayout_right, viewGroup, false);
            return new MessageAdapterViewHolder((view));
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.msglayout_left, viewGroup, false);
            return new MessageAdapterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageAdapterViewHolder messageAdapterViewHolder, int i) {

        message message = messages.get(i);
        if (!message.getName().equals(AllMethods.name))
        {
            messageAdapterViewHolder.tvname.setText(message.getName());
        }
        messageAdapterViewHolder.tvmsg.setText(message.getMsg());
        messageAdapterViewHolder.tvmsgtime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getTime()));

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tvmsg;
        TextView tvmsgtime;
        TextView tvname;

        public MessageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvmsg = itemView.findViewById(R.id.show_message);
            tvmsgtime = itemView.findViewById(R.id.text_msg_time);
            tvname=itemView.findViewById(R.id.name);
        }
    }

    @Override
    public int getItemViewType(int position) {

        message message = messages.get(position);
        if (message.getName().equals(AllMethods.name)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }
}
