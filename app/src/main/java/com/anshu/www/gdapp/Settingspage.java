package com.anshu.www.gdapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settingspage extends AppCompatActivity {


    private Button updateaccountsettings;
    private EditText username, userstatus;
    private CircleImageView userprofileImage;
    private String currentuserid;
    private FirebaseAuth mauth;
    private static final int Gallerypick = 1;
    private DatabaseReference Rootref;
    private StorageReference UserProfileImageRef;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingspage);
        mauth = FirebaseAuth.getInstance();
        currentuserid = mauth.getCurrentUser().getUid();
        Rootref = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference("Profile Photos");
        initializefields();

        username.setVisibility(View.INVISIBLE);

        updateaccountsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                updateSettings();



            }
        });

        retrieveuserinfo();

        userprofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, Gallerypick);
            }
        });

    }

    private void retrieveuserinfo() {

        Rootref.child("Users").child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image")) &&  (!dataSnapshot.hasChild("status"))) {
                    String retrieveusername = dataSnapshot.child("name").getValue().toString();
                    //String retrieveuserstatus = dataSnapshot.child("status").getValue().toString();
                    String retrieveprofileimage = dataSnapshot.child("image").getValue().toString();

                    username.setText(retrieveusername);
                    //userstatus.setText(retrieveuserstatus);
                    Picasso.get().load(retrieveprofileimage).into(userprofileImage);


                }

                else if((dataSnapshot.exists())&& (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image")) && (dataSnapshot.hasChild("status")))
                {
                    String retrieveusername = dataSnapshot.child("name").getValue().toString();
                    String retrieveuserstatus = dataSnapshot.child("status").getValue().toString();
                    String retrieveprofileimage = dataSnapshot.child("image").getValue().toString();

                    username.setText(retrieveusername);
                    userstatus.setText(retrieveuserstatus);
                    Picasso.get().load(retrieveprofileimage).into(userprofileImage);




                }


                else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {
                    String retrieveusername = dataSnapshot.child("name").getValue().toString();
                   // String retrieveuserstatus = dataSnapshot.child("status").getValue().toString();


                    username.setText(retrieveusername);
                   // userstatus.setText(retrieveuserstatus);
                } else {
                    username.setVisibility(View.VISIBLE);
                    Toast.makeText(Settingspage.this, "Please update your profile information", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void initializefields() {

        updateaccountsettings = findViewById(R.id.update_Settings_button);
        username = findViewById(R.id.set_username);
        userstatus = findViewById(R.id.set_profilestatus);
        userprofileImage = findViewById(R.id.profile_image);
        loadingbar=(ProgressDialog) new ProgressDialog(this);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallerypick && resultCode == RESULT_OK && data != null) {

            Uri imageuri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                loadingbar.setTitle("Set Profile Image");
                loadingbar.setMessage("Please wait your profile image is updating...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                final Uri resultUri = result.getUri();

                StorageReference filepath = UserProfileImageRef.child(currentuserid + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                   if(task.isSuccessful())
                   {
                       Toast.makeText(Settingspage.this,"Profile Image uploaded successfully..",Toast.LENGTH_LONG).show();
                       final String downloadtheurl=resultUri.toString();
                       Rootref.child("Users").child(currentuserid).child("image").setValue(downloadtheurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful())
                          {
                              Toast.makeText(Settingspage.this,"Image saved in database successfully...",Toast.LENGTH_LONG).show();
                                    loadingbar.dismiss();
                          }
                          else
                          {
                              String message=task.getException().toString();
                              Toast.makeText(Settingspage.this,"Error: "+message,Toast.LENGTH_SHORT).show();

                          }
                           }
                       });

                   }
                   else
                   {
                       String message=(task.getException()).toString();
                       Toast.makeText(Settingspage.this,"Error: " + message,Toast.LENGTH_LONG).show();
                       loadingbar.dismiss();
                   }

                    }
                });
            }


        }


            }



        private void updateSettings () {

            String setusername = username.getText().toString();
            String setuserstatus = userstatus.getText().toString();

            if (TextUtils.isEmpty(setusername)) {
                Toast.makeText(this, "Please write user name", Toast.LENGTH_LONG).show();
            }

            if (TextUtils.isEmpty(setuserstatus)) {
                Toast.makeText(this, "Please write user status", Toast.LENGTH_LONG).show();
            } else {

                HashMap<String, Object> profileMap = new HashMap<>();
                profileMap.put("uid", currentuserid);
                profileMap.put("name", setusername);
                profileMap.put("status", setuserstatus);
                Rootref.child("Users").child(currentuserid).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Settingspage.this, "Profile Updated", Toast.LENGTH_LONG).show();
                            Intent q = new Intent(Settingspage.this, navigationActivity.class);
                            q.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(q);

                            finish();

                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(Settingspage.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }


        }


}

