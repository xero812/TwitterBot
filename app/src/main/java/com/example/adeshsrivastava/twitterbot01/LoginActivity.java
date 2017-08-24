package com.example.adeshsrivastava.twitterbot01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity
{

    public static final String consumerKey="Od96ImcBimpzBzh0aLBTeNEoZ";
    public static final String consumerSecret="swqElJG0JAQR6wCZIu1Kq7ka0TX5mfQnf3AA1dR5hJHwvH1Jp6";
    TwitterLoginButton loginButton;
    TwitterSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig=new TwitterAuthConfig(consumerKey,consumerSecret);
        Fabric.with(this,new Twitter(authConfig));
        setContentView(R.layout.activity_login);
        loginButton=(TwitterLoginButton)findViewById(R.id.loginButton);
        loginButton.setCallback(callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode,resultCode,data);
    }

    Callback<TwitterSession> callback=new Callback<TwitterSession>()
    {
        @Override
        public void success(Result<TwitterSession> result)
        {
            session=Twitter.getSessionManager().getActiveSession();
            Twitter.getApiClient(session).getAccountService().verifyCredentials(true,false);
            Intent i=new Intent(LoginActivity.this,HomeActivity.class);
            i.putExtra("username",result.data.getUserName());
            startActivity(i);
        }
        @Override
        public void failure(TwitterException exception)
        {
            Toast.makeText(LoginActivity.this,"Failure Login!",Toast.LENGTH_SHORT).show();
        }
    };


}

