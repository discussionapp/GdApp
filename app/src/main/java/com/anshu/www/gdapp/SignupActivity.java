package com.anshu.www.gdapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    public EditText emailId, passwd;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    private DatabaseReference rootreference;
    EditText username4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        rootreference= FirebaseDatabase.getInstance().getReference();
        emailId = findViewById(R.id.input_email);
        passwd = findViewById(R.id.input_password);
        btnSignUp = findViewById(R.id.btn_signup);
        username4=findViewById(R.id.input_name);
        signIn = findViewById(R.id.link_login);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailID = emailId.getText().toString();
                String paswd = passwd.getText().toString();
                final String name=username4.getText().toString();
                if (emailID.isEmpty()) {
                    emailId.setError("Provide your Email first!");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    passwd.setError("Set your password");
                    passwd.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                    String currentuserid=firebaseAuth.getCurrentUser().getUid();
                                    User user=new User();
                                    user.setName(name);
                                    user.setEmail(emailID);
                                    rootreference.child("Users").child(currentuserid).setValue(user);
                                Intent mainIntent=new Intent(SignupActivity.this,navigationActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);

                                finish();

                                // startActivity(new Intent(SignupActivity.this, navigationActivity.class));


                            }
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(I);
            }
        });
    }
}
