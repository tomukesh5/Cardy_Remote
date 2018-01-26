package com.cardyapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.IntentExtras;
import com.cardyapp.services.LocationService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Priyanka on 12/31/2017.
 */

public class DashboardActivity extends BaseActivity {

    @BindView(R.id.btn_visibility)
    public TextView mBtnVisibility;

    private String menuAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setToolBar();
        setTitle(getString(R.string.Dashboard_label));
    }

    @OnClick(R.id.btn_visibility)
    public void onClickBtnVisibility(){
        if (getApp().getPreferences().getIsVisible()) {
            getApp().getPreferences().setIsVisible(false);
        } else {
            getApp().getPreferences().setIsVisible(true);
        }

        setVisibility(getApp().getPreferences().getIsVisible());
    }

    private void setVisibility(boolean visibility) {
        if (visibility) {
            mBtnVisibility.setBackground(getResources().getDrawable(R.drawable.btn_red_bg));
            getPermissions();
        } else {
            mBtnVisibility.setBackground(getResources().getDrawable(R.drawable.btn_blue_bg1));
        }
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
        return R.layout.activity_dashboard;
    }

    @OnClick(R.id.tv_connections)
    public void onClickConnectionMenu() {
        menuAction = AppConstants.DashboardMenu.CONNECTION.name();
        openDrawerActivity();
    }

    @OnClick(R.id.tv_search)
    public void onClickSearhMenu() {
        menuAction = AppConstants.DashboardMenu.SEARCH.name();
        openDrawerActivity();
    }

    @OnClick(R.id.tv_qrScanner)
    public void oonClickQRScannerMenu() {
        menuAction = AppConstants.DashboardMenu.QR_SANNER.name();
        openDrawerActivity();
    }

    @OnClick(R.id.tv_pendingRequest)
    public void onClickPendingREquestMenu() {
        menuAction = AppConstants.DashboardMenu.PENDING_REQUST.name();
        openDrawerActivity();
    }

    @OnClick(R.id.tv_profile)
    public void onClickProfileMenu() {
        menuAction = AppConstants.DashboardMenu.PROFILE.name();
        openDrawerActivity();
    }

    private void openDrawerActivity() {
        Intent intent = new Intent(DashboardActivity.this, DrawerActivity.class);
        intent.putExtra(IntentExtras.DRAWER_MENU, menuAction);
        startActivity(intent);
        finish();
    }
}