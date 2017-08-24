package com.example.adeshsrivastava.twitterbot01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class DeciderActivity extends AppCompatActivity
{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Od96ImcBimgthpzBgrhyrhthweaghgrgewgntrg";
    private static final String TWITTER_SECRET = "swqElJG0JArtrhtrghrgAA1dR5hJHwvH1Jp6";


    SharedPreferences buffer;
    @Override

    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        buffer = getSharedPreferences("com.example.adeshsrivastava.BUFFER", Context.MODE_PRIVATE);

        if(buffer.getBoolean("firstTime", false))
        {
            // If it is not the first time
            Intent i=new Intent(DeciderActivity.this,LoginActivity.class);
            startActivity(i);
        }
        else
        {
            // If it is first time
            SharedPreferences.Editor editor = buffer.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
            Intent i=new Intent(DeciderActivity.this,IntroActivity.class);
            startActivity(i);
        }



    }
}
