package com.eventr.eventr;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    public GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapFragment.class.getSimpleName();
    public LocationRequest mLocationRequest;
    LocationListener listener;
    public static double latitude, longitude;
   // GoogleMap mMap = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
       // setUpMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates
                    (mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates
                    (mGoogleApiClient, mLocationRequest, this);
          //  latitude = location.getLatitude();
         //   longitude = location.getLongitude();
         //   System.out.println(latitude);
         //   System.out.println(longitude);
        }
        else {
            handleNewLocation(location);
        };

    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
     /*   LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("You are here!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    */
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
   /* private void setUpMap() {
        if (mMap == null){
        mMap.addMarker(new MarkerOptions().position(new LatLng(0,
                0)).title("Marker"));
        }
    } */
}
