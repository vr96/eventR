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
    ArrayList<String> followers = new ArrayList<String>();
    int[] topicCounts = {0, 0, 0, 0, 0};
    ArrayList<String> statuses = new ArrayList<String>();


    void setup(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        this.latitude = ((ParseGeoPoint)currentUser.get(ParseConstants.KEY_LOCATION)).getLatitude();
        this.longitude = ((ParseGeoPoint)currentUser.get(ParseConstants.KEY_LOCATION)).getLongitude();
        this.title = (currentUser.get(ParseConstants.KEY_STATUS_MESSAGE)).toString();

        followers = (ArrayList<String>)currentUser.get(ParseConstants.KEY_FOLLOWER_IDS);
        for(String followerId : followers){
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(followerId, new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null){

                    }
                }
            });
        }
    }
}
