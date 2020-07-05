package com.anshu.www.gdapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anshu.www.gdapp.modal.AdminHelper;
import com.anshu.www.gdapp.modal.PrevioustopicHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

public class AdminPanel extends AppCompatActivity {

    EditText topicupload;
    Button uploadbtn;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        topicupload = findViewById(R.id.topicupload);
        uploadbtn = findViewById(R.id.uploadbtn);
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic_name = topicupload.getText().toString();
                if (topic_name.isEmpty()) {
                    Toast.makeText(AdminPanel.this, "Input necessary", Toast.LENGTH_SHORT).show();
                }
                storeUpdatedata(topic_name);

            }
        });
    }

    private void storeUpdatedata(final String topicName) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("DailyTopic");
        DatabaseReference reference1 = rootNode.getReference("PreviousTopics");
        AdminHelper addtopic = new AdminHelper(topicName);
        reference.child("topic").setValue(addtopic).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AdminPanel.this, "stored", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminPanel.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        time = new Date().getTime();
        final HashMap<String, Object> previousTopicMap = new HashMap<>();
        previousTopicMap.put("Pretopic_name", topicName);
        previousTopicMap.put("posted", DateFormat.format("dd-MM-yyyy", time).toString());
        PrevioustopicHelper previoustopicHelper = new PrevioustopicHelper(topicName, DateFormat.format("dd-MM-yyyy", time).toString());
        reference1.child(DateFormat.format("dd-MM-yyyyHH:mm:ss", time).toString()).setValue(previoustopicHelper).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //previoustopicHelper.setTopic_name(previousTopicMap.get("Pretopic_name").toString());
                //previoustopicHelper.setTopic_name(previousTopicMap.get("").toString());
                Toast.makeText(AdminPanel.this, "added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminPanel.this, "not added", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
