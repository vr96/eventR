package com.eventr.eventr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;


public class StatusFragment extends Fragment {

    protected TextView mBubbleTextView;
    protected SeekBar mSeekBar;
    protected Switch mPrivacySwitch;
    protected EditText mStatusText;

    protected int radius;


    protected List<ParseObject> status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);
        mBubbleTextView = (TextView)rootView.findViewById(R.id.bubbleSizeTextView);
        mSeekBar = (SeekBar)rootView.findViewById(R.id.seekBar);
        mPrivacySwitch = (Switch)rootView.findViewById(R.id.privacySwitch);
        mStatusText = (EditText)rootView.findViewById(R.id.statusEditText);

        //similar to data call: remove once in parse
        radius = 5;
        mSeekBar.setProgress(radius * 20);
        mBubbleTextView.setText("Search Radius: " + String.valueOf(radius) + " miles");

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                radius = progress/20;
                mBubbleTextView.setText("Search Radius: " + String.valueOf(radius) + " miles");
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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
