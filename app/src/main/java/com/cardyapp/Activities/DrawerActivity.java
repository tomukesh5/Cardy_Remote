package com.cardyapp.Activities;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.IntentExtras;
import com.cardyapp.fragments.ConnectionListFragment;
import com.cardyapp.fragments.PendingRequestFragment;
import com.cardyapp.fragments.ProfileFragment;
import com.cardyapp.services.LocationService;

public class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toast backToast;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction = null;

    private ProfileFragment profileFragment;
    private ConnectionListFragment connectionListFragment;
    private PendingRequestFragment pendingRequestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getPermissions();

        profileFragment = ProfileFragment.newInstance();
        connectionListFragment = ConnectionListFragment.newInstance();
        pendingRequestFragment = PendingRequestFragment.newIntence();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = null;
        String tag = null;
        String menu = (String) getIntent().getExtras().get(IntentExtras.DRAWER_MENU);
        if (menu != null) {
            if (menu.contentEquals(AppConstants.DashboardMenu.CONNECTION.name())) {
                fragment = connectionListFragment;
                tag = "connectionListFragment";
            } else if (menu.contentEquals(AppConstants.DashboardMenu.PENDING_REQUST.name())) {
                fragment = pendingRequestFragment;
                tag = "pendingRequestFragment";
            } else if (menu.contentEquals(AppConstants.DashboardMenu.QR_SANNER.name())) {

            } else if (menu.contentEquals(AppConstants.DashboardMenu.SEARCH.name())) {

            } else if (menu.contentEquals(AppConstants.DashboardMenu.PROFILE.name())) {
                fragment = profileFragment;
                tag = "profileFragment";
            }
        }
        if (fragment != null)
            getFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).addToBackStack(tag).commit();
    }

    private void getPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        requestForPermissions(permissions, getResources().getString(R.string.LocationPermissionMessage), new BaseActivity.PermissionCallback() {
            @Override
            public void onAllPermissionGranted() {
                startService(new Intent(DrawerActivity.this, LocationService.class));
            }

            @Override
            public void onPermissionsDenied(String[] deniedPermissions) {
                getPermissions();
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_drawer;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            if (backToast != null && backToast.getView().getWindowToken() != null) {
                backToast.cancel();
                super.onBackPressed();
            } else {
                backToast = Toast.makeText(this, "Tap back button once more to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        fragmentManager = getFragmentManager();
        fragmentTransaction = null;

        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
            //fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, homeFragment, "homeFragment");
            finish();
        } else if (id == R.id.nav_myProfile) {
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, profileFragment, "profileFragment");
        } else if (id == R.id.nav_share) {
            //fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, connectionListFragment, "connectionListFragment");
        } else if (id == R.id.nav_pendingRequest) {
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, pendingRequestFragment, "pendingRequestFragment");
        } else if (id == R.id.nav_qrCode) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_logOut) {
            getApp().getPreferences().setLoggedInUser(null, getApp());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(DrawerActivity.this, SignInActivity.class));
                    finish();
                }
            }, 400);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.RIGHT);

        if (null != fragmentTransaction) {
            fragmentTransaction.addToBackStack("Later Transaction").commit();
        }
        return true;
    }
}
