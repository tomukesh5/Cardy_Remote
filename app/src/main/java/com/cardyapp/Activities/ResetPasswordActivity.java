package com.cardyapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cardyapp.Models.BaseResponse;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;
import com.cardyapp.Utils.IntentExtras;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Priyanka on 2/4/2018.
 */

public class ResetPasswordActivity extends BaseActivity implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(sequence = 1, message = "Please enter Password")
    @Password(min = 5, message = "Please enter a min 5 digit Password")
    @BindView(R.id.et_password)
    public EditText mEtPassword;

    @Order(2)
    @NotEmpty(sequence = 1, message = "Please enter confirm Password")
    @ConfirmPassword(sequence = 2, message = "Passwords do not match")
    @BindView(R.id.et_confirmPassword)
    public EditText mEtConfirmPassword;

    private Validator mValidator;
    private String userid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

        if (getIntent().hasExtra(IntentExtras.USER_ID)) {
            userid = (String) getIntent().getExtras().get(IntentExtras.USER_ID);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_reset_password;
    }

    @OnClick(R.id.btn_reset)
    public void onClickBtn_signUp() {
        validate();
    }

    @OnTextChanged(value = R.id.et_confirmPassword,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_ConfirmPassword(Editable editable) {
        if (editable.length() > 0) {
            mEtConfirmPassword.setError(null);
        }
    }

    @OnTextChanged(value = R.id.et_password,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_Password(Editable editable) {
        if (editable.length() > 0) {
            mEtPassword.setError(null);
        }
    }

    public void validate() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        showProgress("");

        CardySingleton.getInstance().callToResetPasswordAPI(userid, mEtPassword.getText() + "", new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                hideProgress();
                Log.d(AppConstants.TAG, response.toString());

                final BaseResponse baseResponse = response.body();

                if (baseResponse != null && baseResponse.getIsStatus()) {
                    Log.e(AppConstants.TAG, "Response :" + baseResponse.toString());

                    Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    DialogUtils.show(ResetPasswordActivity.this, response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
                hideProgress();
                Log.d(AppConstants.TAG, "onFailure");
                DialogUtils.show(ResetPasswordActivity.this, getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
            String[] listMessage = error.getCollatedErrorMessage(ResetPasswordActivity.this).split("\n");
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
