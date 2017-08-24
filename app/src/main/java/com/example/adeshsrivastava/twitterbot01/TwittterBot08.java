/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.adeshsrivastava.twitterbot01;

import java.util.ArrayList;
import java.util.List;
import twitter4j.DirectMessage;
import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Adesh Srivastava
 */
public class TwittterBot08
{

    public static final String consumerKey="Od96ImcBimpzBzh0aLBTeNEoZ";
    public static final String consumerSecret="swqElJG0JAQR6wCZIu1Kq7ka0TX5mfQnf3AA1dR5hJHwvH1Jp6";
    public static final String accessToken="768139397149491200-XIgS1EsJZsmGib4Z3vjNztLIJTl55Xu";
    public static final String accessTokenSecret="qC7BvDavxTgaFoj6S5ChasiVYzPjHqpOhdaJ7vApgSMgP";
      
    public  Twitter twitter;
    

    
    public Twitter intializeToken()
    {
        
        ConfigurationBuilder configurationBuilder=new ConfigurationBuilder();
        
        configurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        Configuration configuration=configurationBuilder.build();
        TwitterFactory twitterFactory =new TwitterFactory(configuration);
        twitter=twitterFactory.getInstance();
        
        return twitter;
    }
    
    public DirectMessage sendDMFromBot(String screenName,String text) throws TwitterException
    {
        DirectMessage msg=twitter.sendDirectMessage(screenName, text);
        return msg;
    }
    
    public ArrayList<Long> getFollowerIDs(String screenName) throws TwitterException
    {
        ArrayList<Long> result=new ArrayList();
        long cursor=-1; 
        IDs ids;
        do
        {
            ids=twitter.getFollowersIDs(screenName, cursor);
            for (long id:ids.getIDs())
            {
                result.add(id);
            }
        }
        while((cursor=ids.getNextCursor())!=0);
        return result;
    }
    
    public boolean statusUpdate(String text) throws TwitterException
    {
        Status status=twitter.updateStatus(text);
        return true;
    }
    
}
