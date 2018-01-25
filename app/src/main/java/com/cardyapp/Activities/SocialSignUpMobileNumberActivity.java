package com.cardyapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cardyapp.Models.SignUpModel;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;
import com.cardyapp.Utils.IntentExtras;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rac on 25/1/18.
 */

public class SocialSignUpMobileNumberActivity extends BaseActivity implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(sequence = 1, message = "Please enter mobile number")
    @Length(min = 10, max = 10, sequence = 2, message = "Mobile number must be 10 digits")
    @BindView(R.id.et_mobileNo)
    public EditText mEtMobileNo;

    private String email;
    private String socialData;
    private String socialType;
    private Validator mValidator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

        if (getIntent().hasExtra(IntentExtras.SOCIAL_DATA)) {
            socialData = (String) getIntent().getExtras().get(IntentExtras.SOCIAL_DATA);
            socialType = (String) getIntent().getExtras().get(IntentExtras.SOCIAL_TYPE);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_social_sign_up_mobile_number;
    }

    @OnTextChanged(value = R.id.et_email,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_Email(Editable editable) {
        if (editable.length() > 0) {
            mEtMobileNo.setError(null);
        }
    }

    @OnClick(R.id.btn_signUp)
    public void onClickBtn_signUp() {
        validate();
    }

    public void validate() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        signUp(mEtMobileNo.getText() + "", "", socialType, socialData);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String[] listMessage = error.getCollatedErrorMessage(SocialSignUpMobileNumberActivity.this).split("\n");
            String message = listMessage[0];
            //String message = error.getCollatedErrorMessage(getActivity());
            // Display error messages ;)
            if (view instanceof EditText) {
                EditText text = ((EditText) view);
                text.setError(message);
            }
        }
    }

    public void signUp(String email, String password, String socialusertype, String socialUserData) {
        this.email = email;
        showProgress("");

        CardySingleton.getInstance().callToSignUpAPI(email, password, socialusertype, socialUserData, Callback_SignUp);
    }

    Callback<SignUpModel> Callback_SignUp = new Callback<SignUpModel>() {
        @Override
        public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {

            hideProgress();
            Log.d(AppConstants.TAG, response.toString());

            final SignUpModel signUpModel = response.body();

            if (signUpModel != null && signUpModel.getIsStatus()) {
                Log.e(AppConstants.TAG, "Response :" + signUpModel.toString());
                Userdata userdata = new Userdata();
                userdata.setUser_email(email);
                userdata.setUserid(signUpModel.getUserid());
                Intent intent = new Intent(SocialSignUpMobileNumberActivity.this, OTPActivity.class);
                intent.putExtra(IntentExtras.USER_DTO, userdata);
                startActivity(intent);
                finish();
            } else {
                DialogUtils.show(SocialSignUpMobileNumberActivity.this, response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
        public void onFailure(Call<SignUpModel> call, Throwable t) {
            hideProgress();
            Log.d(AppConstants.TAG, "onFailure");
            DialogUtils.show(SocialSignUpMobileNumberActivity.this, getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                @Override
                public void onPositiveAction() {

                }

                @Override
                public void onNegativeAction() {

                }
            });
        }
    };
}
