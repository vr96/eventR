package com.eventr.eventr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import java.util.List;

public class LoginActivity extends Activity {
    SocialAuthAdapter adapter;
    Button facebook_button;
    String userName = "";
    String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Parse.initialize(this, getString(R.string.PARSE_APP_ID), getString(R.string.PARSE_KEY));

        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        //   login = (LoginButton) findViewById(R.id.authButton);
        adapter = new SocialAuthAdapter(new ResponseListener());

        facebook_button = (Button)findViewById(R.id.fb_btn);
        facebook_button.setBackgroundResource(R.drawable.facebook);

        facebook_button.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                adapter.authorize(LoginActivity.this, Provider.FACEBOOK);
            }
        });
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

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("email", email);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    if(e == null){
                        if(parseUsers.isEmpty()){
                            signup();
                        }else{
                            login();
                        }
                    }
                }
            });
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {
            System.out.println(socialAuthError.getMessage());
        }
    }

    private void signup() {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(userName);
        newUser.setEmail(email);
        newUser.setPassword("password");
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success!
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void login() {
        ParseUser.logInInBackground(userName, "password", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    //Success!
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    //Note: Same as writing builder.set 3 times...
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
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
}
