package com.example.adeshsrivastava.twitterbot01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Update username
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView username = (TextView)hView.findViewById(R.id.textView);
        username.setText(getIntent().getStringExtra("username"));

        navigationView.setNavigationItemSelectedListener(this);
        new Thread(new UpdateNavHeader(username.getText().toString())).start();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Post your first tweet",Snackbar.LENGTH_LONG).setAction("Post your first tweet",null).show();
            }
        });
    }

    class UpdateNavHeader implements Runnable
    {
        String username;
        public UpdateNavHeader(String username) {this.username=username;}
        @Override
        public void run()
        {
            try
            {
                TwittterBot08 bot=new TwittterBot08();
                Twitter twitter=bot.intializeToken();
                User user=twitter.showUser(username);
                Bundle b = new Bundle();
                b.putString("name", user.getName());
                b.putString("profilePicURL", user.getProfileImageURL());
                Message msgToast = handlerForUpdateNavHeader.obtainMessage();
                msgToast.setData(b);
                handlerForUpdateNavHeader.sendMessage(msgToast);
            }
            catch (TwitterException e) {e.printStackTrace();}
        }
    }

    Handler handlerForUpdateNavHeader=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View hView =  navigationView.getHeaderView(0);

            //Update name
            TextView name = (TextView)hView.findViewById(R.id.name);
            name.setText(msg.getData().getString("name"));

            //Update profilePic
            String profilePicURL=msg.getData().getString("profilePicURL");
            new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                    .execute(profilePicURL);
        }
    };

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.timeline)
        {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View hView =  navigationView.getHeaderView(0);
            TextView username = (TextView)hView.findViewById(R.id.textView);

            // Handle the camera action
            Intent i=new Intent(HomeActivity.this,TimelineActivity.class);
            i.putExtra("username",username.getText());
            startActivity(i);


        }
        else if (id == R.id.search)
        {

        }
        else if (id == R.id.authorize)
        {
            Intent i=new Intent(this,AuthoriseActivity.class);
            startActivity(i);
        }
        else if (id == R.id.automateDMtoFollowers)
        {
            Toast.makeText(this,"Bot has been started",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(this,MyService.class);
            startService(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
