package com.eventr.eventr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class StatusFragment extends Fragment {

    public static final String[] categories = {"Study", "Music", "Game", "Party", "Balloons"};
    public static int categoryNumber = -1;

    protected TextView mBubbleTextView;
    protected SeekBar mSeekBar;
    protected Switch mPrivacySwitch;
    protected EditText mStatusText;
    protected Button mUpdateButton;

    protected ImageButton mStudy;
    protected ImageButton mMusic;
    protected ImageButton mParty;
    protected ImageButton mGames;
    protected ImageButton mBloons;


    protected double mLong = MapFragment.longitude;
    protected double mLat = MapFragment.latitude;
    protected int radius;
    protected boolean mIsPublic;



    protected List<ParseObject> status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);

        mParty = (ImageButton)rootView.findViewById(R.id.partyButton);
        mMusic = (ImageButton)rootView.findViewById(R.id.musicButton);
        mBloons = (ImageButton)rootView.findViewById(R.id.balloonButton);
        mStudy = (ImageButton)rootView.findViewById(R.id.studyButton);
        mGames = (ImageButton)rootView.findViewById(R.id.gameButton);

        mStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryNumber = 0;
            }
        });
        mMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryNumber = 1;
            }
        });
        mParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryNumber = 2;
            }
        });
        mGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryNumber = 3;
            }
        });
        mBloons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryNumber = 4;
            }
        });

        mBubbleTextView = (TextView)rootView.findViewById(R.id.bubbleSizeTextView);
        mSeekBar = (SeekBar)rootView.findViewById(R.id.seekBar);
        mPrivacySwitch = (Switch)rootView.findViewById(R.id.privacySwitch);
        mPrivacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsPublic = isChecked;
            }
        });
        mStatusText = (EditText)rootView.findViewById(R.id.statusEditText);

        update();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                radius = progress/20;
                mBubbleTextView.setText("Search Radius: " + radius + " miles"
                        +" Coordinates = ("+ MapFragment.latitude + ", " + MapFragment.longitude + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        mUpdateButton = (Button)rootView.findViewById(R.id.updateButton);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e == null){
                            ParseGeoPoint point = new ParseGeoPoint(MapFragment.latitude,
                                    MapFragment.longitude);
                            user.put(ParseConstants.KEY_LOCATION, point);
                            user.put(ParseConstants.KEY_RANGE, radius);
                            user.put(ParseConstants.KEY_STATUS_MESSAGE, mStatusText.getText().toString());
                            user.put(ParseConstants.KEY_USER_ID, ParseUser.getCurrentUser().getObjectId());
                            user.put(ParseConstants.KEY_USERNAME, ParseUser.getCurrentUser().getUsername());
                            user.put(ParseConstants.KEY_PUBLIC, mIsPublic);
                            user.put(ParseConstants.KEY_CATEGORY, categoryNumber);
                            ArrayList<String> followersList = new ArrayList<String>();
                            user.put(ParseConstants.KEY_FOLLOWER_IDS, followersList);
                            user.saveInBackground();
                        }
                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null) {
            radius = (int) currentUser.get(ParseConstants.KEY_RANGE);
            mSeekBar.setProgress(radius * 20);
            mStatusText.setText(currentUser.get(ParseConstants.KEY_STATUS_MESSAGE).toString());
            mLat = ((ParseGeoPoint) currentUser.get(ParseConstants.KEY_LOCATION)).getLatitude();
            mLat = ((ParseGeoPoint) currentUser.get(ParseConstants.KEY_LOCATION)).getLatitude();
            mBubbleTextView.setText("Search Radius: " + radius + " miles"
                    + " Coordinates = (" + MapFragment.latitude + ", " + MapFragment.longitude + ")");
            mPrivacySwitch.setChecked((boolean) currentUser.get(ParseConstants.KEY_PUBLIC));
        }else{
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
