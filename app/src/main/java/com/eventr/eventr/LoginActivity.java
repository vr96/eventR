package com.eventr.eventr;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import com.facebook.Session;

public class LoginActivity extends Activity {
    SocialAuthAdapter adapter;
    Button facebook_button;
    String userName = "";
    String email = "";
    String token = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
     //   login = (LoginButton) findViewById(R.id.authButton);
        adapter = new SocialAuthAdapter(new ResponseListener());

        facebook_button = (Button)findViewById(R.id.fb_btn);
        facebook_button.setBackgroundResource(R.drawable.facebook);

        facebook_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                adapter.authorize(LoginActivity.this, Provider.FACEBOOK);
            }
        });
        try {
            token = adapter.getCurrentProvider().getAccessGrant().getKey();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Tuski.");
        }

        System.out.println(token == null);

    }

    public final class ResponseListener implements DialogListener
    {
        // Defines the event when the login was successful.
        @Override
        public void onComplete(Bundle values){
            adapter.getUserProfileAsync(new ProfileDataListener());
        }
        // Defines the event when the login was cancelled.
        @Override
        public void onCancel(){
            System.out.println("Operation was cancelled");
        }
        // Defines the event when the back button was pressed.
        @Override
        public void onBack(){
            System.out.println("Operation was cancelled");
        }

        // Defines the event when there is an error with authenticating.
        @Override
        public void onError(SocialAuthError socialAuthError) {
            System.out.println(socialAuthError.getMessage());
        }
    }

    // To receive the profile response after authentication
    public class ProfileDataListener implements SocialAuthListener<Profile> {

        @Override
        public void onExecute(String s, Profile profile) {
            Profile profilemap = profile;
            // Gets the username.
            userName = profilemap.getFirstName() + " " +
                    profilemap.getLastName();
            email = profilemap.getEmail();
            System.out.println(userName);
            System.out.println(email);
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {
            System.out.println(socialAuthError.getMessage());
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
        ParseFacebookUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                }
            }
        });

    } */


}
