package com.eventr.eventr;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Eventer {
    public static ArrayList<Event> inventor;

    ArrayList<Event> inventify(int range){
        inventor = new ArrayList<Event>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereWithinMiles(
                ParseConstants.KEY_LOCATION,
                (ParseGeoPoint)ParseUser.getCurrentUser().get(ParseConstants.KEY_LOCATION),
                (double)range);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null){
                    for(ParseUser user : parseUsers){
                        if((boolean)user.get(ParseConstants.KEY_PUBLIC)){
                            inventor.add(new Event(user));
                        }
                    }
                }
            }
        });
        return inventor;
    }
}
