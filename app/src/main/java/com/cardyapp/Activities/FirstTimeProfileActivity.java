package com.cardyapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.cardyapp.R;
import com.cardyapp.fragments.ProfileFragment;

/**
 * Created by Priyanka on 1/30/2018.
 */

public class FirstTimeProfileActivity extends BaseActivity implements ProfileFragment.onProfileUpdateListener {

    private Toast backToast;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_first_time_profile;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.Profile_title));

        ProfileFragment fragment = ProfileFragment.newInstance();
        fragment.setListener(this);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (backToast != null && backToast.getView().getWindowToken() != null) {
            backToast.cancel();
            finish();
        } else {
            backToast = Toast.makeText(this, "Tap back button once more to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
    }

    @Override
    public void onProfileUpdated() {
        startActivity(new Intent(FirstTimeProfileActivity.this, DashboardActivity.class));
        finish();
    }
}
