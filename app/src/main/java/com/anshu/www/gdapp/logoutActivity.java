package com.anshu.www.gdapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class logoutActivity extends AppCompatActivity {
    Button btnLogOut;
    TextView tv1,tv2;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        btnLogOut = (Button) findViewById(R.id.btn_logout4);
        tv1=findViewById(R.id.link_signup2);
        tv2=findViewById(R.id.link_login2);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(logoutActivity.this, MainActivity.class);
                startActivity(I);

            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(logoutActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(logoutActivity.this,MainActivity.class);
                startActivity(j);
            }
        });


    }
}