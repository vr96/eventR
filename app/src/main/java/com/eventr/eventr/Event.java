package com.eventr.eventr;

import android.location.Location;

import java.util.ArrayList;

public class Event {
    Location location;
    String name;
    ArrayList<String> followers = new ArrayList<String>();
    int[] topicCounts = {0, 0, 0, 0, 0};
    ArrayList<String> statuses = new ArrayList<String>();

    public Event(){

    }

    void setup(){
        for(String followerId : followers){

        }
    }
}
