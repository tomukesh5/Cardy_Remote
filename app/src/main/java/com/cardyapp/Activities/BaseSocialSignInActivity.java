package com.cardyapp.Activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cardyapp.Models.SocialUserData;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.DialogUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public abstract class BaseSocialSignInActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host
            + "/v1/people/~:"
            + "(email-address,formatted-name,phone-numbers,picture-urls::(original))";
    public static final int REQUEST_GPLUS_SIGN_IN = 1000001;
    public static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    private boolean mIntentInProgress;

    protected abstract void getSocialData(String email, String password, String socialType, String socialUserData);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {
                                if (response.getError() != null) {
                                    DialogUtils.show(BaseSocialSignInActivity.this, "Something went wrong while fetching data from facebook, please try again later", "Login Failed");
                                } else {
                                    try {
                                        String fullName = json.getString("name");
                                        String[] arr = fullName.split(" ");
                                        String firstName, lastName = null;
                                        firstName = arr[0];
                                        if (arr.length > 1) {
                                            lastName = arr[1];
                                        }
                                        SocialUserData userData = new SocialUserData();
                                        userData.setFirstname(firstName);
                                        if (lastName != null) userData.setLastname(lastName);
                                        userData.setFbuserid(json.getString("id"));
                                        userData.setFullname(firstName + (lastName == null ? "" : lastName));
                                        userData.setPersonalemail(json.getString("email"));
                                        userData.setGender(json.getString("gender").toUpperCase().charAt(0) + "");
                                        String str = userData.toString();

                                        getSocialData(json.getString("email"), json.getString("id"), AppConstants.FB_LOGIN, str);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        Bundle params = new Bundle();
                        params.putString("fields", "id, name, email, gender");
                        request.setParameters(params);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.d(AppConstants.TAG, "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(AppConstants.TAG, error.toString());
                        DialogUtils.show(BaseSocialSignInActivity.this, getResources().getString(R.string.facebook_msg), getResources().getString(R.string.facebook_login_failed), getResources().getString(R.string.retry), getResources().getString(R.string.register), false, false, new DialogUtils.ActionListner() {
                            @Override
                            public void onPositiveAction() {

                            }

                            @Override
                            public void onNegativeAction() {
                                //startActivity(new Intent(BaseSocialSignInActivity.this, ActivityRegistration.class));
                                finish();
                            }
                        });
                    }
                });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new com.google.android.gms.common.api.Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void onFbLogin() {
        //setPermissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    public void googleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void getDataFromGPlus(GoogleSignInResult result) {
        if (null != result && result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String email = acct.getEmail();
            String firstName = null, lastName = null, strGender = null, googlePlusId = null;
            String displayName = acct.getDisplayName();
            String[] arr = displayName.split(" ");
            firstName = arr[0];
            if (arr.length > 1) {
                lastName = arr[1];
            }

            googlePlusId = acct.getId();

            SocialUserData userData = new SocialUserData();
            userData.setFirstname(firstName);
            if (lastName != null) userData.setLastname(lastName);
            userData.setGoogleuserid(googlePlusId);
            userData.setFullname(firstName + (lastName == null ? "" : lastName));
            userData.setPersonalemail(email);
            //userData.setGender(strGender);
            String str = userData.toString();
            getSocialData(email, googlePlusId, AppConstants.GOOGLE_LOGIN, str);
        } else {
            DialogUtils.show(BaseSocialSignInActivity.this, getResources().getString(R.string.google_msg), getResources().getString(R.string.google_login_failed), getResources().getString(R.string.retry), getResources().getString(R.string.register), false, false, new DialogUtils.ActionListner() {
                @Override
                public void onPositiveAction() {

                }

                @Override
                public void onNegativeAction() {
                    //startActivity(new Intent(ActivitySignIn.this, ActivityRegistration.class));
                    finish();
                }
            });
        }
    }

    public void linkedinLogin() {
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        /*Toast.makeText(getApplicationContext(), "success" +
                                        LISessionManager.getInstance(getApplicationContext())
                                                .getSession().getAccessToken().toString(),
                                Toast.LENGTH_LONG).show();*/
                        linkededinApiHelper();
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
    }

    // set the permission to retrieve basic -
    //information of User's linkedIn account
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE,
                Scope.R_EMAILADDRESS);
    }

    public void linkededinApiHelper(){

        showProgress("");
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    showResult(result.getResponseDataAsJson());
                    hideProgress();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                hideProgress();
                Toast.makeText(getApplicationContext(), "failed "
                                + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public  void  showResult(JSONObject response){

        try {

            String fullName = response.get("formattedName").toString();
            SocialUserData userData = new SocialUserData();
            if (fullName != null && fullName.contains(" ")) {
                userData.setFirstname(fullName.substring(0, fullName.indexOf(" ")));
                userData.setLastname(fullName.substring(fullName.indexOf(" ") + 1, fullName.length()));
            } else {
                userData.setFirstname(fullName);
                userData.setLastname(null);
            }

            userData.setLinkedinuserid(LISessionManager.getInstance(getApplicationContext())
                    .getSession().getAccessToken().getValue());
            userData.setFullname(fullName);
            userData.setPersonalemail(response.get("emailAddress").toString());
            String str = userData.toString();
            getSocialData(response.get("emailAddress").toString(), LISessionManager.getInstance(getApplicationContext())
                    .getSession().getAccessToken().getValue(), AppConstants.LINKEDIN_LOGIN, str);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //getDataFromGPlus(googleSignInResult,null);
                }
            });
        }
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onConnected(Bundle bundle) {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIntentInProgress && connectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                connectionResult.startResolutionForResult(this, REQUEST_GPLUS_SIGN_IN);
                //timeApp.showToast("Google plus connection failed");
                startIntentSenderForResult(connectionResult.getResolution().getIntentSender(), REQUEST_GPLUS_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                //timeApp.showToast("Google plus connection retrying");
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
}
