package com.anshu.www.gdapp;

import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
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

       /* userdp=findViewById(R.id.userdp);
        username=findViewById(R.id.username);
        useremail=findViewById(R.id.useremail);
        userlocation=findViewById(R.id.userlocation);
        userid=findViewById(R.id.userid);
        discussion_no=findViewById(R.id.diss_appeared);
        total_votes=findViewById(R.id.tot_votes);
        */
        givesugg=findViewById(R.id.give_sugg);
        reqtopic=findViewById(R.id.request_topic);
    }


}


