package com.anshu.www.gdapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupChat extends AppCompatActivity {

    static FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messageref;
    MessageAdapter messageAdapter;
    User user;
    //int votes=0;
    List<message> messages;
    private Toolbar toolbar;
    RecyclerView recyclerView;
    EditText msg;
    FloatingActionButton send;
   public String  currentgroupname2;
   static String tempname;
int likes=0;
int dislikes=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentgroupname2 = getIntent().getExtras().get("groupname").toString();
        setContentView(R.layout.activity_group_chat);

        tempname=currentgroupname2;

        toolbar = findViewById(R.id.group_chat_bar_layout1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentgroupname2);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = new User();

        messages = new ArrayList<message>();
        recyclerView = findViewById(R.id.list);
        msg = findViewById(R.id.input_msg);
        send = findViewById(R.id.send);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(msg.getText().toString())) {
                    message message = new message(msg.getText().toString(), user.getName(),likes,dislikes,auth.getCurrentUser().getUid());
                    msg.setText("");
                    messageref.push().setValue(message);

                } else {
                    Toast.makeText(GroupChat.this, "You can't send empty msg", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentuser = auth.getCurrentUser();

        user.setUid(currentuser.getUid());
        user.setEmail(currentuser.getEmail());

        database.getReference("Users").child(currentuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                user.setUid(currentuser.getUid());
                AllMethods.name = user.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        messageref = database.getReference("messages").child(currentgroupname2);
        messageref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                message mesg = dataSnapshot.getValue(message.class);
                mesg.setKey(dataSnapshot.getKey());
                messages.add(mesg);
                displaymessages(messages);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                message message = dataSnapshot.getValue(message.class);
                message.setKey(dataSnapshot.getKey());

                List<message> newmessages = new ArrayList<message>();

                for (message m : messages) {
                    if (m.getKey().equals(message.getKey())) {
                        newmessages.add(message);

                    } else {
                        newmessages.add(m);
                    }
                }
                messages = newmessages;
                displaymessages(messages);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


                message message = dataSnapshot.getValue(message.class);
                message.setKey(dataSnapshot.getKey());


                List<message> newmessages = new ArrayList<message>();

                for (message m : messages) {
                    if (!m.getKey().equals(message.getKey())) {
                        newmessages.add(m);
                    }
                }

                messages = newmessages;
                displaymessages(messages);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displaymessages(List<message> messages) {

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GroupChat.this);
        linearLayoutManager.scrollToPosition(messages.size()-1);
        recyclerView.setLayoutManager(linearLayoutManager);
       // recyclerView.setLayoutManager(new LinearLayoutManager(GroupChat.this));
        messageAdapter = new MessageAdapter(GroupChat.this, messages, messageref);
        recyclerView.setAdapter(messageAdapter);
    }

    public static String returnfromuserid()
    {
        return  auth.getCurrentUser().getUid();

    }

}
