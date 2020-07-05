package com.anshu.www.gdapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
    TextView userName, discussions, total_votes, email, status;
    ImageView edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingspage);
        mauth = FirebaseAuth.getInstance();
        currentuserid = mauth.getCurrentUser().getUid();
        Rootref = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference("Profile Photos");

        initializefields();

        //username.setVisibility(View.INVISIBLE);

        edit.setOnClickListener(new View.OnClickListener() {
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
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image")) && (!dataSnapshot.hasChild("status"))) {
                    String retrieveusername = dataSnapshot.child("name").getValue().toString();
                    //String retrieveuserstatus = dataSnapshot.child("status").getValue().toString();
                    String retrieveprofileimage = dataSnapshot.child("image").getValue().toString();

                    userName.setText(retrieveusername);
                    //userstatus.setText(retrieveuserstatus);
                    Picasso.get().load(retrieveprofileimage).into(userprofileImage);


                } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image")) && (dataSnapshot.hasChild("status"))) {
                    String retrieveusername = dataSnapshot.child("name").getValue().toString();
                    String retrieveuserstatus = dataSnapshot.child("status").getValue().toString();
                    String retrieveprofileimage = dataSnapshot.child("image").getValue().toString();
                    String retrieveprofileemail = dataSnapshot.child("email").getValue().toString();
                    userName.setText(retrieveusername);
                    status.setText(retrieveuserstatus);
                    email.setText(retrieveprofileemail);
                    Picasso.get().load(retrieveprofileimage).into(userprofileImage);


                } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {
                    String retrieveusername = dataSnapshot.child("name").getValue().toString();
                    // String retrieveuserstatus = dataSnapshot.child("status").getValue().toString();


                    userName.setText(retrieveusername);
                    // userstatus.setText(retrieveuserstatus);
                } else {
                    userName.setVisibility(View.VISIBLE);
                    Toast.makeText(Settingspage.this, "Please update your profile information", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void initializefields() {

        //updateaccountsettings = findViewById(R.id.update_Settings_button);
        userName = findViewById(R.id.name);
        discussions = findViewById(R.id.discussions);
        total_votes = findViewById(R.id.votes);
        email = findViewById(R.id.useremail);
        status = findViewById(R.id.userstatus);
        edit = findViewById(R.id.edit);
        //username = findViewById(R.id.set_username);
        //userstatus = findViewById(R.id.set_profilestatus);
        userprofileImage = findViewById(R.id.profile_image);
        loadingbar = (ProgressDialog) new ProgressDialog(this);


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
                        if (task.isSuccessful()) {
                            Toast.makeText(Settingspage.this, "Profile Image uploaded successfully..", Toast.LENGTH_LONG).show();
                            final String downloadtheurl = resultUri.toString();
                            Rootref.child("Users").child(currentuserid).child("image").setValue(downloadtheurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Settingspage.this, "Image saved in database successfully...", Toast.LENGTH_LONG).show();
                                        loadingbar.dismiss();
                                    } else {
                                        String message = task.getException().toString();
                                        Toast.makeText(Settingspage.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        } else {
                            String message = (task.getException()).toString();
                            Toast.makeText(Settingspage.this, "Error: " + message, Toast.LENGTH_LONG).show();
                            loadingbar.dismiss();
                        }

                    }
                });
            }


        }


    }


    private void updateSettings() {
        // create an alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(Settingspage.this);
        builder.setTitle("Edit Details");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.updatedialog, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText setusername = customLayout.findViewById(R.id.set_username);
                EditText setuserstatus = customLayout.findViewById(R.id.set_profilestatus);
                //sendDialogDataToActivity(editText.getText().toString());
                String updatename = setusername.getText().toString();
                String updatestatus = setuserstatus.getText().toString();

                if (TextUtils.isEmpty(updatename)) {
                    Toast.makeText(Settingspage.this, "Please write user name", Toast.LENGTH_LONG).show();
                }

                if (TextUtils.isEmpty(updatestatus)) {
                    Toast.makeText(Settingspage.this, "Please write user status", Toast.LENGTH_LONG).show();
                } else {

                    HashMap<String, Object> profileMap = new HashMap<>();
                    profileMap.put("uid", currentuserid);
                    profileMap.put("name", updatename);
                    profileMap.put("status", updatestatus);
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
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }


}

