package com.example.adeshsrivastava.twitterbot01;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.SupportActionModeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthoriseActivity extends AppCompatActivity
{

    private EditText consumerKey;
    private EditText consumerSecret;
    private EditText accessSKey;
    private EditText accessSecret;


    private EditText passEditText;
    private Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorise);
        getStarted=(Button)findViewById(R.id.button);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        consumerKey=(EditText)findViewById(R.id.consumer);
        consumerSecret=(EditText)findViewById(R.id.consumerSecret);
        accessSKey=(EditText)findViewById(R.id.access);
        accessSecret=(EditText)findViewById(R.id.accessSecret);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkGetStarted(View arg0)
    {
        SharedPreferences buffer = getSharedPreferences("com.example.adeshsrivastava.BUFFER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = buffer.edit();
        editor.putString("consumer", consumerKey.getText().toString());
        editor.putString("consumerKey", consumerSecret.getText().toString());
        editor.putString("access", accessSKey.getText().toString());
        editor.putString("accessKey", accessSecret.getText().toString());
        editor.commit();
        Intent i=new Intent(AuthoriseActivity.this,HomeActivity.class);
        startActivity(i);
        /*

        final String email = emailEditText.getText().toString();
        if (!isValidEmail(email))
        {
            //Set error message for email field
            emailEditText.setError("Invalid Email");
        }

        final String pass = passEditText.getText().toString();
        if (!isValidPassword(pass))
        {
            //Set error message for password field
            passEditText.setError("Password cannot be empty");
        }

        if(isValidEmail(email) && isValidPassword(pass))
        {
            // Validation Completed
        }
        */

    }

    // validating email id
    private boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password
    private boolean isValidPassword(String pass)
    {
        if (pass != null && pass.length() >= 4)
        {
            return true;
        }
        return false;
    }
}