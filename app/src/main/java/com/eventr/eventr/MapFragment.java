package com.eventr.eventr;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

}
