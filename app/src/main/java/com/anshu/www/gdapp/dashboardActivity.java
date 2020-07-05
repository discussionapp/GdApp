package com.anshu.www.gdapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class dashboardActivity extends AppCompatActivity {

    CircleImageView userdp;
    TextView username, useremail, userlocation, userid, discussion_no, total_votes;
    Button givesugg,reqtopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        userdp=findViewById(R.id.profile_image);
        givesugg=findViewById(R.id.give_sugg);
        reqtopic=findViewById(R.id.request_topic);
    }


}


