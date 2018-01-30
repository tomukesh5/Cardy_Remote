package com.cardyapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cardyapp.Models.BaseResponse;
import com.cardyapp.Models.SendOTPForgotPasswordModel;
import com.cardyapp.Models.SignUpModel;
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

    private Validator mValidator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolBar();
        setTitle(getString(R.string.forgot_password_text));
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
