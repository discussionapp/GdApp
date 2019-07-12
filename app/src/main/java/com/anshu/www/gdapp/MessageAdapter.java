package com.anshu.www.gdapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;
import static android.widget.Toast.LENGTH_SHORT;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    Context context;
    List<message> messages;
    message message;
    User user;
    private FirebaseAuth mauth;
    private DatabaseReference usersref;
    DatabaseReference msgdb;




    public static final int MSG_TYPE_LEFT = 0;
    private DatabaseReference referencemessages;
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
            mauth=FirebaseAuth.getInstance();
            return new MessageAdapterViewHolder((view));
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.msglayout_left, viewGroup, false);
            mauth=FirebaseAuth.getInstance();
            return new MessageAdapterViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.MessageAdapterViewHolder messageAdapterViewHolder,int i) {

String fromUserid;
String messagesenderid=mauth.getCurrentUser().getUid();
        fromUserid=GroupChat.returnfromuserid();
        final message message = messages.get(i);

        usersref=FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserid);

        usersref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
if((dataSnapshot.hasChild("image")) ) {

    String receiverimage = dataSnapshot.child("image").getValue().toString();
    Picasso.get().load(receiverimage).placeholder(R.drawable.profile_image).into(messageAdapterViewHolder.receiverprofileimage);

}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!message.getName().equals(AllMethods.name))
        {
            messageAdapterViewHolder.tvname.setText(message.getName());
        }
        messageAdapterViewHolder.tvmsg.setText(message.getMsg());
        messageAdapterViewHolder.tvmsgtime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getTime()));
        messageAdapterViewHolder.votes.setText(String.valueOf(message.getVotes())+" "+"Likes");

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tvmsg;
        TextView tvmsgtime;
        TextView tvname;
        public CircleImageView receiverprofileimage;
        String messageret;
int f=0;


        String retrievetime,firename,votesfire;
    String groupnamecheck;
    long value;
        TextView votes;
        ImageButton btnlike,btndislike;




        public MessageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvmsg = itemView.findViewById(R.id.show_message);
            tvmsgtime = itemView.findViewById(R.id.text_msg_time);
            tvname=itemView.findViewById(R.id.name);
            votes=itemView.findViewById(R.id.votesshow);
            btnlike=itemView.findViewById(R.id.img1);
            btndislike=itemView.findViewById(R.id.img2);
            receiverprofileimage=itemView.findViewById(R.id.receiverprofileimage);




            btnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!(messages.get(getAdapterPosition()).getName().equals(AllMethods.name))) {
                        msgdb.child(messages.get(getAdapterPosition()).getKey()).child("votes").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                value = (long) dataSnapshot.getValue();
                                value = value + 1;
                                dataSnapshot.getRef().setValue(value);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });


            btndislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(messages.get(getAdapterPosition()).getName().equals(AllMethods.name)))
                {
                        msgdb.child(messages.get(getAdapterPosition()).getKey()).child("votes").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                value = (long) dataSnapshot.getValue();
                                value = value - 1;
                                dataSnapshot.getRef().setValue(value);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            });







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
