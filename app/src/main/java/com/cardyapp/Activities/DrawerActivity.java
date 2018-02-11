package com.cardyapp.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.IntentExtras;
import com.cardyapp.fragments.ConnectionListFragment;
import com.cardyapp.fragments.FeedbackFragment;
import com.cardyapp.fragments.PendingRequestFragment;
import com.cardyapp.fragments.ProfileFragment;
import com.cardyapp.fragments.QRScannerFragment;
import com.cardyapp.fragments.SearchFragment;
import com.cardyapp.fragments.ShareFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tv_connections)
    public TextView mTvConnections;
    @BindView(R.id.tv_search)
    public TextView mTvSearch;
    @BindView(R.id.tv_qrScanner)
    public TextView mTvQrScanner;
    @BindView(R.id.tv_profile)
    public TextView mTvProfile;

    private TextView mTvEmail;
    private TextView mTvName;

    private Toast backToast;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction = null;

    private ProfileFragment profileFragment;
    private ConnectionListFragment connectionListFragment;
    private PendingRequestFragment pendingRequestFragment;
    private SearchFragment searchFragment;
    private QRScannerFragment qrScannerFragment;
    private FeedbackFragment feedbackFragment;
    private ShareFragment shareFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileFragment = ProfileFragment.newInstance();
        connectionListFragment = ConnectionListFragment.newInstance();
        pendingRequestFragment = PendingRequestFragment.newIntence();
        searchFragment = SearchFragment.newIntence();
        qrScannerFragment = QRScannerFragment.newIntence();
        feedbackFragment = FeedbackFragment.newIntence();
        shareFragment = ShareFragment.newIntence();

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
                mTvConnections.setTextColor(getResources().getColor(R.color.yellow_app));
                fragment = connectionListFragment;
                tag = "connectionListFragment";
            } else if (menu.contentEquals(AppConstants.DashboardMenu.PENDING_REQUST.name())) {
                fragment = pendingRequestFragment;
                tag = "pendingRequestFragment";
            } else if (menu.contentEquals(AppConstants.DashboardMenu.QR_SANNER.name())) {
                mTvQrScanner.setTextColor(getResources().getColor(R.color.yellow_app));
                fragment = qrScannerFragment;
                tag = "qrScannerFragment";
            } else if (menu.contentEquals(AppConstants.DashboardMenu.SEARCH.name())) {
                mTvSearch.setTextColor(getResources().getColor(R.color.yellow_app));
                fragment = searchFragment;
                tag = "searchFragment";
            } else if (menu.contentEquals(AppConstants.DashboardMenu.PROFILE.name())) {
                mTvProfile.setTextColor(getResources().getColor(R.color.yellow_app));
                fragment = profileFragment;
                tag = "profileFragment";
            }
        }
        if (fragment != null)
            getFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).addToBackStack(tag).commit();

        Userdata userdata = getApp().getPreferences().getLoggedInUser(getApp());
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_dasboard);
        mTvName = headerView.findViewById(R.id.tv_name);
        mTvEmail = headerView.findViewById(R.id.tv_email);
        if (userdata != null) {
            mTvEmail.setText(userdata.getUser_email());
            mTvName.setText((userdata.getFirstname() == null ? "" : userdata.getFirstname()) + " " + (userdata.getLastname() == null ? "" : userdata.getLastname()));
        } else {
            mTvEmail.setText("");
            mTvName.setText("");
        }
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
                finish();
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
            startActivity(new Intent(DrawerActivity.this, DashboardActivity.class));
            finish();
        } else if (id == R.id.nav_myProfile) {
            loadProfileFragment();
        } else if (id == R.id.nav_share) {
            clearTabSelection();
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, shareFragment, "shareFragment");
        } else if (id == R.id.nav_pendingRequest) {
            clearTabSelection();
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, pendingRequestFragment, "pendingRequestFragment");
        } else if (id == R.id.nav_qrCode) {
            loadQRScannerFragment();
        } else if (id == R.id.nav_feedback) {
            clearTabSelection();
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, feedbackFragment, "feedbackFragment");
        } else if (id == R.id.nav_logOut) {
            getApp().getPreferences().setLoggedInUser(null, getApp());
            getApp().getPreferences().setIsVisible(false);
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

    private void loadProfileFragment() {
        mTvProfile.setTextColor(getResources().getColor(R.color.yellow_app));
        mTvConnections.setTextColor(getResources().getColor(R.color.white));
        mTvQrScanner.setTextColor(getResources().getColor(R.color.white));
        mTvSearch.setTextColor(getResources().getColor(R.color.white));
        fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.container, profileFragment, "profileFragment");
    }

    @OnClick(R.id.tv_connections)
    public void onClicktv_connections() {
        mTvConnections.setTextColor(getResources().getColor(R.color.yellow_app));
        mTvSearch.setTextColor(getResources().getColor(R.color.white));
        mTvQrScanner.setTextColor(getResources().getColor(R.color.white));
        mTvProfile.setTextColor(getResources().getColor(R.color.white));
        fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.container, connectionListFragment, "connectionListFragment");
        if (null != fragmentTransaction) {
            fragmentTransaction.addToBackStack("Later Transaction").commit();
        }
    }

    @OnClick(R.id.tv_search)
    public void onClicktv_search() {
        mTvSearch.setTextColor(getResources().getColor(R.color.yellow_app));
        mTvConnections.setTextColor(getResources().getColor(R.color.white));
        mTvQrScanner.setTextColor(getResources().getColor(R.color.white));
        mTvProfile.setTextColor(getResources().getColor(R.color.white));
        fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.container, searchFragment, "searchFragment");
        if (null != fragmentTransaction) {
            fragmentTransaction.addToBackStack("Later Transaction").commit();
        }
    }

    @OnClick(R.id.tv_qrScanner)
    public void onClicktv_qrScanner() {
        loadQRScannerFragment();
        if (null != fragmentTransaction) {
            fragmentTransaction.addToBackStack("Later Transaction").commit();
        }
    }

    private void loadQRScannerFragment() {
        mTvProfile.setTextColor(getResources().getColor(R.color.white));
        mTvConnections.setTextColor(getResources().getColor(R.color.white));
        mTvQrScanner.setTextColor(getResources().getColor(R.color.yellow_app));
        mTvSearch.setTextColor(getResources().getColor(R.color.white));
        fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.container, qrScannerFragment, "qrScannerFragment");
    }

    @OnClick(R.id.tv_profile)
    public void onClicktv_profile() {
        loadProfileFragment();
        if (null != fragmentTransaction) {
            fragmentTransaction.addToBackStack("Later Transaction").commit();
        }
    }

    private void clearTabSelection() {
        mTvProfile.setTextColor(getResources().getColor(R.color.white));
        mTvConnections.setTextColor(getResources().getColor(R.color.white));
        mTvQrScanner.setTextColor(getResources().getColor(R.color.white));
        mTvSearch.setTextColor(getResources().getColor(R.color.white));
    }
}
