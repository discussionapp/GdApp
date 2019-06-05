package com.anshu.www.gdapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class retrievedata extends AppCompatActivity {


    ListView lv;
    FirebaseDatabase database;
    DatabaseReference ref;
        ArrayList<String> list;
        ArrayAdapter<String> adapter;
      userdetails user1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrievedata);
        user1=new userdetails();
        lv=findViewById(R.id.list1);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("user");
        list=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,R.layout.userinformation,R.id.userinfo1,list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    user1=ds.getValue(userdetails.class);
                    list.add(user1.getName().toString()+ " "+user1.getTopic().toString());
                }
                lv.setAdapter( adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
