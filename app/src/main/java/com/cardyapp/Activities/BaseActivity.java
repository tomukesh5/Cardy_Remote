package com.cardyapp.Activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.cardyapp.App.Cardy;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private Cardy app;

    public interface PermissionCallback {
        /**
         * Called when all the permissions are granted
         */
        void onAllPermissionGranted();

        /**
         * Called when any one of the permission is denied
         * @param deniedPermissions Denied Permissions
         */
        void onPermissionsDenied(String[] deniedPermissions);
    }

    private PermissionCallback permissionCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        app = (Cardy) getApplication();
    }

    protected abstract int getLayoutResourceId();

    protected void requestForPermissions(@NonNull String[] permissions, String explaination, @NonNull PermissionCallback callback) {

        final List<String> requiredPermissions = new ArrayList<>();
        for(String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(permission);
            }
        }
        if(requiredPermissions.isEmpty()) {
            callback.onAllPermissionGranted();
            return;
        }
        permissionCallback = callback;
        boolean showExplanation = false;
        for(String permission : requiredPermissions) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showExplanation = true;
                break;
            }
        }
        if (showExplanation) {
            DialogUtils.show(this, explaination, getString(R.string.permission_required), getString(R.string.ok), false, false, new DialogUtils.ActionListner() {
                @Override
                public void onPositiveAction() {
                    ActivityCompat.requestPermissions(BaseActivity.this, requiredPermissions.toArray(new String[requiredPermissions.size()]), AppConstants.REQUEST_PERMISSION);
                }

                @Override
                public void onNegativeAction() {
                    //N/A
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, requiredPermissions.toArray(new String[requiredPermissions.size()]), AppConstants.REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> deniedPermissions = new ArrayList<>();
        for(int i = 0; i < grantResults.length; i++) {
            int permissionStatus = grantResults[i];
            if(permissionStatus != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if(deniedPermissions.isEmpty()) {
            permissionCallback.onAllPermissionGranted();
            return;
        }
        permissionCallback.onPermissionsDenied(deniedPermissions.toArray(new String[deniedPermissions.size()]));
    }

    public Cardy getApp() {
        return app;
    }
}
