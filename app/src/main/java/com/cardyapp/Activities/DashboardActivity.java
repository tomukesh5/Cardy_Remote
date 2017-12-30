package com.cardyapp.Activities;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cardyapp.R;
import com.cardyapp.fragments.ConnectionListFragment;
import com.cardyapp.fragments.HomeFragment;
import com.cardyapp.fragments.ProfileFragment;
import com.cardyapp.services.LocationService;

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction = null;

    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private ConnectionListFragment connectionListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getPermissions();

        homeFragment = HomeFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();
        connectionListFragment = ConnectionListFragment.newInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getFragmentManager().beginTransaction().replace(R.id.container, homeFragment, "homeFragment").addToBackStack("homeFragment").commit();
    }

    private void getPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        requestForPermissions(permissions, getResources().getString(R.string.LocationPermissionMessage), new BaseActivity.PermissionCallback() {
            @Override
            public void onAllPermissionGranted() {
                startService(new Intent(DashboardActivity.this, LocationService.class));
            }

            @Override
            public void onPermissionsDenied(String[] deniedPermissions) {
                getPermissions();
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_dasboard;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        fragmentManager = getFragmentManager();
        fragmentTransaction = null;

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, homeFragment, "homeFragment");
        } else if (id == R.id.nav_gallery) {
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, profileFragment, "profileFragment");
        } else if (id == R.id.nav_slideshow) {
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, connectionListFragment, "connectionListFragment");
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.RIGHT);

        if (null != fragmentTransaction) {
            fragmentTransaction.addToBackStack("Later Transaction").commit();
        }
        return true;
    }
}
