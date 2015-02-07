package com.eventr.eventr;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class Event {
    double latitude, longitude;
    ParseGeoPoint point;
    String title;
    int category;
    ArrayList<String> followerIds = new ArrayList<String>();
    ArrayList<String> groupNames = new ArrayList<String>();
    int[] topicCounts = {0, 0, 0, 0, 0};
    ArrayList<String> statusFeed = new ArrayList<String>();

    public Event(){
        latitude = 0.0;
        longitude = 0.0;
        title = "Generic ECE Party";
        category = -1;
        for(int i = 0; i < 5; i++){
            topicCounts[i] = 0;
        }
    }

    public Event(ParseUser currentUser){
        this.latitude = ((ParseGeoPoint)currentUser.get(ParseConstants.KEY_LOCATION)).getLatitude();
        this.longitude = ((ParseGeoPoint)currentUser.get(ParseConstants.KEY_LOCATION)).getLongitude();
        this.title = (currentUser.get(ParseConstants.KEY_STATUS_MESSAGE)).toString();
        this.category = (int) currentUser.get(ParseConstants.KEY_CATEGORY);
        if (category >= 0){
            topicCounts[category]++;
        }
        groupNames.add(currentUser.getUsername());

        followerIds = (ArrayList<String>)currentUser.get(ParseConstants.KEY_FOLLOWER_IDS);
        for(String followerId : followerIds){
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(followerId, new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null){
                        statusFeed.add((parseUser.get(ParseConstants.KEY_STATUS_MESSAGE).toString()));
                        if((int)parseUser.get(ParseConstants.KEY_CATEGORY) >= 0){
                            topicCounts[(int)parseUser.get(ParseConstants.KEY_CATEGORY)]++;
                        }
                        groupNames.add(parseUser.getUsername());
                    }
                }
            });
        }
    }

    double getLatitude(){
        return latitude;
    }

    double getLongitude(){
        return longitude;
    }

    String getTitle(){
        return title;
    }

    int[] getTopicCounts(){
        return topicCounts;
    }

    ArrayList<String> getGroupNames(){
        return groupNames;
    }

    ArrayList<String> getFollowerIds(){
        return followerIds;
    }

    ArrayList<String> getStatusFeed(){
        return statusFeed;
    }
}
