package com.anshu.www.gdapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class retrievedata extends AppCompatActivity {


    ListView lv;
    FirebaseDatabase database;
    DatabaseReference ref,group2ref,root2ref;
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
        root2ref=FirebaseDatabase.getInstance().getReference();
        group2ref=FirebaseDatabase.getInstance().getReference();
        list=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,R.layout.userinformation,R.id.userinfo1,list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    user1=ds.getValue(userdetails.class);
                    list.add(user1.getTopic().toString()+" "+"by"+" "+ user1.getName());
                }
                lv.setAdapter( adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String currentgroupname2=parent.getItemAtPosition(position).toString();
                Intent groupchat2intent=new Intent(getApplicationContext(),GroupChat.class);
                groupchat2intent.putExtra("groupname",currentgroupname2);
                group2ref.child("Groups").child(currentgroupname2).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!(dataSnapshot.exists()))
                        {
                            createnewgroup2(currentgroupname2);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                startActivity(groupchat2intent);
            }
        });
        }

            private void createnewgroup2(final String groupname)
            {
                root2ref.child("Groups").child(groupname).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(retrievedata.this,groupname +"group is created",Toast.LENGTH_LONG).show();
                        }

    }

    });
            }
}