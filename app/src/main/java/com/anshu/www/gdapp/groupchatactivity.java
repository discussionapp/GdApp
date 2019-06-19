package com.anshu.www.gdapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class groupchatactivity extends AppCompatActivity {

    private Toolbar toolbar1;
    private ImageButton sendmessagebutton1;
    private EditText usermessageInput1;
    private ScrollView scrollview1;
    private TextView displaytextmessages1;
    private FirebaseAuth mauth1;
    private DatabaseReference userref1,groupnameref,groupmessagekeyref;
    private String currentgroupname2,currentuserid1,currentusername1,currentdate,currenttime;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentgroupname2=getIntent().getExtras().get("groupname").toString();
        setContentView(R.layout.activity_groupchatactivity);
        mauth1=FirebaseAuth.getInstance();
        currentuserid1=mauth1.getCurrentUser().getUid();
        userref1= FirebaseDatabase.getInstance().getReference().child("Users");
        groupnameref=FirebaseDatabase.getInstance().getReference().child("Groups").child(currentgroupname2);


        initializefields();
        Getuserinfo1();
        sendmessagebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessagetodatabase();
                usermessageInput1.setText(null);
                scrollview1.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        groupnameref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.exists())
                {

                    Displaymessages(dataSnapshot);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.exists())
                {

                    Displaymessages(dataSnapshot);

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initializefields() {

        toolbar1=findViewById(R.id.group_chat_bar_layout1);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle(currentgroupname2);
        sendmessagebutton1=findViewById(R.id.sendmessagebutton);
        usermessageInput1=findViewById(R.id.input_groupmessage1);
        displaytextmessages1=findViewById(R.id.group_chat_text_display1);
        scrollview1=findViewById(R.id.my_scroll_view_1);
    }



    private void Getuserinfo1() {


        userref1.child(currentuserid1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentusername1=dataSnapshot.child("name").getValue().toString();



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void sendmessagetodatabase() {
        String message=usermessageInput1.getText().toString();
        String messageKey=groupnameref.push().getKey();

        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(this,"Please write message first...",Toast.LENGTH_LONG).show();
        }

        else
        {
            Calendar calforDate =Calendar.getInstance();
            SimpleDateFormat currentdateformat=new SimpleDateFormat("MMM dd, yyyy");
            currentdate=currentdateformat.format(calforDate.getTime());

            Calendar calfortime =Calendar.getInstance();
            SimpleDateFormat currenttimeformat=new SimpleDateFormat("hh:mm a");
            currenttime=currenttimeformat.format(calfortime.getTime());

            HashMap<String,Object> groupmessageKey =new HashMap<>();
            groupnameref.updateChildren(groupmessageKey);

            groupmessagekeyref=groupnameref.child(messageKey);
            HashMap<String,Object> messageinfomap= new HashMap<>();
            messageinfomap.put("name",currentusername1);
            messageinfomap.put("message",message);
            messageinfomap.put("date",currentdate);
            messageinfomap.put("time",currenttime);

            groupmessagekeyref.updateChildren(messageinfomap);

        }
    }

    private void Displaymessages(DataSnapshot dataSnapshot) {

        Iterator iterator=dataSnapshot.getChildren().iterator();

        while(iterator.hasNext())
        {
            String chatDate1=(String) ((DataSnapshot)iterator.next()).getValue();
            String chatmessage1=(String)((DataSnapshot)iterator.next()).getValue();
            String chatname1=(String)((DataSnapshot)iterator.next()).getValue();
            String chattime1=(String)((DataSnapshot)iterator.next()).getValue();

            displaytextmessages1.append(chatname1+" :\n"+ chatmessage1+ "\n"+ chattime1+ "     "+ chatDate1 + "\n\n\n");
            scrollview1.fullScroll(ScrollView.FOCUS_DOWN);
        }


    }



}
