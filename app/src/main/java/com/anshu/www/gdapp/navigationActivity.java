package com.anshu.www.gdapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Toast;

import com.anshu.www.gdapp.model.MyDataModel;
import com.anshu.www.gdapp.parser.JSONparser;
import com.anshu.www.gdapp.utils.InternetConnection;
import com.anshu.www.gdapp.utils.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class navigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<MyDataModel> list;
    private MyArrayAdapter adapter;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = new ArrayList<>();
        adapter = new MyArrayAdapter(this, list);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(findViewById(R.id.drawer_layout), list.get(position).getDailyTopic() + " => " + list.get(position), Snackbar.LENGTH_LONG).show();
            }
        });

        Toast toast = Toast.makeText(getApplicationContext(), "Click on Refresh button to get todays topic", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //           .setAction("Action", null).show();

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    new GetDataTask().execute();
                } else {
                    Snackbar.make(view, "Internet Connection Not Available", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigationdrawer_open, R.string.navigationdrawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Creating Get Data Task for Getting Data From Web
     */
    @SuppressLint("StaticFieldLeak")
    class GetDataTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        int jIndex;
        int x;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            x = list.size();

            if (x == 0)
                jIndex = 0;
            else
                jIndex = x;

            dialog = new ProgressDialog(navigationActivity.this);
            dialog.setTitle("Hey Wait Please..." + x);
            dialog.setMessage("lets see what is the todays topic");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            JSONObject jsonObject = JSONparser.getDataFromWeb();

            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        JSONArray array = jsonObject.getJSONArray(Keys.KEY_CONTACTS);
                        int lenArray = array.length();
                        if (lenArray > 0) {
                            for (; jIndex < lenArray; jIndex++) {
                                MyDataModel model = new MyDataModel();
                                JSONObject innerObject = array.getJSONObject(jIndex);
                                String name = innerObject.getString(Keys.KEY_NAME);
                                model.setDailyTopic(name);
                                list.add(model);
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException je) {
                Log.i(JSONparser.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            //Checking if List size if more than zero then Update ListView

            if (list.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                Snackbar.make(findViewById(R.id.drawer_layout), "No Data Found", Snackbar.LENGTH_LONG).show();
            }
        }
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
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
