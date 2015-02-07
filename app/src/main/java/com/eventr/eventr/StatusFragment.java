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

    protected TextView mBubbleTextView;
    protected SeekBar mSeekBar;
    protected Switch mPrivacySwitch;
    protected EditText mStatusText;
    protected Button mUpdateButton;

    protected double mLong = MainActivity.mLongitude;
    protected double mLat = MainActivity.mLatitude;
    protected int radius;
    protected boolean mIsPublic;



    protected List<ParseObject> status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);
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
                +" Coordinates = ("+ MainActivity.mLatitude + ", " + MainActivity.mLongitude + ")");
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
                            ParseGeoPoint point = new ParseGeoPoint(MainActivity.mLatitude, MainActivity.mLongitude);
                            user.put(ParseConstants.KEY_LOCATION, point);
                            user.put(ParseConstants.KEY_RANGE, radius);
                            user.put(ParseConstants.KEY_STATUS_MESSAGE, mStatusText.getText().toString());
                            user.put(ParseConstants.KEY_USER_ID, ParseUser.getCurrentUser().getObjectId());
                            user.put(ParseConstants.KEY_USERNAME, ParseUser.getCurrentUser().getUsername());
                            user.put(ParseConstants.KEY_PUBLIC, mIsPublic);

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
                    + " Coordinates = (" + mLat + ", " + mLong + ")");
            mPrivacySwitch.setChecked((boolean) currentUser.get(ParseConstants.KEY_PUBLIC));
        }else{
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
