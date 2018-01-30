package com.cardyapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("test","message");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Userdata userdata = getApp().getPreferences().getLoggedInUser(getApp());

                if (userdata == null)
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                else {
                    if (userdata.getIsProfileComplete())
                        startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                    else startActivity(new Intent(SplashActivity.this, FirstTimeProfileActivity.class));
                }
                finish();
            }
        }, 1000);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }
}
