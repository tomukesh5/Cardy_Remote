package com.cardyapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.IntentExtras;

import butterknife.OnClick;

/**
 * Created by Priyanka on 12/31/2017.
 */

public class DashboardActivity extends BaseActivity {

    private String menuAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBar();
        setTitle(getString(R.string.Dashboard_label));
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
    }
}