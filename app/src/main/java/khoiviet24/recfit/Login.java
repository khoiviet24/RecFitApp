package khoiviet24.recfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity{

    protected ImageView mLoginLogo;

    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mLoginBtn;

    private Button mFBLoginButton;
    protected ParseFile photoFile;

    protected byte[] scaledData;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        final List<String> permissions = Arrays.asList("public_profile", "email", "user_friends", "user_tagged_places");

        mLoginLogo = (ImageView)findViewById(R.id.login_logo);

        mUsername = (EditText) findViewById(R.id.usernameLogin_txt);
        mPassword = (EditText) findViewById(R.id.passwordLogin_txt);
        mLoginBtn = (Button) findViewById(R.id.login_btn);

        mFBLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        Bitmap mLogo = BitmapFactory.decodeResource(getResources(), R.drawable.login_logo);
        mLoginLogo.setImageBitmap(mLogo);

        mFBLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(Login.this, permissions, new LogInCallback() {
                    @Override
                    public void done(final ParseUser user, ParseException err) {
                        //error
                        if (user == null) {
                            Log.d("RecFit", "Uh oh. The user cancelled the Facebook login.");
                            Toast.makeText(getApplicationContext(), "Log-out from Facebook and try again please!", Toast.LENGTH_SHORT).show();
                            ParseUser.logOut();
                        }
                        //new user
                        else if (user.isNew()) {
                            Log.d("RecFit", "User signed up and logged in through Facebook!1");


                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, Login.this, permissions, new SaveCallback() {
                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                            Log.d("RecFit", "Woohoo, user logged in with Facebook!2");

                                            getUserDetailsFromFB();

                                            Intent LoginToHome = new Intent(Login.this, NavDrawer.class);
                                            startActivity(LoginToHome);


                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "You can change your personal data in Settings tab!", Toast.LENGTH_SHORT).show();
                                Intent LoginToHome = new Intent(Login.this, NavDrawer.class);
                                startActivity(LoginToHome);

                                getUserDetailsFromFB();
                            }
                            //old user
                        } else {
                            Log.d("RecFit", "User logged in through Facebook!3");
                            Intent LoginToHome = new Intent(Login.this, NavDrawer.class);
                            startActivity(LoginToHome);

                            getUserDetailsFromFB();

                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, Login.this, permissions, new SaveCallback() {
                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                            Log.d("RecFit", "Woohoo, user logged in with Facebook!4");


                                            getUserDetailsFromFB();

                                            Intent LoginToHome = new Intent(Login.this, NavDrawer.class);
                                            startActivity(LoginToHome);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });


        //listen to Login Button
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the user input and convert to string
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //login using parse sdk
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {

                        if (e == null) {
                            //success
                            Toast.makeText(Login.this, "Welcome Back!", Toast.LENGTH_LONG).show();

                            //take user to home
                            Intent LoginToHome = new Intent(Login.this, NavDrawer.class);
                            startActivity(LoginToHome);

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setMessage(e.getMessage());
                            builder.setTitle("Incorrect Login Info");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //close the dialog
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    }
                });
            }
        });


    }

    private void getUserDetailsFromFB(){

        GraphRequest request  = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {
                try{

                    final String facebookName = object.getString("name");
                    final String facebookEmail=object.getString("email");
                    ParseUser.getCurrentUser().put("Name", facebookName);
                    ParseUser.getCurrentUser().put("username",facebookName);
                    ParseUser.getCurrentUser().put("email", facebookEmail);
                    ParseUser.getCurrentUser().put("Gender", object.getString("gender"));

/*
                    if(object.getString("age_range") != null) {
                        ParseUser.getCurrentUser().put("Age", object.getString("age_range"));
                    }
*/

                    Bitmap tempBitmap = getFacebookProfilePicture(object.getString("id"));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    scaledData = bos.toByteArray();
                    photoFile = new ParseFile(ParseUser.getCurrentUser() + "_profile_photo.jpg", scaledData);
                    ParseUser.getCurrentUser().put("Picture", photoFile);


                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(Login.this, "Welcome back", Toast.LENGTH_LONG).show();
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public static Bitmap getFacebookProfilePicture(String userID){

        Bitmap bitmap = null;

        if(android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        return bitmap;
    }

}
