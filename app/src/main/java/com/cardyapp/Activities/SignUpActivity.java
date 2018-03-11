package com.cardyapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cardyapp.Models.SignUpModel;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;
import com.cardyapp.Utils.IntentExtras;
import com.cardyapp.Views.CardyProgressBar;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.linkedin.platform.LISessionManager;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
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

public class SignUpActivity extends BaseSocialSignInActivity implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(sequence = 1, message = "Please enter mobile number")
    @Length(min = 10, max = 10, sequence = 2, message = "Mobile number must be 10 digits")
    @BindView(R.id.et_mobileNo)
    public EditText mEtMobileNo;

    @Order(2)
    @NotEmpty(sequence = 1, message = "Please enter Password")
    @Password(min = 5, message = "Please enter a min 5 digit Password")
    @BindView(R.id.et_password)
    public EditText mEtPassword;

    @Order(3)
    @NotEmpty(sequence = 1, message = "Please enter confirm Password")
    @ConfirmPassword(sequence = 2, message = "Passwords do not match")
    @BindView(R.id.et_confirmPassword)
    public EditText mEtConfirmPassword;

    @BindView(R.id.progressBar)
    public CardyProgressBar mProgress;

    private String email;

    private Validator mValidator;

    @Override
    protected void getSocialData(String email, String password, String socialType, String socialUserData) {
        /*Intent intent = new Intent(SignUpActivity.this, SocialSignUpMobileNumberActivity.class);
        intent.putExtra(IntentExtras.SOCIAL_DATA, socialUserData);
        intent.putExtra(IntentExtras.SOCIAL_TYPE, socialType);
        startActivity(intent);
        finish();*/

        Intent intent = new Intent(SignUpActivity.this, ForgotPasswordActivity.class);
        intent.putExtra(IntentExtras.SOCIAL_LOGIN_TOKEN, password);
        intent.putExtra(IntentExtras.SOCIAL_TYPE, socialType);
        intent.putExtra(IntentExtras.SOCIAL_DATA, socialUserData);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
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

    @OnTextChanged(value = R.id.et_mobileNo,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_Email(Editable editable) {
        if (editable.length() > 0) {
            mEtMobileNo.setError(null);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_up;
    }

    @OnClick(R.id.btn_signUp)
    public void onClickBtn_signUp() {
        //startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        validate();
    }

    // handle the respone by calling LISessionManager and start new activity
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        requestCode, resultCode, data);

        getCallbackManager().onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            getDataFromGPlus(result);
        }
    }

    @OnClick(R.id.ll_linkedin)
    public void onClickLL_Linkedin() {
        linkedinLogin();
    }

    @OnClick(R.id.ll_fb)
    public void onFacebookLogin() {
        onFbLogin();
    }

    @OnClick(R.id.ll_google)
    public void onGooglePluseLogin() {
        googleLogin();
    }

    public void signUp(String email, String password, String socialusertype, String socialUserData) {
        this.email = email;
        showProgress("");

        CardySingleton.getInstance().callToSignUpAPI(email, password, socialusertype, socialUserData, Callback_SignUp);
    }

    public void validate() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        signUp(mEtMobileNo.getText() + "", mEtPassword.getText() + "", "", "");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String[] listMessage = error.getCollatedErrorMessage(SignUpActivity.this).split("\n");
            String message = listMessage[0];
            //String message = error.getCollatedErrorMessage(getActivity());
            // Display error messages ;)
            if (view instanceof EditText) {
                EditText text = ((EditText) view);
                text.setError(message);
            }
        }
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
                userdata.setUser_mobile(email);
                userdata.setUserid(signUpModel.getUserid());
                Intent intent = new Intent(SignUpActivity.this, OTPActivity.class);
                intent.putExtra(IntentExtras.USER_DTO, userdata);
                startActivity(intent);
                finish();
            } else {
                DialogUtils.show(SignUpActivity.this, response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
            DialogUtils.show(SignUpActivity.this, getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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

