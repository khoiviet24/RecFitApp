package khoiviet24.recfit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class InviteToApp extends android.support.v4.app.Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "RecFitInvite";
    private static final int REQUEST_INVITE = 0;

    private GoogleApiClient mGoogleApiClient;

    protected Button mFacebook;
    protected Button mEmail;
    protected Button mText;

    protected Button mInviteBtn;
    protected Button mCustomInviteBtn;

    public InviteToApp(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final View rootView = inflater.inflate(R.layout.fragment_invite_to_app, container, false);

        //mFacebook = (Button)rootView.findViewById(R.id.invite_facebook);
        //mEmail = (Button)rootView.findViewById(R.id.invite_email);
        //mText = (Button)rootView.findViewById(R.id.invite_text);

        mInviteBtn = (Button)rootView.findViewById(R.id.invite_button);
        mCustomInviteBtn = (Button)rootView.findViewById(R.id.custom_invite_button);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){

            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(AppInvite.API)
                    .enableAutoManage(getActivity(), this)
                    .build();

            boolean autoLaunchDeepLink = true;
            AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, getActivity(), autoLaunchDeepLink)
                    .setResultCallback(
                            new ResultCallback<AppInviteInvitationResult>() {
                                @Override
                                public void onResult(AppInviteInvitationResult result) {
                                    Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                    // Because autoLaunchDeepLink = true we don't have to do anything
                                    // here, but we could set that to false and manually choose
                                    // an Activity to launch to handle the deep link here.
                                }

                            });

/*
            mFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            mEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/

            mInviteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInviteClicked();
                }
            });

            mCustomInviteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCustomInviteClicked();
                }
            });
        }

        return rootView;
    }

    private void onInviteClicked(){
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);

    }

    private void onCustomInviteClicked() {
        // When using the setEmailHtmlContent method, you must also set a subject using the
        // setEmailSubject message and you may not use either setCustomImage or setCallToActionText
        // in conjunction with the setEmailHtmlContent method.
        //
        // The "%%APPINVITE_LINK_PLACEHOLDER%%" token is replaced by the invitation server
        // with the custom invitation deep link based on the other parameters you provide.
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setEmailHtmlContent("<html><body>" +
                        "<h1>App Invites</h1>" +
                        "<a href=\"%%APPINVITE_LINK_PLACEHOLDER%%\">Install Now!</a>" +
                        "<body></html>")
                .setEmailSubject(getString(R.string.invitation_subject))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if(requestCode == REQUEST_INVITE){
            //if(resultCode == RESULT_OK)

            String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
            Log.d(TAG, getString(R.string.sent_invitations_fmt, ids.length));
        }else{
            // Sending failed or it was canceled, show failure message to the user
            //showMessage(getString(R.string.send_failed));
        }
    }

    /*
    private void showMessage(String msg) {
        ViewGroup container = (ViewGroup) findViewById(R.id.snackbar_layout);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }*/

}
