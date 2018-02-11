package com.cardyapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cardyapp.Models.BaseResponse;
import com.cardyapp.Models.SendOTPForgotPasswordModel;
import com.cardyapp.Models.SignInModel;
import com.cardyapp.Models.SignUpModel;
import com.cardyapp.Models.SocialSignInModel;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;
import com.cardyapp.Utils.IntentExtras;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rac on 29/12/17.
 */

public class ForgotPasswordActivity extends BaseActivity implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(sequence = 1, message = "Please enter mobile number")
    @Length(min = 10, max = 10, sequence = 2, message = "Mobile number must be 10 digits")
    @BindView(R.id.et_mobileNo)
    public EditText mEtMobileNo;

    @BindView(R.id.btn_reset)
    public TextView mBtnReset;

    private String password;
    private String socialType;
    private String userData;

    private String TAG = getClass().getSimpleName();
    private Validator mValidator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolBar();

        if (getIntent().hasExtra(IntentExtras.SOCIAL_LOGIN_TOKEN)) {
            setTitle(getString(R.string.btn_sign_in));
            password = (String) getIntent().getExtras().get(IntentExtras.SOCIAL_LOGIN_TOKEN);
            socialType = (String) getIntent().getExtras().get(IntentExtras.SOCIAL_TYPE);
            userData = (String) getIntent().getExtras().get(IntentExtras.SOCIAL_DATA);
            mBtnReset.setText(getString(R.string.btn_sign_in));
        } else {
            setTitle(getString(R.string.forgot_password_text));
            mBtnReset.setText("Get OTP");
        }
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_forgot_password;
    }

    @OnClick(R.id.et_mobileNo)
    public void onClickEmail() {
        mEtMobileNo.setError(null);
    }

    @OnClick(R.id.btn_reset)
    public void onClickReset() {
        validate();
    }

    public void validate() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        // TODO: 29/12/17 integrate forgot password api
        if (getIntent().hasExtra(IntentExtras.SOCIAL_LOGIN_TOKEN)) {
            socialLogIn();
        } else {
          forgotPassword();
        }

    }

    private void socialLogIn() {
        showProgress("");
        CardySingleton.getInstance().callToSocialSignInAPI(mEtMobileNo.getText() + "", password, socialType, userData, Callback_SignIn);
    }


    Callback<SocialSignInModel> Callback_SignIn = new Callback<SocialSignInModel>() {
        @Override
        public void onResponse(Call<SocialSignInModel> call, Response<SocialSignInModel> response) {

            Log.d(AppConstants.TAG, response.toString());

            final SocialSignInModel signInModel = response.body();
            hideProgress();
            if (signInModel != null && signInModel.getIsStatus()) {
                Log.e(TAG, "Response : " + signInModel.toString());
                if (signInModel.getIs_social_account_verified()) {
                    getApp().getPreferences().setLoggedInUser(signInModel.getData(), getApp());
                    if (signInModel.getData().getIsProfileComplete())
                        startActivity(new Intent(ForgotPasswordActivity.this, DashboardActivity.class));
                    else startActivity(new Intent(ForgotPasswordActivity.this, FirstTimeProfileActivity.class));
                } else {
                    Intent intent = new Intent(ForgotPasswordActivity.this, OTPActivity.class);
                    intent.putExtra(IntentExtras.USER_DTO, signInModel.getData());
                    startActivity(intent);
                }
                finish();

            } else {
                DialogUtils.show(ForgotPasswordActivity.this, response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
        public void onFailure(Call<SocialSignInModel> call, Throwable t) {
            hideProgress();
            Log.d(AppConstants.TAG, "onFailure");
            DialogUtils.show(ForgotPasswordActivity.this, getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                @Override
                public void onPositiveAction() {

                }

                @Override
                public void onNegativeAction() {

                }
            });
        }
    };

    private void forgotPassword() {
        showProgress("");

        CardySingleton.getInstance().callToSendOTPForgotPasswordAPI(mEtMobileNo.getText() + "", new Callback<SendOTPForgotPasswordModel>() {
            @Override
            public void onResponse(Call<SendOTPForgotPasswordModel> call, Response<SendOTPForgotPasswordModel> response) {

                hideProgress();
                Log.d(AppConstants.TAG, response.toString());

                final SendOTPForgotPasswordModel baseResponse = response.body();

                if (baseResponse != null && baseResponse.getIsStatus()) {
                    Log.e(AppConstants.TAG, "Response :" + baseResponse.toString());
                    Intent intent = new Intent(ForgotPasswordActivity.this, OTPActivity.class);
                    intent.putExtra(IntentExtras.USER_ID, baseResponse.getData());
                    startActivity(intent);
                    finish();
                } else {
                    DialogUtils.show(ForgotPasswordActivity.this, response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
            public void onFailure(Call<SendOTPForgotPasswordModel> call, Throwable t) {
                hideProgress();
                Log.d(AppConstants.TAG, "onFailure");
                DialogUtils.show(ForgotPasswordActivity.this, getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String[] listMessage = error.getCollatedErrorMessage(ForgotPasswordActivity.this).split("\n");
            String message = listMessage[0];
            //String message = error.getCollatedErrorMessage(getActivity());
            // Display error messages ;)
            if (view instanceof EditText) {
                EditText text = ((EditText) view);
                text.setError(message);
            }
        }
    }
}
