package com.anshu.www.gdapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class navigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listview3;
    FirebaseDatabase database3;
    DatabaseReference ref3;
    DatabaseReference Rootref, checkref;
    FirebaseAuth mauth5;
    ArrayList<String> list3;
    FloatingActionButton fab;


    ArrayAdapter<String> adapter3;
    user3 user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview3 = findViewById(R.id.listview3);
        user = new user3();
        mauth5 = FirebaseAuth.getInstance();
        checkref = FirebaseDatabase.getInstance().getReference();
        Rootref = FirebaseDatabase.getInstance().getReference();
        database3 = FirebaseDatabase.getInstance();
        ref3 = database3.getReference("todaystopics");
        list3 = new ArrayList<>();
        adapter3 = new ArrayAdapter<String>(this, R.layout.userinfo2, R.id.textview3, list3);

        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    user = ds.getValue(user3.class);
                    list3.add(user.getTopic().toString() + " " + "by" + " " + user.getName());

                }
                listview3.setAdapter(adapter3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigationdrawer_open, R.string.navigationdrawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        listview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String currentGroupname1 = parent.getItemAtPosition(position).toString();
                Intent groupchatintent = new Intent(getApplicationContext(), GroupChat.class);
                groupchatintent.putExtra("groupname", currentGroupname1);

                checkref.child("Groups").child(currentGroupname1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!(dataSnapshot.exists())) {
                            createnewgroup(currentGroupname1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                startActivity(groupchatintent);

            }
        });

    }

    private void createnewgroup(final String groupname) {
        Rootref.child("Groups").child(groupname).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(navigationActivity.this, groupname + " " + "group is created", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menuchat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.main_logout_option) {
            mauth5.signOut();
            Intent k = new Intent(navigationActivity.this, MainActivity.class);
            startActivity(k);
        }
        if (item.getItemId() == R.id.main_settings_option) {
            Intent h = new Intent(navigationActivity.this, Settingspage.class);
            startActivity(h);
        }
        if (item.getItemId() == R.id.main_find_friends_option) {

        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            
        } else if (id == R.id.nav_topics) {
            Intent j = new Intent(navigationActivity.this, retrievedata.class);
            startActivity(j);

        } else if (id == R.id.nav_dashboard) {
            Intent h = new Intent(navigationActivity.this, dashboardActivity.class);
            startActivity(h);

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_rate) {

        } else if (id == R.id.nav_signout) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (user == null) {

            Intent z = new Intent(navigationActivity.this, MainActivity.class);
            z.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(z);
            finish();
        } else {
            verifyuserexistance();

        }
    }

    private void verifyuserexistance() {

        String currentuserid7 = mauth5.getCurrentUser().getUid();

        Rootref.child("Users").child(currentuserid7).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("name").exists())) {

                    Intent u = new Intent(navigationActivity.this, Settingspage.class);
                    u.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(u);
                    finish();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
