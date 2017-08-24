package com.example.adeshsrivastava.twitterbot01;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class MyService extends Service
{

    SharedPreferences buffer;

    public MyService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        buffer = getSharedPreferences("com.example.adeshsrivastava.BUFFER", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = buffer.edit();
            editor.putInt("followerCount", -1);
            editor.commit();
            IntentFilter intentFilter=new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(new Receiver(),intentFilter);
    }

    class Receiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Runnable r=new Runnable()
            {
                @Override
                public void run()
                {
                    long c=0l;
                    try
                    {
                        TwittterBot08 bot=new TwittterBot08();
                        Twitter twitter=bot.intializeToken();
                        ArrayList<Long> followerList=bot.getFollowerIDs(twitter.getScreenName());
                        c=followerList.size();

                        Bundle b = new Bundle();
                        b.putString("followerCount", ""+c);

                        Message msgToast = handlerToast.obtainMessage();
                        msgToast.setData(b);
                        handlerToast.sendMessage(msgToast);

                        Message msgBuffer = handlerUpdateBuffer.obtainMessage();
                        msgBuffer.setData(b);
                        handlerUpdateBuffer.sendMessage(msgBuffer);
                    }
                    catch (TwitterException e) {}
                }
            };
            new Thread(r).start();
        }
    };

































    Handler handlerToast=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String followerCount = msg.getData().getString("followerCount");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this);
            builder.setSmallIcon(R.drawable.followers)
                    .setContentTitle("Twitter bot")
                    .setContentText(followerCount+" followers");

            int NOTIFICATION_ID = 12345;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    };


    Handler handlerUpdateBuffer=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            final String followerCount = msg.getData().getString("followerCount");
            int realCount=Integer.parseInt(followerCount);

            Toast.makeText(MyService.this,"gg",Toast.LENGTH_SHORT).show();
            int prevCount=buffer.getInt("followerCount", -1);
            if(prevCount!=-1)
            {
                final int c=realCount-prevCount;
                if(c>0)
                {
                    Runnable r=new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                TwittterBot08 bot=new TwittterBot08();
                                Twitter twitter=bot.intializeToken();
                                ArrayList<Long> followerList=bot.getFollowerIDs(twitter.getScreenName());
                                for (int i=0;i<c;i++)
                                {
                                    User user=twitter.showUser(followerList.get(i));
                                    bot.sendDMFromBot(user.getScreenName(),"Thanks! Stay Connected! :)");

                                }
                            }
                            catch (TwitterException e) { }
                        }
                    };
                    new Thread(r).start();
                }

            }
            SharedPreferences.Editor editor = buffer.edit();
            editor.putInt("followerCount", realCount);
            editor.commit();


        }
    };




}













/*
//Notification Builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this);
            builder.setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setContentTitle("Twitter Bot")
                    .setContentText("Something interesting happened");
            int NOTIFICATION_ID = 12345;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());



 */
