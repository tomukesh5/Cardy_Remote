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

import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;
import com.cardyapp.Views.CardyProgressBar;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.linkedin.platform.LISessionManager;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
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

public class SignInActivity extends BaseSocialSignInActivity implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(sequence = 1, message = "Please enter email address")
    @Email(sequence = 2, message = "Invalid email address")
    @BindView(R.id.et_email)
    public EditText mEtEmail;

    @Order(2)
    @NotEmpty(sequence = 1, message = "Please enter Password")
    @Password(min = 5, scheme = Password.Scheme.NUMERIC, message = "Please enter a min 5 digit Password")
    @BindView(R.id.et_password)
    public EditText mEtPassword;

    @BindView(R.id.progressBar)
    public CardyProgressBar mProgress;

    private Validator mValidator;

    @Override
    protected void getSocialData(String email, String password, String socialType, String socialUserData) {
        signInWithSocial(email, password, socialType, socialUserData);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @OnTextChanged(value = R.id.et_password,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_Password(Editable editable) {
        if (editable.length() > 0) {
            mEtPassword.setError(null);
        }
    }

    @OnTextChanged(value = R.id.et_email,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_Email(Editable editable) {
        if (editable.length() > 0) {
            mEtEmail.setError(null);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_in;
    }

    @OnClick(R.id.btn_signIn)
    public void onClickBtn_signUp() {
        validate();
    }

    @OnClick(R.id.ll_signUp)
    public void onClickSignUp() {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    @OnClick(R.id.tv_forgotPassword)
    public void onClickForgotPassword() {
        startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
    }

    // handle the respone by calling LISessionManager and start new activity
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        requestCode, resultCode, data);

        /*Intent intent = new Intent(MainActivity.this, HomePage.class);
        startActivity(intent);*/
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

    public void validate() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        signInWithSocial(mEtEmail.getText() + "", mEtPassword.getText() + "", "", "");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String[] listMessage = error.getCollatedErrorMessage(SignInActivity.this).split("\n");
            String message = listMessage[0];
            //String message = error.getCollatedErrorMessage(getActivity());
            // Display error messages ;)
            if (view instanceof EditText) {
                EditText text = ((EditText) view);
                text.setError(message);
            }
        }
    }

    private void signInWithSocial(String email, String password, String socialusertype, String socialUserData) {
        mProgress.setVisibility(View.VISIBLE);
        CardySingleton.getInstance().callToSignInAPI(email, password, socialusertype, socialUserData, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                mProgress.setVisibility(View.GONE);
                Log.d(AppConstants.TAG, response.toString());
                Toast.makeText(SignInActivity.this, "onResponse", Toast.LENGTH_SHORT).show();
                DialogUtils.show(SignInActivity.this, response.message(), "CARDY", "OK", false, false, new DialogUtils.ActionListner() {
                    @Override
                    public void onPositiveAction() {
                        startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                    }

                    @Override
                    public void onNegativeAction() {

                    }
                });
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                mProgress.setVisibility(View.GONE);
                Log.d(AppConstants.TAG, "onFailure");
                Toast.makeText(SignInActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
