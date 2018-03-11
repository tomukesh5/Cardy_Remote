package com.cardyapp.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cardyapp.Activities.BaseActivity;
import com.cardyapp.Activities.DrawerActivity;
import com.cardyapp.App.Cardy;
import com.cardyapp.Models.BaseResponse;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;
import com.cardyapp.Utils.PictureSourceChooser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Priyanka on 3/4/2018.
 */

public class SettingFragment extends Fragment {

    private Cardy app;

    public SettingFragment() {

    }

    public static SettingFragment newIntence() {
        return new SettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        app = (Cardy) getActivity().getApplication();
        getActivity().setTitle(getResources().getString(R.string.Setting_title));

        return view;
    }

    @OnClick(R.id.tv_syncContacts)
    public void onClickSyncContact() {
        getPermissions();
    }

    private void callAPI() {

        final DrawerActivity activity = (DrawerActivity) getActivity();
        activity.showProgress("");

        List<String> list = getContacts();
        CardySingleton.getInstance().callToSyncContactAPI(app.getPreferences().getLoggedInUser(app).getUserid(), list, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d(AppConstants.TAG, response.toString());
                activity.hideProgress();
                BaseResponse model = response.body();
                if (model != null && model.getIsStatus()) {

                    DialogUtils.show(getActivity(), model.getMessage(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                        @Override
                        public void onPositiveAction() {
                        }

                        @Override
                        public void onNegativeAction() {
                        }
                    });
                } else {
                    DialogUtils.show(getActivity(), response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                        @Override
                        public void onPositiveAction() {
                        }

                        @Override
                        public void onNegativeAction() {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                activity.hideProgress();
                DialogUtils.show(getActivity(), getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                    @Override
                    public void onPositiveAction() {
                    }

                    @Override
                    public void onNegativeAction() {
                    }
                });
            }
        });

    }

    private List<String> getContacts() {
        List<String> phoneNumbers = new ArrayList<>();
        Cursor cursor = app.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber = phoneNumber.replaceAll("-", "").replaceAll(" ", "").replaceAll("/+", "");
            if (phoneNumber.length() <= 13 && phoneNumber.length() >= 10) {
                String numbers = phoneNumber.length() == 12 ? phoneNumber : phoneNumber.substring(phoneNumber.length() - 10);
                if (numbers.length() <= 10) {
                    phoneNumber = "91" + numbers;
                }
            }
            if(phoneNumbers.contains(phoneNumber) || phoneNumber.contentEquals(app.getPreferences().getLoggedInUser(app).getUser_mobile()))
                continue;

            phoneNumbers.add(phoneNumber);
        }
        cursor.close();

        return phoneNumbers;
    }

    private void getPermissions() {
        String[] permissions = new String[]{
                "android.permission.READ_CONTACTS"
        };
        BaseActivity activity = (BaseActivity) getActivity();
        activity.requestForPermissions(permissions, getString(R.string.Camera_permission_dialog), new BaseActivity.PermissionCallback() {
            @Override
            public void onAllPermissionGranted() {
                callAPI();
            }

            @Override
            public void onPermissionsDenied(String[] deniedPermissions) {
                getPermissions();
            }
        });
    }
}